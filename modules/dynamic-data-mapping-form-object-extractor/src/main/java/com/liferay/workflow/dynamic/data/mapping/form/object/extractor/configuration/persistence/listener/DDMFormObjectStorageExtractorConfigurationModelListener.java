package com.liferay.workflow.dynamic.data.mapping.form.object.extractor.configuration.persistence.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.dynamic.data.mapping.form.object.extractor.configuration.DDMFormObjectStorageExtractorConfiguration;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + DDMFormObjectStorageExtractorConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class DDMFormObjectStorageExtractorConfigurationModelListener extends BaseConfigurationModelListener<DDMFormObjectStorageExtractorConfiguration> {

    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    protected Class<DDMFormObjectStorageExtractorConfiguration> getConfigurationClass() {
        return DDMFormObjectStorageExtractorConfiguration.class;
    }

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }
}