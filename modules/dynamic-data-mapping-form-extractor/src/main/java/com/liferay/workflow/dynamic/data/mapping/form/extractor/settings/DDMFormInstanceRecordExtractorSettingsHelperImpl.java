package com.liferay.workflow.dynamic.data.mapping.form.extractor.settings;

import com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration.DDMFormInstanceRecordExtractorConfiguration;
import com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration.DDMFormInstanceRecordExtractorConfigurationWrapper;
import com.liferay.workflow.dynamic.data.mapping.form.extractor.constants.DDMFormInstanceRecordExtractorConstants;
import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import org.osgi.service.component.annotations.*;

import java.util.Collections;
import java.util.Map;

@Component(immediate = true, service = DDMFormInstanceRecordExtractorSettingsHelper.class)
public class DDMFormInstanceRecordExtractorSettingsHelperImpl extends BaseSettingsHelper<DDMFormInstanceRecordExtractorConfiguration, DDMFormInstanceRecordExtractorConfigurationWrapper> implements DDMFormInstanceRecordExtractorSettingsHelper {

    @Override
    public String[] getDDMFieldReferenceArray(String formInstanceId) {
        DDMFormInstanceRecordExtractorConfigurationWrapper
                extractorConfigurationWrapper =
                getConfigurationWrapper(formInstanceId);

        if (extractorConfigurationWrapper == null) {
            return new String[0];
        }

        return extractorConfigurationWrapper.getDDMFieldReferenceArray();
    }

    @Override
    public Map<String, String> getDDMUserDataFieldMap(String formInstanceId) {
        DDMFormInstanceRecordExtractorConfigurationWrapper
                extractorConfigurationWrapper =
                getConfigurationWrapper(formInstanceId);

        if (extractorConfigurationWrapper == null) {
            return Collections.emptyMap();
        }

        return extractorConfigurationWrapper.getDDMUserDataFieldMap();
    }

    @Override
    public boolean isExtractUploadsRequired(String formInstanceId) {
        DDMFormInstanceRecordExtractorConfigurationWrapper
                extractorConfigurationWrapper =
                getConfigurationWrapper(formInstanceId);

        if (extractorConfigurationWrapper == null) {
            return Boolean.parseBoolean(DDMFormInstanceRecordExtractorConstants.CONFIG_EXTRACT_UPLOADS_DEFAULT);
        }

        return extractorConfigurationWrapper.isExtractUploadsRequired();
    }

    @Override
    public boolean isWorkflowInformationRequired(String formInstanceId) {
        DDMFormInstanceRecordExtractorConfigurationWrapper
                extractorConfigurationWrapper =
                getConfigurationWrapper(formInstanceId);

        if (extractorConfigurationWrapper == null) {
            return Boolean.parseBoolean(DDMFormInstanceRecordExtractorConstants.CONFIG_INCLUDE_WORKFLOW_INFORMATION_DEFAULT);
        }

        return extractorConfigurationWrapper.isWorkflowInformationRequired();
    }

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addDDMFormInstanceRecordExtractorConfigurationWrapper(
            DDMFormInstanceRecordExtractorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a form extractor configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    protected void removeDDMFormInstanceRecordExtractorConfigurationWrapper(
            DDMFormInstanceRecordExtractorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a form extractor configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
