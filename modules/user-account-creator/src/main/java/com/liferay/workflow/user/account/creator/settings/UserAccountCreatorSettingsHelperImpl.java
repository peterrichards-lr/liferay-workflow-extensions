package com.liferay.workflow.user.account.creator.settings;

import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import com.liferay.workflow.user.account.creator.configuration.UserAccountCreatorConfiguration;
import com.liferay.workflow.user.account.creator.configuration.UserAccountCreatorConfigurationWrapper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = UserAccountCreatorSettingsHelper.class)
public class UserAccountCreatorSettingsHelperImpl extends BaseSettingsHelper<UserAccountCreatorConfiguration, UserAccountCreatorConfigurationWrapper> implements UserAccountCreatorSettingsHelper {

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addUserAccountCreatorConfigurationWrapper(
            final UserAccountCreatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a custom field updater configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    @SuppressWarnings("unused")
    protected void removeUserAccountCreatorConfigurationWrapper(
            final UserAccountCreatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a custom field updater configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
