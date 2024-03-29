package com.liferay.workflow.custom.field.updater.configuration;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.custom.field.updater.configuration.model.CustomFieldPair;
import com.liferay.workflow.extensions.common.configuration.BaseUserActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = CustomFieldUpdaterConfiguration.PID,
        immediate = true, service = CustomFieldUpdaterConfigurationWrapper.class
)
public class CustomFieldUpdaterConfigurationWrapper extends BaseUserActionExecutorConfigurationWrapper<CustomFieldUpdaterConfiguration> {

    public List<CustomFieldPair> getCustomFieldPairsList() {
        final String[] customFieldPairsJsonArray = getConfiguration().customFieldPairs();
        return WorkflowExtensionsUtil.getJsonConfigurationValuesAsList(customFieldPairsJsonArray, CustomFieldPair.class, _log);
    }

    public boolean isWorkflowContextKeyUsedForLookup() {
        return getConfiguration().useWorkflowContextKeyForLookupValue();
    }

    public String getLookupValueWorkflowContextKey() {
        return getConfiguration().lookupValueWorkflowContextKey();
    }

    public String getLookupValue() {
        return getConfiguration().lookupValue();
    }

    public String getEntityType() {
        return getConfiguration().entityType();
    }

    public String getLookupType() {
        return getConfiguration().lookupType();
    }

    @Override
    public String toString() {
        return "CustomFieldUpdaterConfigurationWrapper{" +
                "super=" + super.toString() +
                StringPool.COMMA +
                "customFieldPairs=" + StringPool.OPEN_BRACKET +
                getCustomFieldPairsList().stream().map(CustomFieldPair::toString).collect(Collectors.joining(StringPool.COMMA)) +
                StringPool.CLOSE_BRACKET +
                StringPool.COMMA +
                "entityType=" + getConfiguration().entityType() +
                StringPool.COMMA +
                "useWorkflowContextKeyForLookupValue=" + getConfiguration().useWorkflowContextKeyForLookupValue() +
                StringPool.COMMA +
                "lookupValueWorkflowContextKey=" + StringPool.APOSTROPHE + getConfiguration().lookupValueWorkflowContextKey() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "lookupValue=" + StringPool.APOSTROPHE + getConfiguration().lookupValue() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "lookupType=" + StringPool.APOSTROPHE + getConfiguration().lookupType() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "useInContextUserForAction=" + getConfiguration().useInContextUserForAction() +
                StringPool.COMMA +
                "useWorkflowContextKeyForActionUserLookupValue=" + getConfiguration().actionUserLookupValueWorkflowContextKey() +
                StringPool.COMMA +
                "actionUserLookupValueWorkflowContextKey=" + StringPool.APOSTROPHE + getConfiguration().actionUserLookupValueWorkflowContextKey() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "actionUserLookupValue=" + StringPool.APOSTROPHE + getConfiguration().actionUserLookupValue() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "actionUserLookupType=" + StringPool.APOSTROPHE + getConfiguration().actionUserLookupType() + StringPool.APOSTROPHE +
                '}';
    }

    @Activate
    @Modified
    protected void activate(final Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final CustomFieldUpdaterConfiguration configuration = ConfigurableUtil.createConfigurable(
                CustomFieldUpdaterConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
