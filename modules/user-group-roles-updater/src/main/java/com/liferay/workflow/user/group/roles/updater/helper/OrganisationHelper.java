package com.liferay.workflow.user.group.roles.updater.helper;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.service.*;
import com.liferay.workflow.extensions.common.util.StringUtil;
import com.liferay.workflow.user.group.roles.updater.configuration.UserGroupRolesUpdaterConfigurationWrapper;
import com.liferay.workflow.user.group.roles.updater.constants.UserGroupRolesUpdaterConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.Map;

@Component(immediate = true, service = Helper.class)
public class OrganisationHelper extends BaseHelper {

    @Reference
    protected UserGroupRoleService _userGroupRoleService;
    @Reference
    private OrganizationLocalService _organizationLocalService;
    @Reference
    private RoleLocalService _roleLocalService;
    @Reference
    private UserLocalService _userLocalService;

    @Override
    public Integer getEntityType() {
        return UserGroupRolesUpdaterConstants.ORGANISATION_HELPER;
    }

    @Override
    protected long getGroupId(final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final UserGroupRolesUpdaterConfigurationWrapper configuration) throws PortalException {
        final String groupIdLookupValueType = configuration.getGroupIdLookupValueType() != null
                ? configuration.getGroupIdLookupValueType().toLowerCase()
                : UserGroupRolesUpdaterConstants.CONFIG_GROUP_ID_LOOKUP_VALUE_TYPE_DEFAULT;
        final long groupId;
        switch (groupIdLookupValueType) {
            case UserGroupRolesUpdaterConstants.CONFIG_GROUP_ID_LOOKUP_VALUE_TYPE_GROUP_ID:
                _log.debug("Looking up group identifier directly");
                groupId = fetchGroupId(workflowContext, configuration);
                break;
            case UserGroupRolesUpdaterConstants.CONFIG_GROUP_ID_LOOKUP_VALUE_TYPE_ENTITY:
            default:
                _log.debug("Looking up group identifier via entity");
                groupId = lookupOrganisationGroupId(workflowContext, configuration);
                break;
        }
        _log.trace("Group id : {}", groupId);
        return groupId;
    }

    private long lookupOrganisationGroupId(final Map<String, Serializable> workflowContext, final UserGroupRolesUpdaterConfigurationWrapper configuration) throws PortalException {
        final Organization organisation = getOrganisation(workflowContext, configuration);
        if (organisation != null) {
            return organisation.getGroupId();
        }
        throw new PortalException("Unable to obtain group id. The organisation was not found fpr " + configuration.getIdentifier());
    }

    private Organization getOrganisation(final Map<String, Serializable> workflowContext, final UserGroupRolesUpdaterConfigurationWrapper configuration) throws PortalException {
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
            throw new PortalException("Unable to find group because the organisation id is blank for " + configuration.getIdentifier());
        }
        final long organisationId;
        try {
            organisationId = Long.parseLong(groupIdValue);
        } catch (final NumberFormatException e) {
            throw new PortalException("Unable to obtain group id. The organisation identifier is not numeric for " + configuration.getIdentifier(), e);
        }
        return _organizationLocalService.fetchOrganization(organisationId);
    }

    @Override
    protected void beforeChange(final long companyId, final long groupId, final long userId, final long[] roleIds, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final UserGroupRolesUpdaterConfigurationWrapper configuration) {
        final String groupIdLookupValueType = configuration.getGroupIdLookupValueType() != null
                ? configuration.getGroupIdLookupValueType().toLowerCase()
                : UserGroupRolesUpdaterConstants.CONFIG_GROUP_ID_LOOKUP_VALUE_TYPE_DEFAULT;
        if (!UserGroupRolesUpdaterConstants.CONFIG_GROUP_ID_LOOKUP_VALUE_TYPE_ENTITY.equals(groupIdLookupValueType)) {
            return;
        }
        try {
            final Organization organisation = getOrganisation(workflowContext, configuration);
            _organizationLocalService.addUserOrganization(userId, organisation);
            _log.debug("Added user to organisation");
        } catch (final PortalException e) {
            _log.warn("Unable to add user to the organisation", e);
        }
    }

    @Override
    protected UserGroupRoleService getUserGroupRoleService() {
        return _userGroupRoleService;
    }

    @Override
    protected UserLocalService getUserLocalService() {
        return _userLocalService;
    }

    @Override
    protected RoleLocalService getRoleLocalService() {
        return _roleLocalService;
    }
}
