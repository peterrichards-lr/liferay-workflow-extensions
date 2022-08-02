package com.liferay.workflow.commerce.orders.remapper.settings;

import com.liferay.workflow.commerce.orders.remapper.configuration.CommerceOrdersRemapperConfiguration;
import com.liferay.workflow.commerce.orders.remapper.configuration.CommerceOrdersRemapperConfigurationWrapper;
import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = CommerceOrdersRemapperSettingsHelper.class)
public class CommerceOrdersRemapperSettingsHelperImpl extends BaseSettingsHelper<CommerceOrdersRemapperConfiguration, CommerceOrdersRemapperConfigurationWrapper> implements CommerceOrdersRemapperSettingsHelper {
    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addCommerceOrdersRemapperConfigurationWrapper(
            final CommerceOrdersRemapperConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a commerce order remapper configuration\n" +
                "[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    @SuppressWarnings("unused")
    protected void removeCommerceOrdersRemapperConfigurationWrapper(
            final CommerceOrdersRemapperConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a commerce order remapper configuration\n" +
                "[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
