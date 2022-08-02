package com.liferay.workflow.account.entry.creator.action.executor;

import com.liferay.account.constants.AccountConstants;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.commerce.account.constants.CommerceAccountConstants;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.account.entry.creator.configuration.AccountEntryCreatorConfiguration;
import com.liferay.workflow.account.entry.creator.configuration.AccountEntryCreatorConfigurationWrapper;
import com.liferay.workflow.account.entry.creator.constants.AccountEntryCreatorConstants;
import com.liferay.workflow.account.entry.creator.settings.AccountEntryCreatorSettingsHelper;
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowEntityCreatorActionExecutor;
import com.liferay.workflow.extensions.common.configuration.model.MethodParameterConfiguration;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import org.jsoup.helper.StringUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author peterrichards
 */
@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = AccountEntryCreatorConfiguration.PID
)
public final class AccountEntryCreator extends BaseWorkflowEntityCreatorActionExecutor<AccountEntryCreatorConfiguration, AccountEntryCreatorConfigurationWrapper, AccountEntryCreatorSettingsHelper> implements ActionExecutor {
    @Reference
    private AccountEntryLocalService _accountEntryLocalService;
    @Reference
    private UserLocalService _userLocalService;
    @Reference
    private AccountEntryCreatorSettingsHelper _accountEntryCreatorSettingsHelper;
    @Reference
    private WorkflowStatusManager _workflowStatusManager;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;

    @Override
    protected UserLocalService getUserLocalService() {
        return _userLocalService;
    }

    @Override
    protected AccountEntryCreatorSettingsHelper getSettingsHelper() {
        return _accountEntryCreatorSettingsHelper;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final AccountEntryCreatorConfigurationWrapper configuration, final User actionUser) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final ServiceContext serviceContext = executionContext.getServiceContext();
            final boolean success = createAccountEntry(actionUser, workflowContext, serviceContext, configuration);
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Map<String, MethodParameterConfiguration> getEntityCreationAttributeMap() {
        return new HashMap<>() {{
            put(AccountEntryCreatorConstants.METHOD_PARAM_PARENT_ID, new MethodParameterConfiguration(AccountEntryCreatorConstants.METHOD_PARAM_PARENT_ID, Long.class, false,
                    CommerceAccountConstants.DEFAULT_PARENT_ACCOUNT_ID));
            put(AccountEntryCreatorConstants.METHOD_PARAM_NAME, new MethodParameterConfiguration(AccountEntryCreatorConstants.METHOD_PARAM_NAME, String.class, true, null));
            put(AccountEntryCreatorConstants.METHOD_PARAM_DESCRIPTION, new MethodParameterConfiguration(AccountEntryCreatorConstants.METHOD_PARAM_DESCRIPTION, String.class, false, StringPool.BLANK));
            put(AccountEntryCreatorConstants.METHOD_PARAM_DOMAINS, new MethodParameterConfiguration(AccountEntryCreatorConstants.METHOD_PARAM_DOMAINS, String[].class, false, null));
            put(AccountEntryCreatorConstants.METHOD_PARAM_EMAIL_ADDRESS, new MethodParameterConfiguration(AccountEntryCreatorConstants.METHOD_PARAM_EMAIL_ADDRESS, String.class, false, null));
            put(AccountEntryCreatorConstants.METHOD_PARAM_LOGO_BASE_64, new MethodParameterConfiguration(AccountEntryCreatorConstants.METHOD_PARAM_LOGO_BASE_64, String.class, false, null));
            put(AccountEntryCreatorConstants.TAX_ID_NUMBER, new MethodParameterConfiguration(AccountEntryCreatorConstants.TAX_ID_NUMBER, String.class, false, null));
            put(AccountEntryCreatorConstants.METHOD_PARAM_TYPE, new MethodParameterConfiguration(AccountEntryCreatorConstants.METHOD_PARAM_TYPE, String.class, false, AccountConstants.ACCOUNT_ENTRY_TYPE_GUEST));
            put(AccountEntryCreatorConstants.METHOD_PARAM_STATUS, new MethodParameterConfiguration(AccountEntryCreatorConstants.METHOD_PARAM_STATUS, Integer.class, false, WorkflowConstants.STATUS_APPROVED));
        }};
    }

    private boolean createAccountEntry(final User creator, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final AccountEntryCreatorConfigurationWrapper configuration) throws ActionExecutorException {
        final Map<String, Object> methodParameters = buildMethodParametersMap(workflowContext, serviceContext, configuration);

        final long parentId = (long) methodParameters.get(AccountEntryCreatorConstants.METHOD_PARAM_PARENT_ID);
        final String name = (String) methodParameters.get(AccountEntryCreatorConstants.METHOD_PARAM_NAME);
        final String description = (String) methodParameters.get(AccountEntryCreatorConstants.METHOD_PARAM_DESCRIPTION);
        final String[] domains = (String[]) methodParameters.get(AccountEntryCreatorConstants.METHOD_PARAM_DOMAINS);
        final String emailAddress = (String) methodParameters.get(AccountEntryCreatorConstants.METHOD_PARAM_EMAIL_ADDRESS);

        final String logoBase64 = (String) methodParameters.get(AccountEntryCreatorConstants.METHOD_PARAM_LOGO_BASE_64);
        final byte[] logoBytes = StringUtil.isBlank(logoBase64) ? null : Base64.decode(logoBase64);

        final String taxIdNumber = (String) methodParameters.get(AccountEntryCreatorConstants.TAX_ID_NUMBER);
        final String type = (String) methodParameters.get(AccountEntryCreatorConstants.METHOD_PARAM_TYPE);
        final int status = (int) methodParameters.get(AccountEntryCreatorConstants.METHOD_PARAM_STATUS);

        try {
            AccountEntry accountEntry = configuration.useExistingIfFound() ? fetchAccountEntry(name, type) : null;
            if (accountEntry == null) {
                accountEntry = _accountEntryLocalService.addAccountEntry(creator.getUserId(), parentId, name, description, domains, emailAddress, logoBytes, taxIdNumber, type, status, serviceContext);
                WorkflowExtensionsUtil.runIndexer(accountEntry, serviceContext);
                _log.debug("New account entry created");
            } else {
                _log.debug("Existing account entry returned");
            }

            if (accountEntry != null) {
                final String identifierWorkflowKey = configuration.getCreatedEntityIdentifierWorkflowContextKey();
                final long accountEntryId = accountEntry.getAccountEntryId();
                _log.debug("Returning account entry identifier {} in {}", accountEntryId, identifierWorkflowKey);
                workflowContext.put(identifierWorkflowKey, accountEntryId);
                return true;
            }
            _log.warn("The addAccountEntry returned null");
            return false;
        } catch (final PortalException e) {
            _log.error("Unable to create account entry", e);
            return false;
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