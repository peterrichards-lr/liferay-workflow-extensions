package com.liferay.workflow.twilio.whatsapp.notifier.configuration.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import com.liferay.workflow.twilio.whatsapp.notifier.configuration.WhatsAppNotifierConfiguration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + WhatsAppNotifierConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class WhatsAppNotifierConfigurationModelListener extends BaseConfigurationModelListener<WhatsAppNotifierConfiguration> {

    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }

    @Override
    protected Class<WhatsAppNotifierConfiguration> getConfigurationClass() {
        return WhatsAppNotifierConfiguration.class;
    }
}

