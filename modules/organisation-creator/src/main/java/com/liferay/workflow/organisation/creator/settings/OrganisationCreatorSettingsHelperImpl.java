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
            OrganisationCreatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a custom field updater configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    protected void removeOrganisationCreatorConfigurationWrapper(
            OrganisationCreatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a custom field updater configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
