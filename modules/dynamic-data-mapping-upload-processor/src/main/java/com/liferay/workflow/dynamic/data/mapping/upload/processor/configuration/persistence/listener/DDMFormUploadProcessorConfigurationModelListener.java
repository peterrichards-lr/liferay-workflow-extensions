package com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration.persistence.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration.DDMFormUploadProcessorConfiguration;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + DDMFormUploadProcessorConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class DDMFormUploadProcessorConfigurationModelListener extends BaseConfigurationModelListener<DDMFormUploadProcessorConfiguration> {

    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    protected Class<DDMFormUploadProcessorConfiguration> getConfigurationClass() {
        return DDMFormUploadProcessorConfiguration.class;
    }

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }
}
