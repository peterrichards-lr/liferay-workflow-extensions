package com.liferay.workflow.extensions.common.action.executor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.configuration.BaseEntityCreatorActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.configuration.BaseEntityCreatorActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.configuration.model.EntityCreationAttributeConfiguration;
import com.liferay.workflow.extensions.common.configuration.model.MethodParameterConfiguration;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;
import com.liferay.workflow.extensions.common.util.EntityCreationAttributeUtil;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import org.jsoup.helper.StringUtil;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseWorkflowEntityCreatorActionExecutor<C extends BaseEntityCreatorActionExecutorConfiguration, W extends BaseEntityCreatorActionExecutorConfigurationWrapper<C>, S extends SettingsHelper<C, W>> extends BaseWorkflowUserActionExecutor<C, W, S> {
    @SuppressWarnings("rawtypes")
    protected Map<String, MethodParameterConfiguration> getEntityCreationAttributeMap() {
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes", "unused"})
    protected Map<String, Object> buildMethodParametersMap(final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final W configuration) throws ActionExecutorException {
        final Map<String, MethodParameterConfiguration> accountEntryCreationAttributes = getEntityCreationAttributeMap();
        final Map<String, EntityCreationAttributeConfiguration> entityCreationAttributeValues = configuration.getEntityCreationAttributeMap();

        final Map<String, MethodParameterConfiguration> entityCreationAttributeMap = getEntityCreationAttributeMap();
        final Set<String> entityCreationAttributeKeys = entityCreationAttributeMap != null ? entityCreationAttributeMap.keySet() : Collections.emptySet();

        final Map<String, Object> methodParameters = new HashMap<>();

        for (final String key : entityCreationAttributeKeys) {
            final MethodParameterConfiguration methodParameterConfiguration = accountEntryCreationAttributes.get(key);
            if (!entityCreationAttributeValues.containsKey(key)) {
                if (methodParameterConfiguration.isRequired()) {
                    throw new ActionExecutorException("No entityCreationAttributeValue was found for " + key + " and it is required");
                }
                final Object defaultValue = methodParameterConfiguration.getDefaultValue();
                _log.info("No entityCreationAttributeValue for {}. The default value if {} will be used", key, defaultValue);
                methodParameters.put(key, defaultValue);
                continue;
            }
            final EntityCreationAttributeConfiguration entityCreationAttributeConfigurationValue = entityCreationAttributeValues.get(key);

            final String valueString;
            if (entityCreationAttributeConfigurationValue.isUseWorkflowContextKey()) {
                final String workflowContextKey = entityCreationAttributeConfigurationValue.getWorkflowContextKey();
                if (workflowContext.containsKey(workflowContextKey)) {
                    valueString = String.valueOf(workflowContext.get(workflowContextKey));
                    _log.debug("The value of {} will be {}", key, valueString);
                } else {
                    valueString = entityCreationAttributeConfigurationValue.getDefaultValue();
                    _log.debug("{} was not found in the workflowContext. The value of {} will use the default value of {}", workflowContextKey, key, valueString);
                }
            } else {
                valueString = entityCreationAttributeConfigurationValue.getDefaultValue();
                _log.debug("The value of {} will use the default value of {}", key, valueString);
            }

            final Class clazz = methodParameterConfiguration.getMethodParameterClass();
            Object value;
            if (clazz.isArray()) {
                _log.debug("{} is an array. The value provided is {}", clazz.getSimpleName(), valueString);
                if (WorkflowExtensionsUtil.isJSONValid(valueString)) {
                    try {
                        value = WorkflowExtensionsConstants.DEFAULT_OBJECT_MAPPER.readValue(valueString, clazz);
                    } catch (final JsonProcessingException e) {
                        final Class<?> componentClazz = clazz.getComponentType();
                        _log.trace("componentType is {}", componentClazz.getSimpleName());
                        value = Array.newInstance(componentClazz, 0);
                    }
                } else if (StringUtil.isBlank(valueString)) {
                    final Class<?> componentClazz = clazz.getComponentType();
                    _log.trace("componentType is {}", componentClazz.getSimpleName());
                    value = Array.newInstance(componentClazz, 0);
                } else {
                    final Class<?> componentClazz = clazz.getComponentType();
                    _log.trace("componentType is {}", componentClazz.getSimpleName());
                    final String[] innerValueStringArray = valueString.split(",");
                    final Object[] innerValueArray = (Object[]) Array.newInstance(componentClazz, innerValueStringArray.length);
                    int i = 0;
                    for (final String innerValue : innerValueStringArray) {
                        Array.set(innerValueArray, i, EntityCreationAttributeUtil.parse(innerValue, componentClazz));
                        i++;
                    }
                    value = innerValueArray;
                }
                _log.trace("Array size is {}", Array.getLength(value));
            } else {
                value = EntityCreationAttributeUtil.parse(valueString, clazz);
            }
            methodParameters.put(key, value);
        }
        return methodParameters;
    }
}
