package com.liferay.workflow.custom.field.updater.action.executor;


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
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowUserActionExecutor;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
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
public final class CustomFieldUpdater extends BaseWorkflowUserActionExecutor<CustomFieldUpdaterConfiguration, CustomFieldUpdaterConfigurationWrapper, CustomFieldUpdaterSettingsHelper> implements ActionExecutor {

    @Reference
    private UpdateHelperFactory _UpdateHelperFactory;
    @Reference
    private UserLocalService _userLocalService;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;
    @Reference
    private WorkflowStatusManager _workflowStatusManager;
    @Reference
    private CustomFieldUpdaterSettingsHelper customFieldUpdaterSettingsHelper;

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final CustomFieldUpdaterConfigurationWrapper configuration, final User actionUser) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final ServiceContext serviceContext = executionContext.getServiceContext();
            final boolean success = updateCustomField(configuration, workflowContext, serviceContext, actionUser);
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

    @Override
    protected UserLocalService getUserLocalService() {
        return _userLocalService;
    }

    private boolean updateCustomField(final CustomFieldUpdaterConfigurationWrapper configuration, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final User actionUser) throws PortalException {
        final long companyId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
        final String lookupType = configuration.getLookupType();
        final String lookupValue = getLookupValue(configuration, workflowContext);
        final List<CustomFieldPair> customFieldPairList = configuration.getCustomFieldPairsList();
        final String entityTypeLabel = configuration.getEntityType();
        final int entityType = CustomFieldUpdaterConstants.getEntityType(entityTypeLabel);
        final EntityUpdateHelper entityUpdateHelper = _UpdateHelperFactory.getEntityUpdateHelper(entityType);
        return entityUpdateHelper.updateCustomFields(actionUser, companyId, lookupType, lookupValue, customFieldPairList, workflowContext, serviceContext);
    }

    private String getLookupValue(final CustomFieldUpdaterConfigurationWrapper configuration, final Map<String, Serializable> workflowContext) throws PortalException {
        if (configuration.isWorkflowContextKeyUsedForLookup()) {
            final String workflowContextKey = configuration.getLookupValueWorkflowContextKey();
            if (workflowContext.containsKey(workflowContextKey)) {
                return String.valueOf(workflowContext.get(workflowContextKey));
            }
            throw new PortalException(workflowContextKey + " was not found in the workflow context");
        }
        return configuration.getLookupValue();
    }

    @Override
    protected CustomFieldUpdaterSettingsHelper getSettingsHelper() {
        return customFieldUpdaterSettingsHelper;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }

    @Override
    protected WorkflowStatusManager getWorkflowStatusManager() {
        return _workflowStatusManager;
    }
}
