package com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration.persistence.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration.DDMFormInstanceRecordExtractorConfiguration;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + DDMFormInstanceRecordExtractorConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class DDMFormInstanceRecordExtractorConfigurationModelListener extends BaseConfigurationModelListener<DDMFormInstanceRecordExtractorConfiguration> {

    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    protected Class<DDMFormInstanceRecordExtractorConfiguration> getConfigurationClass() {
        return DDMFormInstanceRecordExtractorConfiguration.class;
    }

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }
}

