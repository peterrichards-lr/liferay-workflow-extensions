package com.liferay.workflow.user.group.creator.action.executor;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowEntityCreatorActionExecutor;
import com.liferay.workflow.extensions.common.configuration.model.MethodParameterConfiguration;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import com.liferay.workflow.user.group.creator.configuration.UserGroupCreatorConfiguration;
import com.liferay.workflow.user.group.creator.configuration.UserGroupCreatorConfigurationWrapper;
import com.liferay.workflow.user.group.creator.constants.UserGroupCreatorConstants;
import com.liferay.workflow.user.group.creator.settings.UserGroupCreatorSettingsHelper;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author peterrichards
 */
@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = UserGroupCreatorConfiguration.PID
)
public class UserGroupCreator extends BaseWorkflowEntityCreatorActionExecutor<UserGroupCreatorConfiguration, UserGroupCreatorConfigurationWrapper, UserGroupCreatorSettingsHelper> implements ActionExecutor {
    @Reference
    private UserGroupCreatorSettingsHelper _userGroupCreatorSettingsHelper;
    @Reference
    private UserGroupLocalService _userGroupLocalService;
    @Reference
    private UserLocalService _userLocalService;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;
    @Reference
    private WorkflowStatusManager _workflowStatusManager;

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final UserGroupCreatorConfigurationWrapper configuration, final User actionUser) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final ServiceContext serviceContext = executionContext.getServiceContext();
            final boolean success = createUserGroup(actionUser, workflowContext, serviceContext, configuration);
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

    private boolean createUserGroup(final User creator, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final UserGroupCreatorConfigurationWrapper configuration) throws ActionExecutorException {
        final long companyId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
        final Map<String, Object> methodParameters = buildMethodParametersMap(workflowContext, serviceContext, configuration);
        final String name = (String) methodParameters.get(UserGroupCreatorConstants.METHOD_PARAM_NAME);
        final String description = (String) methodParameters.get(UserGroupCreatorConstants.METHOD_PARAM_DESCRIPTION);
        try {
            final UserGroup newUserGroup = _userGroupLocalService.addUserGroup(creator.getUserId(), companyId, name, description, serviceContext);
            WorkflowExtensionsUtil.runIndexer(newUserGroup, serviceContext);
            if (newUserGroup != null) {
                final String identifierWorkflowKey = configuration.getCreatedEntityIdentifierWorkflowContextKey();
                final long userGroupId = newUserGroup.getUserGroupId();
                _log.debug("New user group created: {}", userGroupId);
                workflowContext.put(identifierWorkflowKey, userGroupId);
                return true;
            }
            _log.warn("The addUserGroup returned null");
            return false;
        } catch (final PortalException e) {
            _log.error("Unable to create user group", e);
            return false;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected Map<String, MethodParameterConfiguration> getEntityCreationAttributeMap() {
        return new HashMap<>() {{
            put(UserGroupCreatorConstants.METHOD_PARAM_NAME, new MethodParameterConfiguration(UserGroupCreatorConstants.METHOD_PARAM_NAME, String.class, true, null));
            put(UserGroupCreatorConstants.METHOD_PARAM_DESCRIPTION, new MethodParameterConfiguration(UserGroupCreatorConstants.METHOD_PARAM_DESCRIPTION, String.class, false, StringPool.BLANK));
        }};
    }

    @Override
    protected UserGroupCreatorSettingsHelper getSettingsHelper() {
        return _userGroupCreatorSettingsHelper;
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