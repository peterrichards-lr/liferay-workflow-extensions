package com.liferay.workflow.dynamic.data.mapping.form.extractor.settings;

import com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration.DDMFormInstanceRecordExtractorConfigurationWrapper;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;

import java.util.Map;

public interface DDMFormInstanceRecordExtractorSettingsHelper extends SettingsHelper<DDMFormInstanceRecordExtractorConfigurationWrapper> {
    String[] getDDMFieldReferenceArray(long formInstanceId);

    Map<String, String> getDDMUserDataFieldMap(long formInstanceId);

    boolean isExtractUploadsRequired(long formInstanceId);
}
