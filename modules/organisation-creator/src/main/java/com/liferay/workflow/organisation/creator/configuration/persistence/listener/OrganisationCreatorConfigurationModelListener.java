package com.liferay.workflow.organisation.creator.configuration.persistence.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import com.liferay.workflow.organisation.creator.configuration.OrganisationCreatorConfiguration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + OrganisationCreatorConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class OrganisationCreatorConfigurationModelListener extends BaseConfigurationModelListener<OrganisationCreatorConfiguration> {
    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    protected Class<OrganisationCreatorConfiguration> getConfigurationClass() {
        return OrganisationCreatorConfiguration.class;
    }

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }
}
