package com.liferay.workflow.dynamic.data.mapping.form.options.translator.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.dynamic.data.mapping.form.options.translator.configuration.model.OptionTranslation;
import com.liferay.workflow.extensions.common.configuration.BaseFormActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = DDMFormOptionsTranslatorConfiguration.PID,
        immediate = true, service = DDMFormOptionsTranslatorConfigurationWrapper.class
)
public class DDMFormOptionsTranslatorConfigurationWrapper extends BaseFormActionExecutorConfigurationWrapper<DDMFormOptionsTranslatorConfiguration> {

    public List<OptionTranslation> getOptionTranslationArray() {
        final String[] optionTranslationJsonArray = getConfiguration().optionTranslationJsonArray();
        if (optionTranslationJsonArray != null) {
            List<OptionTranslation> optionTranslations = new ArrayList<>(optionTranslationJsonArray.length);
            for (String optionTranslationJson : optionTranslationJsonArray) {
                try {
                    OptionTranslation optionTranslation = WorkflowExtensionsConstants.DEFAULT_OBJECT_MAPPER.readValue(optionTranslationJson, OptionTranslation.class);
                    optionTranslations.add(optionTranslation);
                } catch (JsonProcessingException e) {
                    _log.warn("Failed to parse JSON object : {}", optionTranslationJson);
                }
            }
            _log.debug("optionTranslations size is {}", optionTranslations.size());
            return optionTranslations;
        }
        return Collections.emptyList();
    }

    @Activate
    @Modified
    protected void activate(Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final DDMFormOptionsTranslatorConfiguration configuration = ConfigurableUtil.createConfigurable(
                DDMFormOptionsTranslatorConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
