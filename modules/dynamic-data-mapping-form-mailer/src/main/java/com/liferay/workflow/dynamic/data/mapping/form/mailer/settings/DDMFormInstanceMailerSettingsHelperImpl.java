package com.liferay.workflow.dynamic.data.mapping.form.mailer.settings;

import com.liferay.workflow.dynamic.data.mapping.form.mailer.configuration.DDMFormInstanceMailerConfiguration;
import com.liferay.workflow.dynamic.data.mapping.form.mailer.configuration.DDMFormInstanceMailerConfigurationWrapper;
import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = DDMFormInstanceMailerSettingsHelper.class)
public class DDMFormInstanceMailerSettingsHelperImpl extends BaseSettingsHelper<DDMFormInstanceMailerConfiguration, DDMFormInstanceMailerConfigurationWrapper> implements DDMFormInstanceMailerSettingsHelper {

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addDDMFormInstanceMailerConfigurationWrapper(
            final DDMFormInstanceMailerConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a form extractor configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    @SuppressWarnings("unused")
    protected void removeDDMFormInstanceMailerConfigurationWrapper(
            final DDMFormInstanceMailerConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a form extractor configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
