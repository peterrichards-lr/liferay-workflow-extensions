package com.liferay.workflow.dynamic.data.mapping.form.mailer.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseFormActionExecutorConfigurationWrapper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = DDMFormInstanceMailerConfiguration.PID,
        immediate = true, service = DDMFormInstanceMailerConfigurationWrapper.class
)
public class DDMFormInstanceMailerConfigurationWrapper extends BaseFormActionExecutorConfigurationWrapper<DDMFormInstanceMailerConfiguration> {

    public boolean isWorkflowKeyUsedForSenderEmailAddress() {
        return getConfiguration().useWorkflowContextKeyForFromEmailAddress();
    }

    public String getSenderEmailAddressWorkflowContextKey() {
        return getConfiguration().fromEmailAddressWorkflowContextKey();
    }

    public String getSenderEmailAddress() {
        return getConfiguration().fromEmailAddress();
    }

    public String getRecipientEmailAddressWorkflowContextKey() {
        return getConfiguration().toEmailAddressWorkflowContextKey();
    }

    public String getEmailSubjectTemplate() {
        return getConfiguration().emailSubjectTemplate();
    }

    public String getEmailBodyTemplate() {
        return getConfiguration().emailBodyTemplate();
    }

    @Activate
    @Modified
    protected void activate(Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final DDMFormInstanceMailerConfiguration configuration = ConfigurableUtil.createConfigurable(
                DDMFormInstanceMailerConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
