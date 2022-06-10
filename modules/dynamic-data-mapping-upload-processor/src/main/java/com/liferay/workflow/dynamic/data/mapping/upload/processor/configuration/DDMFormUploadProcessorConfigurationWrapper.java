package com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper;
import com.liferay.workflow.extensions.common.configuration.constants.WorkflowExtensionsConstants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = DDMFormUploadProcessorConfiguration.PID,
        immediate = true, service = DDMFormUploadProcessorConfigurationWrapper.class
)
public class DDMFormUploadProcessorConfigurationWrapper extends BaseConfigurationWrapper<DDMFormUploadProcessorConfiguration> {
    public boolean isWorkflowKeyUsedForFolderName() {
        return getConfiguration().useWorkflowContextKeyForFolderName();
    }

    public String folderNameWorkflowContextKey() {
        return getConfiguration().folderNameWorkflowContextKey();
    }

    public String folderNameUserAttribute() {
        return getConfiguration().folderNameUserAttribute();
    }

    public boolean isFolderAlwaysCreated() {
        return getConfiguration().alwaysCreateFolder();
    }

    public long parentFolderId() {
        return getConfiguration().parentFolderId();
    }

    @Activate
    @Modified
    protected void activate(Map<String, Object> properties) {
        _log.trace("Activating DDMFormUploadProcessorConfigurationWrapper : {}", properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final DDMFormUploadProcessorConfiguration configuration = ConfigurableUtil.createConfigurable(
                DDMFormUploadProcessorConfiguration.class, properties);

        super.setConfiguration(configuration);
    }

    @Override
    protected String toStringSubClass() {
        return "useWorkflowContextKeyForFolderName=" +
                isWorkflowKeyUsedForFolderName() +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "folderNameWorkflowContextKey='" +
                folderNameWorkflowContextKey() + '\'' +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "folderNameUserAttribute='" +
                folderNameUserAttribute() + '\'' +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "alwaysCreateFolder=" +
                isFolderAlwaysCreated() +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "parentFolderId=" +
                parentFolderId();
    }
}
