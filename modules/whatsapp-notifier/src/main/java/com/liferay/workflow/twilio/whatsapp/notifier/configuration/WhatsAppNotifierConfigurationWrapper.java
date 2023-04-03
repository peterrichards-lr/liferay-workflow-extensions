package com.liferay.workflow.twilio.whatsapp.notifier.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfigurationWrapper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = WhatsAppNotifierConfiguration.PID,
        immediate = true, service = WhatsAppNotifierConfigurationWrapper.class
)
public class WhatsAppNotifierConfigurationWrapper extends BaseActionExecutorConfigurationWrapper<WhatsAppNotifierConfiguration> {

    @Activate
    @Modified
    protected void activate(final Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final WhatsAppNotifierConfiguration configuration = ConfigurableUtil.createConfigurable(
                WhatsAppNotifierConfiguration.class, properties);
        super.setConfiguration(configuration);
    }

    public String getMessageTemplate() {
        return getConfiguration().messageTemplate();
    }

    public String getRecipientNumber() {
        return getConfiguration().recipientNumber();
    }

    public String getRecipientNumberWorkflowKey() {
        return getConfiguration().recipientNumberWorkflowContextKey();
    }

    public String getSenderNumber() {
        return getConfiguration().senderNumber();
    }

    public String getSenderNumberWorkflowKey() {
        return getConfiguration().senderNumberWorkflowContextKey();
    }

    public Boolean isWorkflowContextKeyUsedForRecipientNumber() {
        return getConfiguration().useWorkflowContextKeyForRecipientNumber();
    }

    public Boolean isWorkflowContextKeyUsedForSenderNumber() {
        return getConfiguration().useWorkflowContextKeyForSenderNumber();
    }
}
