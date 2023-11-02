package com.liferay.workflow.user.group.updater.action.executor;

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
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowUserActionExecutor;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.util.EntityCreationAttributeUtil;
import com.liferay.workflow.extensions.common.util.StringUtil;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import com.liferay.workflow.user.group.updater.configuration.UserGroupUpdaterConfiguration;
import com.liferay.workflow.user.group.updater.configuration.UserGroupUpdaterConfigurationWrapper;
import com.liferay.workflow.user.group.updater.settings.UserGroupUpdaterSettingsHelper;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author peterrichards
 */
@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = UserGroupUpdaterConfiguration.PID
)
public class UserGroupUpdater extends BaseWorkflowUserActionExecutor<UserGroupUpdaterConfiguration, UserGroupUpdaterConfigurationWrapper, UserGroupUpdaterSettingsHelper> implements ActionExecutor {

    @Reference
    private UserGroupLocalService _userGroupLocalService;
    @Reference
    private UserGroupUpdaterSettingsHelper _userGroupUpdaterSettingsHelper;
    @Reference
    private UserLocalService _userLocalService;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final UserGroupUpdaterConfigurationWrapper configuration, final User actionUser) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final ServiceContext serviceContext = executionContext.getServiceContext();
            final boolean success = updateUserGroup(workflowContext, serviceContext, configuration);
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
    protected UserGroupUpdaterSettingsHelper getSettingsHelper() {
        return _userGroupUpdaterSettingsHelper;
    }

    private User getUser(final long companyId, final String lookupType, final String lookupValue) throws PortalException {
        final User entity;
        switch (lookupType) {
            case "screen-name":
                entity = getUserLocalService().fetchUserByScreenName(companyId, lookupValue);
                break;
            case "email-address":
                entity = getUserLocalService().fetchUserByEmailAddress(companyId, lookupValue);
                break;
            case "id":
                final long userId = GetterUtil.getLong(lookupValue);
                entity = getUserLocalService().getUserById(userId);
                break;
            default:
                throw new PortalException("Unknown lookup type: " + lookupType);
        }
        return entity;
    }

    private UserGroup getUserGroup(final String userGroupLookupValue) throws PortalException {
        final long userGroupId = GetterUtil.getLong(userGroupLookupValue);
        return _userGroupLocalService.getUserGroup(userGroupId);
    }

    private String getUserGroupLookupValue(final UserGroupUpdaterConfigurationWrapper configuration, final Map<String, Serializable> workflowContext) throws PortalException {
        if (configuration.isWorkflowContextKeyUsedForUserGroupId()) {
            final String workflowContextKey = configuration.getUserGroupIdWorkflowContextKey();
            if (workflowContext.containsKey(workflowContextKey)) {
                return String.valueOf(workflowContext.get(workflowContextKey));
            }
            throw new PortalException(workflowContextKey + " was not found in the workflow context");
        }
        return configuration.getUserGroupIdValue();
    }

    private Long[] getUserIds(final long companyId, final UserGroupUpdaterConfigurationWrapper configuration, final Map<String, Serializable> workflowContext) throws PortalException {
        final String lookupType = configuration.getValueLookupType();
        final String[] usersValues = getUserValues(configuration, workflowContext);
        final List<Long> userIds = new ArrayList<>(usersValues.length);
        for (final String userValue : usersValues) {
            final long id;
            try {
                id = getUser(companyId, lookupType, userValue).getUserId();
            } catch (final PortalException ex) {
                _log.warn("Unable to retrieve user", ex);
                continue;
            }
            if (id > -1) {
                userIds.add(id);
            }
        }
        return userIds.toArray(new Long[0]);
    }

    @Override
    protected UserLocalService getUserLocalService() {
        return _userLocalService;
    }

    private String[] getUserValues(final UserGroupUpdaterConfigurationWrapper configuration, final Map<String, Serializable> workflowContext) throws PortalException {
        final String userValues;
        if (configuration.isWorkflowContextKeyUsedForValueArray()) {
            final String workflowContextKey = configuration.getValueArrayWorkflowContextKey();
            if (workflowContext.containsKey(workflowContextKey)) {
                userValues = String.valueOf(workflowContext.get(workflowContextKey));
            } else {
                throw new PortalException(workflowContextKey + " was not found in the workflow context");
            }
        } else {
            userValues = Arrays.toString(configuration.getValueArray());
        }
        return StringUtil.isBlank(userValues)
                ? new String[0]
                : userValues.split(StringPool.COMMA);
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }

    private long lookupUserGroupId(final Map<String, Serializable> workflowContext, final UserGroupUpdaterConfigurationWrapper configuration) throws PortalException {
        final UserGroup userGroup;
        final String userGroupLookupValue = getUserGroupLookupValue(configuration, workflowContext);
        userGroup = getUserGroup(userGroupLookupValue);
        if (userGroup == null) {
            throw new PortalException("Unable to obtain user group id " + configuration.getIdentifier());
        }
        return userGroup.getUserGroupId();
    }

    private boolean updateUserGroup(final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final UserGroupUpdaterConfigurationWrapper configuration) throws PortalException {
        final long companyId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
        final long userGroupId = lookupUserGroupId(workflowContext, configuration);
        final long[] userIds = EntityCreationAttributeUtil.unboxed(getUserIds(companyId, configuration, workflowContext));
        try {
            _userLocalService.addUserGroupUsers(userGroupId, userIds);
            final UserGroup userGroup = _userGroupLocalService.getUserGroup(userGroupId);
            WorkflowExtensionsUtil.runIndexer(userGroup, serviceContext);
            return true;
        } catch (final PortalException e) {
            _log.error("Unable to update user group", e);
            return false;
        }
    }
}