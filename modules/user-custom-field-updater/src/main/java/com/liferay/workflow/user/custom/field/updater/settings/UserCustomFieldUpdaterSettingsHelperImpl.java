package com.liferay.workflow.user.custom.field.updater.settings;

import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import com.liferay.workflow.user.custom.field.updater.configuration.UserCustomFieldUpdaterConfiguration;
import com.liferay.workflow.user.custom.field.updater.configuration.UserCustomFieldUpdaterConfigurationWrapper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = UserCustomFieldUpdaterSettingsHelper.class)
public class UserCustomFieldUpdaterSettingsHelperImpl extends BaseSettingsHelper<UserCustomFieldUpdaterConfiguration, UserCustomFieldUpdaterConfigurationWrapper> implements UserCustomFieldUpdaterSettingsHelper {

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addUserCustomFieldUpdaterConfigurationWrapper(
            UserCustomFieldUpdaterConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a custom field updater configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    protected void removeUserCustomFieldUpdaterConfigurationWrapper(
            UserCustomFieldUpdaterConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a custom field updater configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
