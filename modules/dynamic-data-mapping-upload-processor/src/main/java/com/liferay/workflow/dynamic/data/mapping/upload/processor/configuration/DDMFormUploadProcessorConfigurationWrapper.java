package com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration;

import com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper;
import org.osgi.service.component.annotations.Component;

@Component(
        configurationPid = DDMFormUploadProcessorConfiguration.PID,
        immediate = true, service = DDMFormUploadProcessorConfigurationWrapper.class
)
public class DDMFormUploadProcessorConfigurationWrapper extends BaseConfigurationWrapper<DDMFormUploadProcessorConfiguration> {
    @Override
    protected String toStringSubClass() {
        return null;
    }
}
