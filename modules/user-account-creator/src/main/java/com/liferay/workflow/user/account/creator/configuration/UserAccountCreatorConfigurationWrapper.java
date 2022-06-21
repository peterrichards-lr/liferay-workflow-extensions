package com.liferay.workflow.user.account.creator.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseEntityCreatorActionExecutorConfigurationWrapper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = UserAccountCreatorConfiguration.PID,
        immediate = true, service = UserAccountCreatorConfigurationWrapper.class
)
public class UserAccountCreatorConfigurationWrapper extends BaseEntityCreatorActionExecutorConfigurationWrapper<UserAccountCreatorConfiguration> {

    @Activate
    @Modified
    protected void activate(Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final UserAccountCreatorConfiguration configuration = ConfigurableUtil.createConfigurable(
                UserAccountCreatorConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
