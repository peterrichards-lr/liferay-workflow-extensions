package com.liferay.workflow.user.group.updater.configuration.persistence.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import com.liferay.workflow.user.group.updater.configuration.UserGroupUpdaterConfiguration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + UserGroupUpdaterConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class UserGroupUpdaterConfigurationModelListener extends BaseConfigurationModelListener<UserGroupUpdaterConfiguration> {
    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Class getConfigurationClass() {
        return UserGroupUpdaterConfiguration.class;
    }

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }
}
