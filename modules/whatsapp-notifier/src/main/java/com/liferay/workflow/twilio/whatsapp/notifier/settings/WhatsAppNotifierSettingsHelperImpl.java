package com.liferay.workflow.twilio.whatsapp.notifier.settings;

import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import com.liferay.workflow.twilio.whatsapp.notifier.configuration.WhatsAppNotifierConfiguration;
import com.liferay.workflow.twilio.whatsapp.notifier.configuration.WhatsAppNotifierConfigurationWrapper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = WhatsAppNotifierSettingsHelper.class)
public class WhatsAppNotifierSettingsHelperImpl extends BaseSettingsHelper<WhatsAppNotifierConfiguration, WhatsAppNotifierConfigurationWrapper> implements WhatsAppNotifierSettingsHelper {
    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addWhatsAppNotifierConfigurationWrapper(
            final WhatsAppNotifierConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a WhatsApp notifier configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    @SuppressWarnings("unused")
    protected void removeWhatsAppNotifierConfigurationWrapper(
            final WhatsAppNotifierConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a WhatsApp notifier configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
