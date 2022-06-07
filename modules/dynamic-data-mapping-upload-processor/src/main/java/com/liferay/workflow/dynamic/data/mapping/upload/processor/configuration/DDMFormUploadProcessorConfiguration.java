package com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.workflow.extensions.common.configuration.BaseConfiguration;

@Meta.OCD(
        factory = true,
        id = DDMFormUploadProcessorConfiguration.PID,
        localization = "content/Language", name = "config-ddm-form-extractor-name",
        description = "config-ddm-form-extractor-description"
)
public interface DDMFormUploadProcessorConfiguration extends BaseConfiguration {
    String PID = "com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration.DDMFormUploadProcessorConfiguration";
}
