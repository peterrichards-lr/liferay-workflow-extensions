package com.liferay.workflow.user.role.creator.settings;

import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import com.liferay.workflow.user.role.creator.configuration.UserRoleCreatorConfiguration;
import com.liferay.workflow.user.role.creator.configuration.UserRoleCreatorConfigurationWrapper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = UserRoleCreatorSettingsHelper.class)
public class UserRoleCreatorSettingsHelperImpl extends BaseSettingsHelper<UserRoleCreatorConfiguration, UserRoleCreatorConfigurationWrapper> implements UserRoleCreatorSettingsHelper {
    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addUserRoleCreatorConfigurationWrapper(
            final UserRoleCreatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a user role creator configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    @SuppressWarnings("unused")
    protected void removeUserRoleCreatorConfigurationWrapper(
            final UserRoleCreatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a user role creator configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
