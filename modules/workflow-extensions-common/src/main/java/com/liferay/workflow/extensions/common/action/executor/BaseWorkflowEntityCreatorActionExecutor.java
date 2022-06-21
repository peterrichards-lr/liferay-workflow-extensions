package com.liferay.workflow.extensions.common.action.executor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.configuration.BaseEntityCreatorActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.configuration.BaseEntityCreatorActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.configuration.model.EntityCreationAttributeConfiguration;
import com.liferay.workflow.extensions.common.configuration.model.MethodParameterConfiguration;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;
import com.liferay.workflow.extensions.common.util.EntityCreationAttributeUtil;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseWorkflowEntityCreatorActionExecutor<C extends BaseEntityCreatorActionExecutorConfiguration, W extends BaseEntityCreatorActionExecutorConfigurationWrapper<C>, S extends SettingsHelper<C, W>> extends BaseWorkflowUserActionExecutor<C, W, S> {
    protected abstract Map<String, MethodParameterConfiguration> getEntityCreationAttributeMap();

    protected Map<String, Object> buildMethodParametersMap(User creator, Map<String, Serializable> workflowContext, ServiceContext serviceContext, W configuration) throws ActionExecutorException {
        final Map<String, MethodParameterConfiguration> accountEntryCreationAttributes = getEntityCreationAttributeMap();
        final Map<String, EntityCreationAttributeConfiguration> entityCreationAttributeValues = configuration.getEntityCreationAttributeMap();

        final Set<String> entityCreationAttributeKeys = getEntityCreationAttributeMap().keySet();

        final Map<String, Object> methodParameters = new HashMap<>();

        for (String key : entityCreationAttributeKeys) {
            final MethodParameterConfiguration methodParameterConfiguration = accountEntryCreationAttributes.get(key);
            if (!entityCreationAttributeValues.containsKey(key)) {
                if (methodParameterConfiguration.isRequired()) {
                    throw new ActionExecutorException("No entityCreationAttributeValue was found for " + key + " and it is required");
                }
                final Object defaultValue = methodParameterConfiguration.getDefaultValue();
                _log.warn("No entityCreationAttributeValue for {}. The default value if {} will be used", key, defaultValue);
                methodParameters.put(key, defaultValue);
                continue;
            }
            final EntityCreationAttributeConfiguration entityCreationAttributeConfigurationValue = entityCreationAttributeValues.get(key);

            final String valueString;
            if (entityCreationAttributeConfigurationValue.isUseWorkflowContextKey()) {
                final String workflowContextKey = entityCreationAttributeConfigurationValue.getWorkflowContextKey();
                if (workflowContext.containsKey(workflowContextKey)) {
                    valueString = GetterUtil.getString(workflowContext.get(workflowContextKey));
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
                try {
                    value = WorkflowExtensionsConstants.DEFAULT_OBJECT_MAPPER.readValue(valueString, clazz);
                } catch (JsonProcessingException e) {
                    value = Array.newInstance(clazz, 0);
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
