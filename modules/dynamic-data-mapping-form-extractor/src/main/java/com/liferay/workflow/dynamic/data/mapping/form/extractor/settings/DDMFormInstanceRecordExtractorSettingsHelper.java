package com.liferay.workflow.dynamic.data.mapping.form.extractor.settings;

import com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration.DDMFormInstanceRecordExtractorConfiguration;
import com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration.DDMFormInstanceRecordExtractorConfigurationWrapper;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;

import java.util.Map;

public interface DDMFormInstanceRecordExtractorSettingsHelper extends SettingsHelper<DDMFormInstanceRecordExtractorConfiguration, DDMFormInstanceRecordExtractorConfigurationWrapper> {
    String[] getDDMFieldReferenceArray(String formInstanceId);

    Map<String, String> getDDMUserDataFieldMap(String formInstanceId);

    boolean isExtractUploadsRequired(String formInstanceId);

    boolean isWorkflowInformationRequired(String formInstanceId);
}
