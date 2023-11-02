package com.liferay.workflow.user.role.creator.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseEntityCreatorActionExecutorConfigurationWrapper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = UserRoleCreatorConfiguration.PID,
        immediate = true, service = UserRoleCreatorConfigurationWrapper.class
)
public class UserRoleCreatorConfigurationWrapper extends BaseEntityCreatorActionExecutorConfigurationWrapper<UserRoleCreatorConfiguration>  {
    @Activate
    @Modified
    protected void activate(final Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final UserRoleCreatorConfiguration configuration = ConfigurableUtil.createConfigurable(
                UserRoleCreatorConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
