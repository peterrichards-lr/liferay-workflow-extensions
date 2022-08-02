package com.liferay.workflow.commerce.orders.remapper.configuration.persistence.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.commerce.orders.remapper.configuration.CommerceOrdersRemapperConfiguration;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + CommerceOrdersRemapperConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class CommerceOrdersRemapperConfigurationModelListener extends BaseConfigurationModelListener<CommerceOrdersRemapperConfiguration> {
    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Class getConfigurationClass() {
        return CommerceOrdersRemapperConfiguration.class;
    }
}
