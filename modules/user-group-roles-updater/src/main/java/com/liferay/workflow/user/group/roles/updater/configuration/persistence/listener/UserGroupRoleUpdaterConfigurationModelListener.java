package com.liferay.workflow.user.group.roles.updater.configuration.persistence.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import com.liferay.workflow.user.group.roles.updater.configuration.UserGroupRolesUpdaterConfiguration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + UserGroupRolesUpdaterConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class UserGroupRoleUpdaterConfigurationModelListener extends BaseConfigurationModelListener<UserGroupRolesUpdaterConfiguration> {
    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Class getConfigurationClass() {
        return UserGroupRolesUpdaterConfiguration.class;
    }
}
