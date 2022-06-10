package com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper;
import com.liferay.workflow.extensions.common.configuration.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import org.jsoup.helper.StringUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = DDMFormInstanceRecordExtractorConfiguration.PID,
        immediate = true, service = DDMFormInstanceRecordExtractorConfigurationWrapper.class
)
public class DDMFormInstanceRecordExtractorConfigurationWrapper extends BaseConfigurationWrapper<DDMFormInstanceRecordExtractorConfiguration> {
    public String[] getDDMFieldReferenceArray() {
        return getConfiguration().ddmFieldReferenceArray();
    }

    public Map<String, String> getDDMUserDataFieldMap() {
        if (!StringUtil.isBlank(getConfiguration().ddmUserDataFieldMap())) {
            try {
                return WorkflowExtensionsConstants.DEFAULT_OBJECT_MAPPER.readValue(getConfiguration().ddmUserDataFieldMap(), WorkflowExtensionsConstants.CONFIG_MAP_TYPE);
            } catch (JsonProcessingException e) {
                _log.warn("Failed to parse JSON map : {}", getConfiguration().ddmUserDataFieldMap());
            }
        }
        return Collections.emptyMap();
    }

    public boolean isExtractUploadsRequired() {
        return getConfiguration().extractUploads();
    }

    public boolean isWorkflowInformationRequired() {
        return getConfiguration().includeWorkflowInformation();
    }

    @Activate
    @Modified
    protected void activate(Map<String, Object> properties) {
        _log.trace("Activating DDMFormUploadProcessorConfigurationWrapper : {}", properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final DDMFormInstanceRecordExtractorConfiguration configuration = ConfigurableUtil.createConfigurable(
                DDMFormInstanceRecordExtractorConfiguration.class, properties);

        super.setConfiguration(configuration);
    }

    @Override
    protected String toStringSubClass() {
        return "ddmFieldReferenceArray=" +
                "[" +
                String.join(WorkflowExtensionsConstants.TO_STRING_SEPARATOR, getDDMFieldReferenceArray()) +
                "]" +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "ddmUserDataFieldMap=" +
                "[" +
                WorkflowExtensionsUtil.mapAsString(getDDMUserDataFieldMap()) +
                "]" +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "extractUploads=" +
                isExtractUploadsRequired() +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "includeWorkflowInformation=" +
                isWorkflowInformationRequired();

    }
}
