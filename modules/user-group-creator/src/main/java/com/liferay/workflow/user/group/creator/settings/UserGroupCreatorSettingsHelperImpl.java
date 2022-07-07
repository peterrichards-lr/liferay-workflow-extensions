package com.liferay.workflow.user.group.creator.settings;

import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import com.liferay.workflow.user.group.creator.configuration.UserGroupCreatorConfiguration;
import com.liferay.workflow.user.group.creator.configuration.UserGroupCreatorConfigurationWrapper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = UserGroupCreatorSettingsHelper.class)
public class UserGroupCreatorSettingsHelperImpl extends BaseSettingsHelper<UserGroupCreatorConfiguration, UserGroupCreatorConfigurationWrapper> implements UserGroupCreatorSettingsHelper {
    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addUserGroupCreatorConfigurationWrapper(
            final UserGroupCreatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding an user group creator configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    @SuppressWarnings("unused")
    protected void removeUserGroupCreatorConfigurationWrapper(
            final UserGroupCreatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing an user group creator configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
