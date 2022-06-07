package com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.workflow.extensions.common.configuration.BaseConfiguration;
import com.liferay.workflow.extensions.common.configuration.constants.WorkflowExtensionsConstants;

@ExtendedObjectClassDefinition(
        category = "workflow", scope = ExtendedObjectClassDefinition.Scope.GROUP,
        factoryInstanceLabelAttribute = WorkflowExtensionsConstants.CONFIG_FORM_INSTANCE_ID
)
@Meta.OCD(
        factory = true,
        id = DDMFormUploadProcessorConfiguration.PID,
        localization = "content/Language", name = "config-ddm-form-upload-processor-name",
        description = "config-ddm-form-upload-processor-description"
)
public interface DDMFormUploadProcessorConfiguration extends BaseConfiguration {
    String PID = "com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration.DDMFormUploadProcessorConfiguration";
}
