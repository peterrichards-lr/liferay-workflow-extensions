package com.liferay.workflow.dynamic.data.mapping.form.options.translator.settings;

import com.liferay.workflow.dynamic.data.mapping.form.options.translator.configuration.DDMFormOptionsTranslatorConfiguration;
import com.liferay.workflow.dynamic.data.mapping.form.options.translator.configuration.DDMFormOptionsTranslatorConfigurationWrapper;
import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = DDMFormOptionsTranslatorSettingsHelper.class)
public class DDMFormOptionsTranslatorSettingsHelperImpl extends BaseSettingsHelper<DDMFormOptionsTranslatorConfiguration, DDMFormOptionsTranslatorConfigurationWrapper> implements DDMFormOptionsTranslatorSettingsHelper {

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addDDMFormOptionsTranslatorConfigurationWrapper(
            DDMFormOptionsTranslatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a options translator configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    protected void removeDDMFormOptionsTranslatorConfigurationWrapper(
            DDMFormOptionsTranslatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a options translator configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
