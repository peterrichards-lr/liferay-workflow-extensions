package com.liferay.workflow.organisation.creator.settings;

import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import com.liferay.workflow.organisation.creator.configuration.OrganisationCreatorConfiguration;
import com.liferay.workflow.organisation.creator.configuration.OrganisationCreatorConfigurationWrapper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = OrganisationCreatorSettingsHelper.class)
public class OrganisationCreatorSettingsHelperImpl extends BaseSettingsHelper<OrganisationCreatorConfiguration, OrganisationCreatorConfigurationWrapper> implements OrganisationCreatorSettingsHelper {

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addOrganisationCreatorConfigurationWrapper(
            final OrganisationCreatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a organisation creator configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    @SuppressWarnings("unused")
    protected void removeOrganisationCreatorConfigurationWrapper(
            final OrganisationCreatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a organisation creator configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
