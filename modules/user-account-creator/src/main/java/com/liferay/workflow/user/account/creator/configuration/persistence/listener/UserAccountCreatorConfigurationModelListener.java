package com.liferay.workflow.user.account.creator.configuration.persistence.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import com.liferay.workflow.user.account.creator.configuration.UserAccountCreatorConfiguration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + UserAccountCreatorConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class UserAccountCreatorConfigurationModelListener extends BaseConfigurationModelListener<UserAccountCreatorConfiguration> {

    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }

    @Override
    protected Class<UserAccountCreatorConfiguration> getConfigurationClass() {
        return UserAccountCreatorConfiguration.class;
    }
}
