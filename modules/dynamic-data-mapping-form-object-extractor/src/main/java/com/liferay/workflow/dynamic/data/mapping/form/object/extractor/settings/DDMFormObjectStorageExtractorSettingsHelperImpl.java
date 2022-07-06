package com.liferay.workflow.dynamic.data.mapping.form.object.extractor.settings;

import com.liferay.workflow.dynamic.data.mapping.form.object.extractor.configuration.DDMFormObjectStorageExtractorConfiguration;
import com.liferay.workflow.dynamic.data.mapping.form.object.extractor.configuration.DDMFormObjectStorageExtractorConfigurationWrapper;
import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = DDMFormObjectStorageExtractorSettingsHelper.class)
public class DDMFormObjectStorageExtractorSettingsHelperImpl extends BaseSettingsHelper<DDMFormObjectStorageExtractorConfiguration, DDMFormObjectStorageExtractorConfigurationWrapper> implements DDMFormObjectStorageExtractorSettingsHelper {
    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addDDMFormObjectStorageExtractorConfigurationWrapper(
            final DDMFormObjectStorageExtractorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a form extractor configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    @SuppressWarnings("unused")
    protected void removeDDMFormObjectStorageExtractorConfigurationWrapper(
            final DDMFormObjectStorageExtractorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a form extractor configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
