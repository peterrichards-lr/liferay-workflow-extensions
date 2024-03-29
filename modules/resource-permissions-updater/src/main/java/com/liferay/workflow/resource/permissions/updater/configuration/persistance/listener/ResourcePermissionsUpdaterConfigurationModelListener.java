package com.liferay.workflow.resource.permissions.updater.configuration.persistance.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import com.liferay.workflow.resource.permissions.updater.configuration.ResourcePermissionsUpdaterConfiguration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + ResourcePermissionsUpdaterConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class ResourcePermissionsUpdaterConfigurationModelListener extends BaseConfigurationModelListener<ResourcePermissionsUpdaterConfiguration> {
    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Class getConfigurationClass() {
        return ResourcePermissionsUpdaterConfiguration.class;
    }
}
