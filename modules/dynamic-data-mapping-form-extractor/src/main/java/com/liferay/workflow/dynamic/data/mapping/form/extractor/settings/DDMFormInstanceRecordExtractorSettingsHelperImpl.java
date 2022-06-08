package com.liferay.workflow.dynamic.data.mapping.form.extractor.settings;

import com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration.DDMFormInstanceRecordExtractorConfigurationWrapper;
import com.liferay.workflow.dynamic.data.mapping.form.extractor.constants.DDMFormInstanceRecordExtractorConstants;
import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import org.osgi.service.component.annotations.*;

import java.util.Collections;
import java.util.Map;

@Component(immediate = true, service = DDMFormInstanceRecordExtractorSettingsHelper.class)
public class DDMFormInstanceRecordExtractorSettingsHelperImpl extends BaseSettingsHelper<DDMFormInstanceRecordExtractorConfigurationWrapper> implements DDMFormInstanceRecordExtractorSettingsHelper {

    @Override
    public String[] getDDMFieldReferenceArray(long formInstanceId) {
        DDMFormInstanceRecordExtractorConfigurationWrapper
                extractorConfigurationWrapper =
                getConfigurationWrapper(formInstanceId);

        if (extractorConfigurationWrapper == null) {
            return new String[0];
        }

        return extractorConfigurationWrapper.getDDMFieldReferenceArray();
    }

    @Override
    public Map<String, String> getDDMUserDataFieldMap(long formInstanceId) {
        DDMFormInstanceRecordExtractorConfigurationWrapper
                extractorConfigurationWrapper =
                getConfigurationWrapper(formInstanceId);

        if (extractorConfigurationWrapper == null) {
            return Collections.emptyMap();
        }

        return extractorConfigurationWrapper.getDDMUserDataFieldMap();
    }

    @Override
    public boolean isExtractUploadsRequired(long formInstanceId) {
        DDMFormInstanceRecordExtractorConfigurationWrapper
                extractorConfigurationWrapper =
                getConfigurationWrapper(formInstanceId);

        if (extractorConfigurationWrapper == null) {
            return Boolean.parseBoolean(DDMFormInstanceRecordExtractorConstants.CONFIG_EXTRACT_UPLOADS_DEFAULT);
        }

        return extractorConfigurationWrapper.isExtractUploadsRequired();
    }

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addDDMFormInstanceRecordExtractorConfigurationWrapper(
            DDMFormInstanceRecordExtractorConfigurationWrapper
                    configurationWrapper) {
        _log.info(configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    protected void removeDDMFormInstanceRecordExtractorConfigurationWrapper(
            DDMFormInstanceRecordExtractorConfigurationWrapper
                    configurationWrapper) {
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
