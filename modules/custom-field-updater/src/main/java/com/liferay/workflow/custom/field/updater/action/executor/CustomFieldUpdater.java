package com.liferay.workflow.custom.field.updater.action.executor;


import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.custom.field.updater.configuration.CustomFieldUpdaterConfiguration;
import com.liferay.workflow.custom.field.updater.configuration.CustomFieldUpdaterConfigurationWrapper;
import com.liferay.workflow.custom.field.updater.configuration.model.CustomFieldPair;
import com.liferay.workflow.custom.field.updater.constants.CustomFieldUpdaterConstants;
import com.liferay.workflow.custom.field.updater.helper.EntityUpdateHelper;
import com.liferay.workflow.custom.field.updater.helper.factory.UpdateHelperFactory;
import com.liferay.workflow.custom.field.updater.settings.CustomFieldUpdaterSettingsHelper;
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowActionExecutor;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import org.jsoup.helper.StringUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = CustomFieldUpdaterConfiguration.PID
)
public final class CustomFieldUpdater extends BaseWorkflowActionExecutor<CustomFieldUpdaterConfiguration, CustomFieldUpdaterConfigurationWrapper, CustomFieldUpdaterSettingsHelper> implements ActionExecutor {

    @Reference
    private CustomFieldUpdaterSettingsHelper customFieldUpdaterSettingsHelper;
    @Reference
    private WorkflowStatusManager _workflowStatusManager;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;
    @Reference
    private UserLocalService _userLocalService;
    @Reference
    private UpdateHelperFactory _UpdateHelperFactory;

    @Override
    protected CustomFieldUpdaterSettingsHelper getSettingsHelper() {
        return customFieldUpdaterSettingsHelper;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }

    @Override
    protected void execute(KaleoAction kaleoAction, ExecutionContext executionContext, WorkflowActionExecutionContext workflowExecutionContext, CustomFieldUpdaterConfigurationWrapper configuration) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final ServiceContext serviceContext = executionContext.getServiceContext();
            final boolean success = updateCustomField(configuration, workflowContext, serviceContext);
            if (configuration.isWorkflowStatusUpdatedOnSuccess() && success
            ) {
                updateWorkflowStatus(configuration.getSuccessWorkflowStatus(), workflowContext);
            }
        } catch (PortalException | RuntimeException e) {
            if (configuration == null) {
                throw new ActionExecutorException("Unable to determine if workflow status is updated on exception. Configuration is null");
            } else if (configuration.isWorkflowStatusUpdatedOnException()) {
                _log.error("Unexpected exception. See inner exception for details", e);
                try {
                    updateWorkflowStatus(configuration.getExceptionWorkflowStatus(), workflowContext);
                } catch (WorkflowException ex) {
                    throw new ActionExecutorException("See inner exception", ex);
                }
            } else {
                _log.error("Unexpected exception. See inner exception for details", e);
            }
        }
    }

    private boolean updateCustomField(CustomFieldUpdaterConfigurationWrapper configuration, Map<String, Serializable> workflowContext, ServiceContext serviceContext) throws PortalException {
        final User user;
        if (configuration.isInContextUserRequired()) {
            final long userId = GetterUtil.getLong((String) workflowContext.get(WorkflowConstants.CONTEXT_USER_ID));
            user = lookupUser(userId);
        } else {
            user = lookupUser(configuration, workflowContext);
        }

        final long companyId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
        final String lookupType = configuration.getLookupType();
        final String lookupValue = configuration.getLookupValue();
        final List<CustomFieldPair> customFieldPairList = configuration.getCustomFieldPairsList();

        final String entityTypeLabel = configuration.getEntityType();
        final int entityType = CustomFieldUpdaterConstants.getEntityType(entityTypeLabel);

        EntityUpdateHelper entityUpdateHelper = _UpdateHelperFactory.getEntityUpdateHelper(entityType);
        return entityUpdateHelper.updateCustomFields(user, companyId, lookupType, lookupValue, customFieldPairList, workflowContext, serviceContext);
    }


    private User lookupUser(CustomFieldUpdaterConfigurationWrapper configuration, Map<String, Serializable> workflowContext) throws PortalException {
        final long companyId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
        final String lookupValue;
        final String lookupType = configuration.getUserLookupType().toLowerCase();
        if (configuration.isWorkflowContextKeyUsedForUserLookup()) {
            final String workflowKey = configuration.getUserLookupValueWorkflowContextKey();
            lookupValue = workflowContext.containsKey(workflowKey) ?
                    GetterUtil.getString(workflowContext.get(workflowKey)) :
                    StringPool.BLANK;
        } else {
            lookupValue = configuration.getUserLookupValue();
        }

        if (StringUtil.isBlank(lookupValue)) {
            throw new PortalException("Unable to find user because the lookup value was blank");
        }

        switch (lookupType) {
            case "screen-name":
                return _userLocalService.fetchUserByScreenName(companyId, lookupValue);
            case "email-address":
                return _userLocalService.fetchUserByEmailAddress(companyId, lookupValue);
            case "user-id":
                final long userId = GetterUtil.getLong(lookupValue);
                return lookupUser(userId);
        }
        throw new PortalException("Unknown lookup type: " + lookupType);
    }

    private User lookupUser(long userId) throws PortalException {
        return _userLocalService.getUserById(userId);
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
        } catch (WorkflowException e) {
            throw new WorkflowException("Unable to update workflow status", e);
        }
    }
}
