package com.liferay.workflow.user.group.roles.updater.helper;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupRoleService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.workflow.extensions.common.util.EntityCreationAttributeUtil;
import com.liferay.workflow.extensions.common.util.StringUtil;
import com.liferay.workflow.extensions.common.util.UserLookupHelper;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import com.liferay.workflow.user.group.roles.updater.configuration.UserGroupRolesUpdaterConfigurationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("EmptyMethod")
public abstract class BaseHelper implements Helper {
    protected final Logger _log = LoggerFactory.getLogger(getClass());

    protected abstract UserGroupRoleService getUserGroupRoleService();

    protected abstract UserLocalService getUserLocalService();

    protected abstract RoleLocalService getRoleLocalService();

    @Override
    public final boolean addUserGroupRoles(final User actionUser, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final UserGroupRolesUpdaterConfigurationWrapper configuration) throws PortalException {
        final long companyId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
        final long groupId = getGroupId(workflowContext, serviceContext, configuration);
        final long userId = lookupUserId(companyId, workflowContext, configuration);
        final long[] roleIds = EntityCreationAttributeUtil.unboxed(getRoleIds(companyId, configuration.getRoles()));

        if (roleIds.length == 0) {
            _log.warn("No roles found");
            return false;
        }

        WorkflowExtensionsUtil.setupPermissionChecker(actionUser);
        WorkflowExtensionsUtil.setupPrincipalThread(actionUser);

        beforeChange(companyId, groupId, userId, roleIds, workflowContext, serviceContext, configuration);
        try {
            getUserGroupRoleService().addUserGroupRoles(userId, groupId, roleIds);
            return true;
        } catch (final PrincipalException e) {
            _log.error("The action user does not have permissions to assign group roles");
            return false;
        } finally {
            afterChange(companyId, groupId, userId, roleIds, workflowContext, serviceContext, configuration);
        }
    }

    private UserLookupHelper getUserLookupHelper() {
        return new UserLookupHelper();
    }

    private long lookupUserId(final long companyId, final Map<String, Serializable> workflowContext, final UserGroupRolesUpdaterConfigurationWrapper configuration) throws PortalException {
        return getUserLookupHelper().lookupUserId(getUserLocalService(), companyId, workflowContext, configuration);
    }

    private User getUser(final long companyId, final String lookupType, final String lookupValue) throws PortalException {
        return getUserLookupHelper().getUser(getUserLocalService(), companyId, lookupType, lookupValue);
    }

    private String getUserLookupValue(final UserGroupRolesUpdaterConfigurationWrapper configuration, final Map<String, Serializable> workflowContext) throws PortalException {
        return getUserLookupHelper().getUserLookupValue(configuration, workflowContext);
    }

    private Long[] getRoleIds(final long companyId, final String[] roles) {
        final List<Long> roleIds = new ArrayList<>(roles.length);
        for (final String roleValue : roles) {
            long id;
            try {
                id = Long.parseLong(roleValue);
            } catch (final NumberFormatException e) {
                try {
                    final Role role = getRoleLocalService().getRole(companyId, roleValue);
                    id = role.getRoleId();
                } catch (final PortalException ex) {
                    // do nothing
                    continue;
                }
            }
            if (id > -1) {
                roleIds.add(id);
            }
        }
        return roleIds.toArray(new Long[0]);
    }

    protected long fetchGroupId(final Map<String, Serializable> workflowContext, final UserGroupRolesUpdaterConfigurationWrapper configuration) throws PortalException {
        if (configuration.isInContextGroupIdRequired()) {
            return GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_GROUP_ID));
        }

        final String groupIdValue;
        if (configuration.isWorkflowContextKeyUsedForGroupId()) {
            final String workflowKey = configuration.getGroupIdWorkflowContextKey();

            groupIdValue = workflowContext.containsKey(workflowKey) ?
                    String.valueOf(workflowContext.get(workflowKey)) :
                    StringPool.BLANK;
        } else {
            groupIdValue = configuration.getGroupIdValue();
        }

        if (StringUtil.isBlank(groupIdValue)) {
            throw new PortalException("Unable to find group because the group id is blank for " + configuration.getIdentifier());
        }

        try {
            return Long.parseLong(groupIdValue);
        } catch (final NumberFormatException e) {
            throw new PortalException("Unable to obtain group id for " + configuration.getIdentifier(), e);
        }
    }

    @SuppressWarnings("unused")
    protected void beforeChange(final long companyId, final long groupId, final long userId, final long[] roleIds, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final UserGroupRolesUpdaterConfigurationWrapper configuration) {
    }

    @SuppressWarnings("unused")
    protected abstract long getGroupId(Map<String, Serializable> workflowContext, ServiceContext serviceContext, UserGroupRolesUpdaterConfigurationWrapper configuration) throws PortalException;

    @SuppressWarnings("unused")
    protected void afterChange(final long companyId, final long groupId, final long userId, final long[] roleIds, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final UserGroupRolesUpdaterConfigurationWrapper configuration) {
    }
}
