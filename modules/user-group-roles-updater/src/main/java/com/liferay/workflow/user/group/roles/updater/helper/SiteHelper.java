package com.liferay.workflow.user.group.roles.updater.helper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupRoleService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.workflow.user.group.roles.updater.configuration.UserGroupRolesUpdaterConfigurationWrapper;
import com.liferay.workflow.user.group.roles.updater.constants.UserGroupRolesUpdaterConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.Map;

@Component(immediate = true, service = Helper.class)
public class SiteHelper extends BaseHelper {
    @Reference
    protected UserGroupRoleService _userGroupRoleService;
    @Reference
    private RoleLocalService _roleLocalService;
    @Reference
    private UserLocalService _userLocalService;

    @Override
    public Integer getEntityType() {
        return UserGroupRolesUpdaterConstants.SITE_HELPER;
    }

    @Override
    protected long getGroupId(final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final UserGroupRolesUpdaterConfigurationWrapper configuration) throws PortalException {
        final String groupIdLookupValueType = configuration.getGroupIdLookupValueType() != null
                ? configuration.getGroupIdLookupValueType().toLowerCase()
                : UserGroupRolesUpdaterConstants.CONFIG_GROUP_ID_LOOKUP_VALUE_TYPE_DEFAULT;
        final long groupId;
        switch (groupIdLookupValueType) {
            case UserGroupRolesUpdaterConstants.CONFIG_GROUP_ID_LOOKUP_VALUE_TYPE_ENTITY:
                _log.info("The group identifier already represents the site.");
            case UserGroupRolesUpdaterConstants.CONFIG_GROUP_ID_LOOKUP_VALUE_TYPE_GROUP_ID:
            default:
                _log.debug("Looking up group identifier directly");
                groupId = fetchGroupId(workflowContext, configuration);
                break;
        }
        _log.trace("Group id : {}", groupId);
        return groupId;
    }

    @Override
    protected RoleLocalService getRoleLocalService() {
        return _roleLocalService;
    }

    @Override
    protected UserGroupRoleService getUserGroupRoleService() {
        return _userGroupRoleService;
    }

    @Override
    protected UserLocalService getUserLocalService() {
        return _userLocalService;
    }
}
