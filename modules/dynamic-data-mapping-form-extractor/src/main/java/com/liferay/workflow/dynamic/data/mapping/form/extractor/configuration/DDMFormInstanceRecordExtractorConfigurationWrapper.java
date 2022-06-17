package com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
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
public class DDMFormInstanceRecordExtractorConfigurationWrapper extends BaseActionExecutorConfigurationWrapper<DDMFormInstanceRecordExtractorConfiguration> {
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

    @Override
    public String toString() {
        return "BaseActionExecutorConfigurationWrapper{" +
                "super=" + super.toString() +
                StringPool.COMMA +
                "ddmFieldReferenceArray=" + StringPool.OPEN_BRACKET +
                String.join(",", getDDMFieldReferenceArray()) +
                StringPool.CLOSE_BRACKET + StringPool.COMMA +
                "ddmUserDataFieldMap=" + StringPool.OPEN_BRACKET +
                WorkflowExtensionsUtil.mapAsString(getDDMUserDataFieldMap()) +
                StringPool.CLOSE_BRACKET + StringPool.COMMA +
                "extractUploads=" + getConfiguration().extractUploads() +
                StringPool.COMMA +
                "includeWorkflowInformation=" + getConfiguration().includeWorkflowInformation() +
                '}';
    }

    @Activate
    @Modified
    protected void activate(Map<String, Object> properties) {
        _log.trace("Activating {} : {}".getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final DDMFormInstanceRecordExtractorConfiguration configuration = ConfigurableUtil.createConfigurable(
                DDMFormInstanceRecordExtractorConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
