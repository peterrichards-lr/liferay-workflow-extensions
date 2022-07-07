package com.liferay.workflow.user.group.updater.settings;

import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import com.liferay.workflow.user.group.updater.configuration.UserGroupUpdaterConfiguration;
import com.liferay.workflow.user.group.updater.configuration.UserGroupUpdaterConfigurationWrapper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = UserGroupUpdaterSettingsHelper.class)
public class UserGroupUpdaterSettingsHelperImpl extends BaseSettingsHelper<UserGroupUpdaterConfiguration, UserGroupUpdaterConfigurationWrapper> implements UserGroupUpdaterSettingsHelper {
    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addUserGroupUpdaterConfiguration(
            final UserGroupUpdaterConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a user group updater configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    @SuppressWarnings("unused")
    protected void removeUserGroupUpdaterConfiguration(
            final UserGroupUpdaterConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a user group updater configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
