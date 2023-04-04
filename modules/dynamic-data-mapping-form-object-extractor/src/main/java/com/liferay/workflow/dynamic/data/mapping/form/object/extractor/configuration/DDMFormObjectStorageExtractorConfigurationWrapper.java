package com.liferay.workflow.dynamic.data.mapping.form.object.extractor.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfigurationWrapper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = DDMFormObjectStorageExtractorConfiguration.PID,
        immediate = true, service = DDMFormObjectStorageExtractorConfigurationWrapper.class
)
public class DDMFormObjectStorageExtractorConfigurationWrapper extends BaseActionExecutorConfigurationWrapper<DDMFormObjectStorageExtractorConfiguration> {

    public String[] getDDMFieldReferenceArray() {
        return getConfiguration().ddmFieldReferenceArray();
    }

    public boolean isWorkflowInformationRequired() {
        return getConfiguration().includeWorkflowInformation();
    }

    @Activate
    @Modified
    protected void activate(final Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final DDMFormObjectStorageExtractorConfiguration configuration = ConfigurableUtil.createConfigurable(
                DDMFormObjectStorageExtractorConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
