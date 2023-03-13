package com.liferay.workflow.account.entry.finder.action.executor;

import com.liferay.account.model.AccountEntry;
import com.liferay.account.model.AccountEntryUserRel;
import com.liferay.account.model.AccountRole;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.account.service.AccountEntryUserRelLocalService;
import com.liferay.account.service.AccountRoleLocalService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.account.entry.finder.configuration.AccountEntryFinderConfiguration;
import com.liferay.workflow.account.entry.finder.configuration.AccountEntryFinderConfigurationWrapper;
import com.liferay.workflow.account.entry.finder.settings.AccountEntryFinderSettingsHelper;
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowActionExecutor;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author peterrichards
 */
@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = AccountEntryFinderConfiguration.PID
)
public class AccountEntryFinder extends BaseWorkflowActionExecutor<AccountEntryFinderConfiguration, AccountEntryFinderConfigurationWrapper, AccountEntryFinderSettingsHelper> implements ActionExecutor {
    @Reference
    private AccountEntryLocalService _accountEntryLocalService;
    @Reference
    private AccountEntryUserRelLocalService _accountEntryUserRelLocalService;
    @Reference
    private AccountRoleLocalService _accountRoleLocalService;
    @Reference
    private RoleLocalService _roleLocalService;
    @Reference
    private AccountEntryFinderSettingsHelper _accountEntryFinderSettingsHelper;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;
    @Reference
    private WorkflowStatusManager _workflowStatusManager;

    @Override
    protected AccountEntryFinderSettingsHelper getSettingsHelper() {
        return _accountEntryFinderSettingsHelper;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final AccountEntryFinderConfigurationWrapper configuration) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final boolean success = findAccountEntry(workflowContext, configuration);
            if (configuration.isWorkflowStatusUpdatedOnSuccess() && success
            ) {
                updateWorkflowStatus(configuration.getSuccessWorkflowStatus(), workflowContext);
            }
        } catch (final PortalException | RuntimeException e) {
            if (configuration == null) {
                throw new ActionExecutorException("Unable to determine if workflow status is updated on exception. Configuration is null");
            } else if (configuration.isWorkflowStatusUpdatedOnException()) {
                _log.error("Unexpected exception. See inner exception for details", e);
                try {
                    updateWorkflowStatus(configuration.getExceptionWorkflowStatus(), workflowContext);
                } catch (final WorkflowException ex) {
                    throw new ActionExecutorException("See inner exception", ex);
                }
            } else {
                _log.error("Unexpected exception. See inner exception for details", e);
            }
        }
    }

    private boolean findAccountEntry(final Map<String, Serializable> workflowContext, final AccountEntryFinderConfigurationWrapper configuration) throws WorkflowException {
        final long companyId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
        final String name = GetterUtil.getString(workflowContext.get(configuration.getEntityLookupNameValueWorkflowContextKey()));
        final String type = GetterUtil.getString(workflowContext.get(configuration.getEntityLookupTypeValueWorkflowContextKey()));
        final AccountEntry accountEntry = fetchAccountEntry(name, type == null || type.isBlank() ? "business" : type);
        if (accountEntry != null) {
            final String identifierWorkflowKey = configuration.getEntityIdentifierWorkflowContextKey();
            final long accountEntryId = accountEntry.getAccountEntryId();
            _log.debug("Returning account entry identifier {} in {}", accountEntryId, identifierWorkflowKey);
            workflowContext.put(identifierWorkflowKey, accountEntryId);

            final List<AccountEntryUserRel> accountEntryUserRels = _accountEntryUserRelLocalService.getAccountEntryUserRelsByAccountEntryId(accountEntryId);
            if (accountEntryUserRels == null || accountEntryUserRels.isEmpty())
                return true;

            final AccountRole accountAdministratorRole = getAccountAdministratorRole(companyId);
            if (accountAdministratorRole == null)
                return true;

            boolean foundAdministratorUser = false;
            for(final AccountEntryUserRel accountEntryUserRel : accountEntryUserRels) {
                final long userId = accountEntryUserRel.getAccountUserId();
                try {
                    if (_accountRoleLocalService.hasUserAccountRole(accountEntryId, accountAdministratorRole.getAccountRoleId(), userId)){
                        final String administrationUserIdentifierWorkflowContextKey = configuration.getEntityAdministrationUserIdentifierWorkflowContextKey();
                        _log.debug("Returning account entry identifier {} in {}", userId, administrationUserIdentifierWorkflowContextKey);
                        workflowContext.put(administrationUserIdentifierWorkflowContextKey, userId);
                        foundAdministratorUser = true;
                        break;
                    }
                } catch (final PortalException e) {
                    throw new WorkflowException("Unable to find", e);
                }
            }

            if (!foundAdministratorUser) {
                _log.debug("Account administration user not found");
            }
            return true;
        }
        return false;
    }

    private AccountRole getAccountAdministratorRole(final long companyId) throws WorkflowException {
        final String roleName = "Account Administrator";
        try {
            final Role accountAdminRole = _roleLocalService.getRole(companyId, roleName);
            return _accountRoleLocalService.fetchAccountRoleByRoleId(accountAdminRole.getRoleId());
        } catch (final PortalException e) {
            throw new WorkflowException("Unable to lookup Account Administrator role", e);
        }
    }

    private AccountEntry fetchAccountEntry(final String name, final String type) {
        final DynamicQuery query = _accountEntryLocalService.dynamicQuery()
                .add(RestrictionsFactoryUtil.eq("name", name))
                .add(RestrictionsFactoryUtil.eq("type", type.toLowerCase(Locale.ROOT)));
        final List<AccountEntry> accountEntryList = _accountEntryLocalService.dynamicQuery(query);
        if (accountEntryList == null || accountEntryList.isEmpty()) {
            return null;
        } else if (accountEntryList.size() > 1) {
            _log.debug("Found more than one....");
        }
        return accountEntryList.get(0);
    }

    private void updateWorkflowStatus(final int status, final Map<String, Serializable> workflowContext) throws WorkflowException {
        try {
            if (status > -1) {
                if (_log.isDebugEnabled()) {
                    final String workflowLabelStatus = WorkflowConstants.getStatusLabel(status);
                    _log.debug("Setting workflow status to {} [{}]", workflowLabelStatus, status);
                }
                _workflowStatusManager.updateStatus(status, workflowContext);
            }
        } catch (final WorkflowException e) {
            throw new WorkflowException("Unable to update workflow status", e);
        }
    }
}