package com.liferay.workflow.dynamic.data.mapping.form.options.translator.configuration.persistence.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.dynamic.data.mapping.form.options.translator.configuration.DDMFormOptionsTranslatorConfiguration;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + DDMFormOptionsTranslatorConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class DDMFormOptionsTranslatorConfigurationModelListener extends BaseConfigurationModelListener<DDMFormOptionsTranslatorConfiguration> {

    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }

    @Override
    protected Class<DDMFormOptionsTranslatorConfiguration> getConfigurationClass() {
        return DDMFormOptionsTranslatorConfiguration.class;
    }
}