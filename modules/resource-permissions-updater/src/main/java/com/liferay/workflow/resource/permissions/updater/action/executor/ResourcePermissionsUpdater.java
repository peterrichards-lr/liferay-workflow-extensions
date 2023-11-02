package com.liferay.workflow.resource.permissions.updater.action.executor;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowUserActionExecutor;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.resource.permissions.updater.configuration.ResourcePermissionsUpdaterConfiguration;
import com.liferay.workflow.resource.permissions.updater.configuration.ResourcePermissionsUpdaterConfigurationWrapper;
import com.liferay.workflow.resource.permissions.updater.constants.ResourcePermissionsUpdaterConstants;
import com.liferay.workflow.resource.permissions.updater.helper.Helper;
import com.liferay.workflow.resource.permissions.updater.helper.factory.HelperFactory;
import com.liferay.workflow.resource.permissions.updater.settings.ResourcePermissionsUpdaterSettingsHelper;
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
        configurationPid = ResourcePermissionsUpdaterConfiguration.PID
)
public class ResourcePermissionsUpdater extends BaseWorkflowUserActionExecutor<ResourcePermissionsUpdaterConfiguration, ResourcePermissionsUpdaterConfigurationWrapper, ResourcePermissionsUpdaterSettingsHelper> implements ActionExecutor {
    @Reference
    private HelperFactory _helperFactory;
    @Reference
    private ResourcePermissionsUpdaterSettingsHelper _resourcePermissionsUpdaterSettingsHelper;
    @Reference
    private UserLocalService _userLocalService;
    @Reference
    private RoleLocalService _roleLocalService;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final ResourcePermissionsUpdaterConfigurationWrapper configuration, final User actionUser) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final ServiceContext serviceContext = executionContext.getServiceContext();
            final boolean success = updateResourcePermissions(actionUser, workflowContext, serviceContext, configuration);
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

    private String getLookupValue(final ResourcePermissionsUpdaterConfigurationWrapper configuration, final Map<String, Serializable> workflowContext) throws PortalException {
        if (configuration.isWorkflowContextKeyUsedForEntityLookup()) {
            final String workflowContextKey = configuration.getEntityLookupValueWorkflowContextKey();
            if (workflowContext.containsKey(workflowContextKey)) {
                return String.valueOf(workflowContext.get(workflowContextKey));
            }
            throw new PortalException(workflowContextKey + " was not found in the workflow context");
        }
        return configuration.getEntityLookupValue();
    }

    @Override
    protected ResourcePermissionsUpdaterSettingsHelper getSettingsHelper() {
        return _resourcePermissionsUpdaterSettingsHelper;
    }

    @Override
    protected UserLocalService getUserLocalService() {
        return _userLocalService;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }

    private boolean updateResourcePermissions(final User actionUser, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final ResourcePermissionsUpdaterConfigurationWrapper configuration) throws PortalException {
        final long roleId = lookupRoleId(workflowContext, configuration);
        final long groupId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_GROUP_ID));
        final String lookupType = configuration.getEntityLookupType();
        final String lookupValue = getLookupValue(configuration, workflowContext);
        final String[] actionIds = configuration.getActionIds();
        final String entityTypeLabel = configuration.getEntityType();
        final int entityType = ResourcePermissionsUpdaterConstants.getEntityType(entityTypeLabel);
        final Helper helper = _helperFactory.getHelper(entityType);
        return helper.setResourcePermissions(actionUser, roleId, groupId, lookupType, lookupValue, actionIds, workflowContext, serviceContext);
    }

    private long lookupRoleId(final Map<String, Serializable> workflowContext, final ResourcePermissionsUpdaterConfigurationWrapper configuration) throws PortalException {
        final Role role;
        final long companyId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
        final String roleLookupType = configuration.getRoleLookupType();
        final String roleLookupValue = getRoleLookupValue(configuration, workflowContext);
        role = geRole(companyId, roleLookupType, roleLookupValue);
        if (role == null) {
            throw new PortalException("Unable to obtain user group id " + configuration.getIdentifier());
        }
        return role.getRoleId();
    }

    private Role geRole(final long companyId, final String lookupType, final String lookupValue) throws PortalException {
        final Role role;
        switch (lookupType) {
            case "name":
                role = _roleLocalService.getRole(companyId, lookupValue);
                break;
            case "id":
                final long roleId = GetterUtil.getLong(lookupValue);
                role = _roleLocalService.getRole(roleId);
                break;
            default:
                throw new PortalException("Unknown lookup type: " + lookupType);
        }
        return role;
    }

    private String getRoleLookupValue(final ResourcePermissionsUpdaterConfigurationWrapper configuration, final Map<String, Serializable> workflowContext) throws PortalException {
        if (configuration.isWorkflowContextKeyUsedForRoleLookup()) {
            final String workflowContextKey = configuration.getRoleLookupValueWorkflowContextKey();
            if (workflowContext.containsKey(workflowContextKey)) {
                return String.valueOf(workflowContext.get(workflowContextKey));
            }
            throw new PortalException(workflowContextKey + " was not found in the workflow context");
        }
        return configuration.getRoleLookupValue();
    }
}