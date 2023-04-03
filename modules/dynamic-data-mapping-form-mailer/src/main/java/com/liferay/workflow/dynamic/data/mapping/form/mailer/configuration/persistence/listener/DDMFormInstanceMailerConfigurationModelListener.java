package com.liferay.workflow.dynamic.data.mapping.form.mailer.configuration.persistence.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.dynamic.data.mapping.form.mailer.configuration.DDMFormInstanceMailerConfiguration;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + DDMFormInstanceMailerConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class DDMFormInstanceMailerConfigurationModelListener extends BaseConfigurationModelListener<DDMFormInstanceMailerConfiguration> {

    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    protected Class<DDMFormInstanceMailerConfiguration> getConfigurationClass() {
        return DDMFormInstanceMailerConfiguration.class;
    }

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }
}

