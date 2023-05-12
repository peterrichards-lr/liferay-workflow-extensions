package com.liferay.workflow.user.group.updater.configuration;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseUserActionExecutorConfigurationWrapper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = UserGroupUpdaterConfiguration.PID,
        immediate = true, service = UserGroupUpdaterConfigurationWrapper.class
)
public class UserGroupUpdaterConfigurationWrapper extends BaseUserActionExecutorConfigurationWrapper<UserGroupUpdaterConfiguration> {
    public boolean isWorkflowContextKeyUsedForUserGroupId() {
        return getConfiguration().useWorkflowContextKeyForUserGroupIdLookup();
    }

    public String getUserGroupIdWorkflowContextKey() {
        return getConfiguration().userGroupIdLookupValueWorkflowContextKey();
    }

    public String getUserGroupIdValue() {
        return getConfiguration().userGroupIdLookupValue();
    }

    public boolean isWorkflowContextKeyUsedForValueArray() {
        return getConfiguration().useWorkflowContextKeyForValueArray();
    }

    public String getValueArrayWorkflowContextKey() {
        return getConfiguration().valueArrayWorkflowContextKey();
    }

    public String[] getValueArray() {
        return getConfiguration().valueArray() == null
                ? new String[0]
                : getConfiguration().valueArray().split(StringPool.COMMA);
    }

    public String getValueLookupType() {
        return getConfiguration().lookupType();
    }

    @Activate
    @Modified
    protected void activate(final Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final UserGroupUpdaterConfiguration configuration = ConfigurableUtil.createConfigurable(
                UserGroupUpdaterConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
