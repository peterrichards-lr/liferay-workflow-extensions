package com.liferay.workflow.dynamic.data.mapping.upload.processor.settings;

import com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration.DDMFormUploadProcessorConfiguration;
import com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration.DDMFormUploadProcessorConfigurationWrapper;
import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = DDMFormUploadProcessorSettingsHelper.class)
public class DDMFormUploadProcessorSettingsHelperImpl extends BaseSettingsHelper<DDMFormUploadProcessorConfiguration, DDMFormUploadProcessorConfigurationWrapper> implements DDMFormUploadProcessorSettingsHelper {
    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addDDMFormUploadProcessorConfigurationWrapper(
            DDMFormUploadProcessorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a form extractor configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    protected void removeDDMFormUploadProcessorConfigurationWrapper(
            DDMFormUploadProcessorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a form extractor configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
