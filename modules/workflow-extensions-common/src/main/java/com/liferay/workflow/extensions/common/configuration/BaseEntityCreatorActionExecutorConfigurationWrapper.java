package com.liferay.workflow.extensions.common.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liferay.workflow.extensions.common.configuration.model.EntityCreationAttributeConfiguration;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BaseEntityCreatorActionExecutorConfigurationWrapper<T extends BaseEntityCreatorActionExecutorConfiguration> extends BaseUserActionExecutorConfigurationWrapper<T> {
    public Map<String, EntityCreationAttributeConfiguration> getEntityCreationAttributeMap() {
        final String[] entityCreationAttributesJsonArray = getConfiguration().entityCreationAttributes();
        if (entityCreationAttributesJsonArray != null) {
            final Map<String, EntityCreationAttributeConfiguration> entityCreationAttributes = new HashMap<>(entityCreationAttributesJsonArray.length);
            for (final String entityCreationAttributesJson : entityCreationAttributesJsonArray) {
                try {
                    final EntityCreationAttributeConfiguration entityCreationAttributeConfiguration = WorkflowExtensionsConstants.DEFAULT_OBJECT_MAPPER.readValue(entityCreationAttributesJson, EntityCreationAttributeConfiguration.class);
                    entityCreationAttributes.put(entityCreationAttributeConfiguration.getEntityAttributeName(), entityCreationAttributeConfiguration);
                } catch (final JsonProcessingException e) {
                    _log.warn("Failed to parse JSON object : {}", entityCreationAttributesJson);
                }
            }
            _log.debug("EntityCreationAttributes size is {}", entityCreationAttributes.size());
            return entityCreationAttributes;
        }
        return Collections.emptyMap();
    }

    public String getCreatedEntityIdentifierWorkflowContextKey() {
        return getConfiguration().createdEntityIdentifierWorkflowContextKey();
    }
}
