package com.liferay.workflow.dynamic.data.mapping.form.mailer.configuration;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfigurationWrapper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = DDMFormInstanceMailerConfiguration.PID,
        immediate = true, service = DDMFormInstanceMailerConfigurationWrapper.class
)
public class DDMFormInstanceMailerConfigurationWrapper extends BaseActionExecutorConfigurationWrapper<DDMFormInstanceMailerConfiguration> {

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

    @Override
    public String toString() {
        return "BaseActionExecutorConfigurationWrapper{" +
                "super=" + super.toString() +
                StringPool.COMMA +
                "useWorkflowContextKeyForFromEmailAddress=" + getConfiguration().useWorkflowContextKeyForFromEmailAddress() +
                StringPool.COMMA +
                "fromEmailAddressWorkflowContextKey=" + StringPool.APOSTROPHE + getConfiguration().fromEmailAddressWorkflowContextKey() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "fromEmailAddress=" + StringPool.APOSTROPHE + getConfiguration().fromEmailAddress() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "toEmailAddressWorkflowContextKey=" + StringPool.APOSTROPHE + getConfiguration().toEmailAddressWorkflowContextKey() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "emailSubjectTemplate=" + StringPool.APOSTROPHE + getConfiguration().emailSubjectTemplate() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "emailBodyTemplate=" + StringPool.APOSTROPHE + getConfiguration().emailBodyTemplate() + StringPool.APOSTROPHE +
                '}';
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
