package com.liferay.workflow.user.group.roles.updater.helper;

import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.account.service.AccountEntryUserRelLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupRoleService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.workflow.user.group.roles.updater.configuration.UserGroupRolesUpdaterConfigurationWrapper;
import com.liferay.workflow.user.group.roles.updater.constants.UserGroupRolesUpdaterConstants;
import org.jsoup.helper.StringUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.Map;

@Component(immediate = true, service = Helper.class)
public class AccountEntryHelper extends BaseHelper {


    @Reference
    protected UserGroupRoleService _userGroupRoleService;
    @Reference
    private UserLocalService _userLocalService;
    @Reference
    private RoleLocalService _roleLocalService;
    @Reference
    private AccountEntryLocalService _accountEntryLocalService;

    @Reference
    private AccountEntryUserRelLocalService _accountEntryUserRelLocalService;

    @Override
    public Integer getEntityType() {
        return UserGroupRolesUpdaterConstants.ACCOUNT_HELPER;
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
                groupId = lookupAccountEntryGroupId(workflowContext, configuration);
                break;
        }

        _log.trace("Group id : {}", groupId);
        return groupId;
    }

    private long lookupAccountEntryGroupId(final Map<String, Serializable> workflowContext, final UserGroupRolesUpdaterConfigurationWrapper configuration) throws PortalException {
        final AccountEntry accountEntry = getAccountEntry(workflowContext, configuration);
        if (accountEntry != null) {
            return accountEntry.getAccountEntryGroupId();
        }
        throw new PortalException("Unable to obtain group id. The account entry was not found fpr " + configuration.getIdentifier());
    }

    private AccountEntry getAccountEntry(final Map<String, Serializable> workflowContext, final UserGroupRolesUpdaterConfigurationWrapper configuration) throws PortalException {
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
            throw new PortalException("Unable to find group because the account entry id is blank for " + configuration.getIdentifier());
        }

        final long accountEntryId;
        try {
            accountEntryId = Long.parseLong(groupIdValue);
        } catch (final NumberFormatException e) {
            throw new PortalException("Unable to obtain group id. The account entry identifier is not numeric for " + configuration.getIdentifier(), e);
        }
        return _accountEntryLocalService.fetchAccountEntry(accountEntryId);
    }

    @Override
    protected void beforeChange(final long companyId, final long groupId, final long userId, final long[] roleIds, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final UserGroupRolesUpdaterConfigurationWrapper configuration) {
        try {
            final AccountEntry accountEntry = getAccountEntry(workflowContext, configuration);
            _accountEntryUserRelLocalService.addAccountEntryUserRel(accountEntry.getAccountEntryId(), userId);
        } catch (final PortalException e) {
            _log.warn("Unable to add user to the account entry", e);
        }
    }
}
