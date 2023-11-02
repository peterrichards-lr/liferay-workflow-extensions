package com.liferay.workflow.resource.permissions.updater.settings;

import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import com.liferay.workflow.resource.permissions.updater.configuration.ResourcePermissionsUpdaterConfiguration;
import com.liferay.workflow.resource.permissions.updater.configuration.ResourcePermissionsUpdaterConfigurationWrapper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = ResourcePermissionsUpdaterSettingsHelper.class)
public class ResourcePermissionsUpdaterSettingsHelperImpl extends BaseSettingsHelper<ResourcePermissionsUpdaterConfiguration, ResourcePermissionsUpdaterConfigurationWrapper> implements ResourcePermissionsUpdaterSettingsHelper {
    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addResourcePermissionsUpdaterConfiguration(
            final ResourcePermissionsUpdaterConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a resource permissions updater configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    @SuppressWarnings("unused")
    protected void removeResourcePermissionsUpdaterConfiguration(
            final ResourcePermissionsUpdaterConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a resource permissions updater configuration\n[{}]", configurationWrapper.toString());

    }
}
