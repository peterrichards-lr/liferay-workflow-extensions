package com.liferay.workflow.user.group.roles.updater.settings;

import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import com.liferay.workflow.user.group.roles.updater.configuration.UserGroupRolesUpdaterConfiguration;
import com.liferay.workflow.user.group.roles.updater.configuration.UserGroupRolesUpdaterConfigurationWrapper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = UserGroupRolesUpdaterSettingsHelper.class)
public class UserGroupRolesUpdaterSettingsHelperImpl extends BaseSettingsHelper<UserGroupRolesUpdaterConfiguration, UserGroupRolesUpdaterConfigurationWrapper> implements UserGroupRolesUpdaterSettingsHelper {

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addUserGroupRolesUpdaterConfigurationWrapper(
            final UserGroupRolesUpdaterConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a user groups roles updater configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    @SuppressWarnings("unused")
    protected void removeUserGroupRolesUpdaterConfigurationWrapper(
            final UserGroupRolesUpdaterConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a user groups roles updater configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
