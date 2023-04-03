package com.liferay.workflow.user.group.creator.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseEntityCreatorActionExecutorConfigurationWrapper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = UserGroupCreatorConfiguration.PID,
        immediate = true, service = UserGroupCreatorConfigurationWrapper.class
)
public class UserGroupCreatorConfigurationWrapper extends BaseEntityCreatorActionExecutorConfigurationWrapper<UserGroupCreatorConfiguration> {
    @Activate
    @Modified
    protected void activate(final Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final UserGroupCreatorConfiguration configuration = ConfigurableUtil.createConfigurable(
                UserGroupCreatorConfiguration.class, properties);
        super.setConfiguration(configuration);
    }
}