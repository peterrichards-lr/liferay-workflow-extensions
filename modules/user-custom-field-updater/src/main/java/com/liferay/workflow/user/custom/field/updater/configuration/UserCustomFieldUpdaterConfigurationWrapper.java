package com.liferay.workflow.user.custom.field.updater.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.user.custom.field.updater.configuration.model.CustomFieldPair;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = UserCustomFieldUpdaterConfiguration.PID,
        immediate = true, service = UserCustomFieldUpdaterConfigurationWrapper.class
)
public class UserCustomFieldUpdaterConfigurationWrapper extends BaseActionExecutorConfigurationWrapper<UserCustomFieldUpdaterConfiguration> {

    @Override
    public String getIdentifier() {
        return getConfiguration().identifier().toLowerCase();
    }

    public List<CustomFieldPair> getCustomFieldPairsList() {
        final String[] customFieldPairsJsonArray = getConfiguration().customFieldPairs();
        if (customFieldPairsJsonArray != null) {
            List<CustomFieldPair> customFieldPairs = new ArrayList<>(customFieldPairsJsonArray.length);
            for (String customFieldPairJson : customFieldPairsJsonArray) {
                try {
                    CustomFieldPair optionTranslation = WorkflowExtensionsConstants.DEFAULT_OBJECT_MAPPER.readValue(customFieldPairJson, CustomFieldPair.class);
                    customFieldPairs.add(optionTranslation);
                } catch (JsonProcessingException e) {
                    _log.warn("Failed to parse JSON object : {}", customFieldPairJson);
                }
            }
            _log.debug("customFieldPairs size is {}", customFieldPairs.size());
            return customFieldPairs;
        }
        return Collections.emptyList();
    }

    public boolean isInContextUserRequired() {
        return getConfiguration().useInContextUser();
    }

    public boolean isWorkflowContextKeyUsedForLookup() {
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

    @Override
    public String toString() {
        return "BaseActionExecutorConfigurationWrapper{" +
                "super=" + super.toString() +
                StringPool.COMMA +
                "customFieldPairs=" + StringPool.OPEN_BRACKET +
                getCustomFieldPairsList().stream().map(cfp -> cfp.toString()).collect(Collectors.joining(StringPool.COMMA)) +
                StringPool.CLOSE_BRACKET +
                StringPool.COMMA +
                "useInContextUser=" + getConfiguration().useInContextUser() +
                StringPool.COMMA +
                "useWorkflowContextKeyForUserLookupValue=" + getConfiguration().useWorkflowContextKeyForUserLookupValue() +
                StringPool.COMMA +
                "userLookupValueWorkflowContextKey=" + StringPool.APOSTROPHE + getConfiguration().userLookupValueWorkflowContextKey() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "userLookupValue=" + StringPool.APOSTROPHE + getConfiguration().userLookupValue() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "userLookupType=" + StringPool.APOSTROPHE + getConfiguration().userLookupType() + StringPool.APOSTROPHE +
                '}';
    }

    @Activate
    @Modified
    protected void activate(Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final UserCustomFieldUpdaterConfiguration configuration = ConfigurableUtil.createConfigurable(
                UserCustomFieldUpdaterConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
