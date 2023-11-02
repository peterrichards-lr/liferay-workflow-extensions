package com.liferay.workflow.resource.permissions.updater.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseUserActionExecutorConfigurationWrapper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = ResourcePermissionsUpdaterConfiguration.PID,
        immediate = true, service = ResourcePermissionsUpdaterConfigurationWrapper.class
)
public class ResourcePermissionsUpdaterConfigurationWrapper extends BaseUserActionExecutorConfigurationWrapper<ResourcePermissionsUpdaterConfiguration> {
    public String[] getActionIds() {
        return getConfiguration().actionIds();
    }
    public boolean isWorkflowContextKeyUsedForEntityLookup() {
        return getConfiguration().useWorkflowContextKeyForEntityLookupValue();
    }
    public String getEntityLookupValueWorkflowContextKey() {
        return getConfiguration().entityLookupValueWorkflowContextKey();
    }
    public String getEntityLookupValue() {
        return getConfiguration().entityLookupValue();
    }
    public String getEntityType() {
        return getConfiguration().entityType();
    }
    public String getEntityLookupType() {
        return getConfiguration().entityLookupType();
    }
    public boolean isWorkflowContextKeyUsedForRoleLookup() {
        return getConfiguration().useWorkflowContextKeyForRoleLookupValue();
    }
    public String getRoleLookupValueWorkflowContextKey() {
        return getConfiguration().roleLookupValueWorkflowContextKey();
    }
    public String getRoleLookupValue() {
        return getConfiguration().roleLookupValue();
    }
    public String getRoleLookupType() {
        return getConfiguration().roleLookupType();
    }

    @Activate
    @Modified
    protected void activate(final Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final ResourcePermissionsUpdaterConfiguration configuration = ConfigurableUtil.createConfigurable(
                ResourcePermissionsUpdaterConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
