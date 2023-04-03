package com.liferay.workflow.user.group.roles.updater.action.executor;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowEntityCreatorActionExecutor;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.user.group.roles.updater.configuration.UserGroupRolesUpdaterConfiguration;
import com.liferay.workflow.user.group.roles.updater.configuration.UserGroupRolesUpdaterConfigurationWrapper;
import com.liferay.workflow.user.group.roles.updater.constants.UserGroupRolesUpdaterConstants;
import com.liferay.workflow.user.group.roles.updater.helper.Helper;
import com.liferay.workflow.user.group.roles.updater.helper.factory.HelperFactory;
import com.liferay.workflow.user.group.roles.updater.settings.UserGroupRolesUpdaterSettingsHelper;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.Map;

/**
 * @author peterrichards
 */
@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = UserGroupRolesUpdaterConfiguration.PID
)
public final class UserGroupRolesUpdater extends BaseWorkflowEntityCreatorActionExecutor<UserGroupRolesUpdaterConfiguration, UserGroupRolesUpdaterConfigurationWrapper, UserGroupRolesUpdaterSettingsHelper> implements ActionExecutor {
    @Reference
    private HelperFactory _helperFactory;

    @Reference
    private UserLocalService _userLocalService;
    @Reference
    private UserGroupRolesUpdaterSettingsHelper _userGroupRolesUpdaterSettingsHelper;
    @Reference
    private WorkflowStatusManager _workflowStatusManager;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }

    @Override
    protected WorkflowStatusManager getWorkflowStatusManager() {
        return _workflowStatusManager;
    }

    @Override
    protected UserGroupRolesUpdaterSettingsHelper getSettingsHelper() {
        return _userGroupRolesUpdaterSettingsHelper;
    }

    @Override
    protected UserLocalService getUserLocalService() {
        return _userLocalService;
    }

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final UserGroupRolesUpdaterConfigurationWrapper configuration, final User actionUser) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final ServiceContext serviceContext = executionContext.getServiceContext();
            final boolean success = addUserGroupRoles(actionUser, workflowContext, serviceContext, configuration);
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

    private boolean addUserGroupRoles(final User actionUser, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final UserGroupRolesUpdaterConfigurationWrapper configuration) throws PortalException {
        final String groupIdType = configuration.getGroupIdType();
        final Helper helper = _helperFactory.getHelper(UserGroupRolesUpdaterConstants.getHelperType(groupIdType));
        return helper.addUserGroupRoles(actionUser, workflowContext, serviceContext, configuration);
    }
}
