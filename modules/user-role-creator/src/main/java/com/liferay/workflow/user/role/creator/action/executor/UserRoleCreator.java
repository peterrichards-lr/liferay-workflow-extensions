package com.liferay.workflow.user.role.creator.action.executor;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowEntityCreatorActionExecutor;
import com.liferay.workflow.extensions.common.configuration.model.MethodParameterConfiguration;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.util.StringUtil;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import com.liferay.workflow.user.role.creator.configuration.UserRoleCreatorConfiguration;
import com.liferay.workflow.user.role.creator.configuration.UserRoleCreatorConfigurationWrapper;
import com.liferay.workflow.user.role.creator.constants.UserRoleCreatorConstants;
import com.liferay.workflow.user.role.creator.settings.UserRoleCreatorSettingsHelper;
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
@Component(property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java", service = ActionExecutor.class, configurationPid = UserRoleCreatorConfiguration.PID)
public class UserRoleCreator extends BaseWorkflowEntityCreatorActionExecutor<UserRoleCreatorConfiguration, UserRoleCreatorConfigurationWrapper, UserRoleCreatorSettingsHelper> implements ActionExecutor {
    @Reference
    private RoleLocalService _roleLocalService;
    @Reference
    private UserLocalService _userLocalService;
    @Reference
    private UserRoleCreatorSettingsHelper _userRoleCreatorSettingsHelper;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;

    private boolean createRole(final User creator, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final UserRoleCreatorConfigurationWrapper configuration) throws ActionExecutorException {
        final Map<String, Object> methodParameters = buildMethodParametersMap(workflowContext, serviceContext, configuration);
        final String className = (String) methodParameters.get(UserRoleCreatorConstants.METHOD_PARAM_CLASS_NAME);
        final long classPK = (long) methodParameters.get(UserRoleCreatorConstants.METHOD_PARAM_CLASS_PRIMARY_KEY);
        final String name = (String) methodParameters.get(UserRoleCreatorConstants.METHOD_PARAM_NAME);
        final String title = (String) methodParameters.get(UserRoleCreatorConstants.METHOD_PARAM_TITLE);
        final String description = (String) methodParameters.get(UserRoleCreatorConstants.METHOD_PARAM_DESCRIPTION);
        final int type = (int) methodParameters.get(UserRoleCreatorConstants.METHOD_PARAM_TYPE);
        final String subtype = (String) methodParameters.get(UserRoleCreatorConstants.METHOD_PARAM_SUBTYPE);

        final Map<Locale, String> titleMap = title == null ? null : StringUtil.localiseString(title, LocaleUtil.getDefault());
        final Map<Locale, String> descriptionMap = description == null ? null : StringUtil.localiseString(description, LocaleUtil.getDefault());

        try {
            Role role = configuration.getReturnExistingEntityIdentifierIfFound() ? fetchRole(name, type) : null;
            if (role == null) {
                role = _roleLocalService.addRole(creator.getUserId(), className, classPK, name, titleMap, descriptionMap, type, subtype, serviceContext);
                WorkflowExtensionsUtil.runIndexer(role, serviceContext);
                _log.debug("New role created");
            } else {
                _log.debug("Existing role returned");
            }
            if (role != null) {
                final String identifierWorkflowKey = configuration.getCreatedEntityIdentifierWorkflowContextKey();
                final long roleId = role.getRoleId();
                _log.debug("Returning role identifier {} in {}", roleId, identifierWorkflowKey);
                workflowContext.put(identifierWorkflowKey, roleId);
                return true;
            }
            _log.warn("The addRole returned null");
            return false;
        } catch (final PortalException e) {
            _log.error("Unable to create role", e);
            return false;
        }
    }

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final UserRoleCreatorConfigurationWrapper configuration, final User actionUser) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final ServiceContext serviceContext = executionContext.getServiceContext();
            final boolean success = createRole(actionUser, workflowContext, serviceContext, configuration);
            if (configuration.isWorkflowStatusUpdatedOnSuccess() && success) {
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

    private Role fetchRole(final String name, final int type) {
        final DynamicQuery query = _roleLocalService.dynamicQuery().add(RestrictionsFactoryUtil.eq("name", name)).add(RestrictionsFactoryUtil.eq("type", type));
        final List<Role> RoleList = _roleLocalService.dynamicQuery(query);
        if (RoleList == null || RoleList.isEmpty()) {
            return null;
        } else if (RoleList.size() > 1) {
            _log.debug("Found more than one....");
        }
        return RoleList.get(0);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Map<String, MethodParameterConfiguration> getEntityCreationAttributeMap() {
        return new HashMap<>() {{
            put(UserRoleCreatorConstants.METHOD_PARAM_CLASS_NAME, new MethodParameterConfiguration(UserRoleCreatorConstants.METHOD_PARAM_CLASS_NAME, String.class, false, null));
            put(UserRoleCreatorConstants.METHOD_PARAM_CLASS_PRIMARY_KEY, new MethodParameterConfiguration(UserRoleCreatorConstants.METHOD_PARAM_CLASS_PRIMARY_KEY, long.class, false, 0L));
            put(UserRoleCreatorConstants.METHOD_PARAM_NAME, new MethodParameterConfiguration(UserRoleCreatorConstants.METHOD_PARAM_NAME, String.class, true, null));
            put(UserRoleCreatorConstants.METHOD_PARAM_TITLE, new MethodParameterConfiguration(UserRoleCreatorConstants.METHOD_PARAM_TITLE, String.class, false, null));
            put(UserRoleCreatorConstants.METHOD_PARAM_DESCRIPTION, new MethodParameterConfiguration(UserRoleCreatorConstants.METHOD_PARAM_DESCRIPTION, String.class, false, null));
            put(UserRoleCreatorConstants.METHOD_PARAM_TYPE, new MethodParameterConfiguration(UserRoleCreatorConstants.METHOD_PARAM_TYPE, int.class, false, RoleConstants.TYPE_REGULAR));
            put(UserRoleCreatorConstants.METHOD_PARAM_SUBTYPE, new MethodParameterConfiguration(UserRoleCreatorConstants.METHOD_PARAM_SUBTYPE, String.class, false, null));
        }};
    }

    @Override
    protected UserRoleCreatorSettingsHelper getSettingsHelper() {
        return _userRoleCreatorSettingsHelper;
    }

    @Override
    protected UserLocalService getUserLocalService() {
        return _userLocalService;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }
}
