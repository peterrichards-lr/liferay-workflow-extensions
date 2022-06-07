package com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.workflow.dynamic.data.mapping.form.extractor.constants.DDMFormInstanceRecordExtractorConstants;
import com.liferay.workflow.extensions.common.configuration.BaseConfiguration;

@Meta.OCD(
        factory = true,
        id = DDMFormInstanceRecordExtractorConfiguration.PID,
        localization = "content/Language", name = "config-ddm-form-extractor-name",
        description = "config-ddm-form-extractor-description"
)
public interface DDMFormInstanceRecordExtractorConfiguration extends BaseConfiguration {
    String PID = "com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration.DDMFormInstanceRecordExtractorConfiguration";

    @Meta.AD(
            deflt = DDMFormInstanceRecordExtractorConstants.CONFIG_FIELD_REFERENCE_ARRAY_DEFAULT,
            description = "config-ddm-form-field-reference-array-description",
            name = "config-ddm-form-field-reference-array-name",
            required = false
    )
    String[] ddmFieldReferenceArray();

    @Meta.AD(
            deflt = DDMFormInstanceRecordExtractorConstants.CONFIG_USER_DATA_FIELD_MAP_DEFAULT,
            description = "config-ddm-form-user-data-field-map-description",
            name = "config-ddm-form-user-data-field-map-name",
            required = false
    )
    String ddmUserDataFieldMap();

    @Meta.AD(
            deflt = DDMFormInstanceRecordExtractorConstants.CONFIG_EXTRACT_UPLOADS_DEFAULT,
            description = "config-extract-uploads-description",
            name = "config-extract-uploads-name",
            required = false
    )
    boolean extractUploads();
}
