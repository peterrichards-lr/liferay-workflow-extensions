package com.liferay.workflow.extensions.common.configuration;

import com.liferay.workflow.extensions.common.configuration.model.EntityCreationAttributeConfiguration;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;

import java.util.Map;
import java.util.function.Function;

public class BaseEntityCreatorActionExecutorConfigurationWrapper<T extends BaseEntityCreatorActionExecutorConfiguration> extends BaseUserActionExecutorConfigurationWrapper<T> {
    public String getCreatedEntityIdentifierWorkflowContextKey() {
        return getConfiguration().createdEntityIdentifierWorkflowContextKey();
    }

    public Map<String, EntityCreationAttributeConfiguration> getEntityCreationAttributeMap() {
        final String[] entityCreationAttributesJsonArray = getConfiguration().entityCreationAttributes();
        final Function<EntityCreationAttributeConfiguration, String> keyFinder = this::getEntityCreationAttributeConfigurationKey;
        return WorkflowExtensionsUtil.getJsonConfigurationValuesAsMap(entityCreationAttributesJsonArray, EntityCreationAttributeConfiguration.class, keyFinder, _log);
    }

    public String getEntityCreationAttributeConfigurationKey(final EntityCreationAttributeConfiguration entityCreationAttributeConfiguration) {
        return entityCreationAttributeConfiguration.getEntityAttributeName();
    }
}
