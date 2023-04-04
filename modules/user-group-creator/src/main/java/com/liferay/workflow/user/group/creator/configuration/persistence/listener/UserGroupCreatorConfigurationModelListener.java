package com.liferay.workflow.user.group.creator.configuration.persistence.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import com.liferay.workflow.user.group.creator.configuration.UserGroupCreatorConfiguration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + UserGroupCreatorConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class UserGroupCreatorConfigurationModelListener extends BaseConfigurationModelListener<UserGroupCreatorConfiguration> {
    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Class getConfigurationClass() {
        return UserGroupCreatorConfiguration.class;
    }
}
