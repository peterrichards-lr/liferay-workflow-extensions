package com.liferay.workflow.custom.field.updater.configuration.persistence.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.custom.field.updater.configuration.CustomFieldUpdaterConfiguration;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + CustomFieldUpdaterConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class CustomFieldUpdaterConfigurationModelListener extends BaseConfigurationModelListener<CustomFieldUpdaterConfiguration> {
    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }

    @Override
    protected Class getConfigurationClass() {
        return CustomFieldUpdaterConfiguration.class;
    }
}