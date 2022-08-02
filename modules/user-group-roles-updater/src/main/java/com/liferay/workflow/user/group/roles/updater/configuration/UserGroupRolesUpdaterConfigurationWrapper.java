package com.liferay.workflow.user.group.roles.updater.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseEntityCreatorActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.configuration.UserLookupConfiguration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = UserGroupRolesUpdaterConfiguration.PID,
        immediate = true, service = UserGroupRolesUpdaterConfigurationWrapper.class
)
public class UserGroupRolesUpdaterConfigurationWrapper extends BaseEntityCreatorActionExecutorConfigurationWrapper<UserGroupRolesUpdaterConfiguration> implements UserLookupConfiguration {

    public boolean isInContextGroupIdRequired() {
        return getConfiguration().useInContextGroupId();
    }

    public boolean isWorkflowContextKeyUsedForGroupId() {
        return getConfiguration().useWorkflowContextKeyForGroupIdLookup();
    }

    public String getGroupIdWorkflowContextKey() {
        return getConfiguration().groupIdLookupValueWorkflowContextKey();
    }

    public String getGroupIdValue() {
        return getConfiguration().groupIdLookupValue();
    }

    public String getGroupIdLookupValueType() {
        return getConfiguration().groupIdLookupValueType();
    }

    public String getGroupIdType() {
        return getConfiguration().groupIdType();
    }

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

    public String[] getRoles() {
        return getConfiguration().roles();
    }

    @Activate
    @Modified
    protected void activate(final Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final UserGroupRolesUpdaterConfiguration configuration = ConfigurableUtil.createConfigurable(
                UserGroupRolesUpdaterConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
