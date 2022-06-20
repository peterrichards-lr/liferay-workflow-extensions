package com.liferay.workflow.custom.field.updater.settings;

import com.liferay.workflow.custom.field.updater.configuration.CustomFieldUpdaterConfiguration;
import com.liferay.workflow.custom.field.updater.configuration.CustomFieldUpdaterConfigurationWrapper;
import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = CustomFieldUpdaterSettingsHelper.class)
public class CustomFieldUpdaterSettingsHelperImpl extends BaseSettingsHelper<CustomFieldUpdaterConfiguration, CustomFieldUpdaterConfigurationWrapper> implements CustomFieldUpdaterSettingsHelper {

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addCustomFieldUpdaterConfigurationWrapper(
            CustomFieldUpdaterConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a custom field updater configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    protected void removeCustomFieldUpdaterConfigurationWrapper(
            CustomFieldUpdaterConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a custom field updater configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
