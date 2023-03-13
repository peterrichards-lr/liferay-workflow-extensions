package com.liferay.workflow.dynamic.data.mapping.form.options.translator.configuration;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.dynamic.data.mapping.form.options.translator.configuration.model.OptionTranslation;
import com.liferay.workflow.extensions.common.configuration.BaseDDMFormActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = DDMFormOptionsTranslatorConfiguration.PID,
        immediate = true, service = DDMFormOptionsTranslatorConfigurationWrapper.class
)
public class DDMFormOptionsTranslatorConfigurationWrapper extends BaseDDMFormActionExecutorConfigurationWrapper<DDMFormOptionsTranslatorConfiguration> {

    public List<OptionTranslation> getOptionTranslationArray() {
        final String[] optionTranslationJsonArray = getConfiguration().optionTranslationJsonArray();
        return WorkflowExtensionsUtil.getJsonConfigurationValuesAsList(optionTranslationJsonArray, OptionTranslation.class, _log);
    }

    @Override
    public String toString() {
        return "BaseActionExecutorConfigurationWrapper{" +
                "super=" + super.toString() +
                StringPool.COMMA +
                "optionTranslations=" + StringPool.OPEN_BRACKET +
                getOptionTranslationArray().stream().map(OptionTranslation::toString).collect(Collectors.joining(StringPool.COMMA)) +
                StringPool.CLOSE_BRACKET +
                '}';
    }

    @Activate
    @Modified
    protected void activate(final Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final DDMFormOptionsTranslatorConfiguration configuration = ConfigurableUtil.createConfigurable(
                DDMFormOptionsTranslatorConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
