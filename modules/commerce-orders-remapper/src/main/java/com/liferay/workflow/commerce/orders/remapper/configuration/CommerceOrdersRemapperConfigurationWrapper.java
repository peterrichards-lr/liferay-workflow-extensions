package com.liferay.workflow.commerce.orders.remapper.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseUserActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.configuration.UserLookupConfiguration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = CommerceOrdersRemapperConfiguration.PID,
        immediate = true, service = CommerceOrdersRemapperConfigurationWrapper.class
)
public class CommerceOrdersRemapperConfigurationWrapper extends BaseUserActionExecutorConfigurationWrapper<CommerceOrdersRemapperConfiguration> implements UserLookupConfiguration {
    public boolean isInContextUserRequired() {
        return getConfiguration().useInContextUser();
    }

    public boolean isWorkflowContextKeyUsedForUserLookup() {
        return getConfiguration().useWorkflowContextKeyForUserLookupValue();
    }

    public String getUserLookupValueWorkflowContextKey() {
        return getConfiguration().userLookupValueWorkflowContextKey();
    }

    public String getUserLookupValue() {
        return getConfiguration().userLookupValue();
    }

    public String getUserLookupType() {
        return getConfiguration().userLookupType();
    }

    @Activate
    @Modified
    protected void activate(final Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final CommerceOrdersRemapperConfiguration configuration = ConfigurableUtil.createConfigurable(
                CommerceOrdersRemapperConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
