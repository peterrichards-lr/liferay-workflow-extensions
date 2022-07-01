package com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseDDMFormActionExecutorConfigurationWrapper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = DDMFormUploadProcessorConfiguration.PID,
        immediate = true, service = DDMFormUploadProcessorConfigurationWrapper.class
)
public class DDMFormUploadProcessorConfigurationWrapper extends BaseDDMFormActionExecutorConfigurationWrapper<DDMFormUploadProcessorConfiguration> {
    public boolean isWorkflowKeyUsedForFolderName() {
        return getConfiguration().useWorkflowContextKeyForFolderName();
    }

    public String getFolderNameWorkflowContextKey() {
        return getConfiguration().folderNameWorkflowContextKey();
    }

    public String getFolderNameUserAttribute() {
        return getConfiguration().folderNameUserAttribute();
    }

    public boolean isFolderAlwaysCreated() {
        return getConfiguration().alwaysCreateFolder();
    }

    public long getParentFolderId() {
        return getConfiguration().parentFolderId();
    }

    @Override
    public String toString() {
        return "BaseActionExecutorConfigurationWrapper{" +
                "super=" + super.toString() +
                StringPool.COMMA +
                "useWorkflowContextKeyForFolderName=" + getConfiguration().useWorkflowContextKeyForFolderName() +
                StringPool.COMMA +
                "folderNameWorkflowContextKey=" + StringPool.APOSTROPHE + getConfiguration().folderNameWorkflowContextKey() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "folderNameUserAttribute=" + StringPool.APOSTROPHE + getConfiguration().folderNameUserAttribute() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "alwaysCreateFolder=" + getConfiguration().alwaysCreateFolder() +
                StringPool.COMMA +
                "parentFolderId=" + getConfiguration().parentFolderId() +
                '}';
    }

    @Activate
    @Modified
    protected void activate(Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final DDMFormUploadProcessorConfiguration configuration = ConfigurableUtil.createConfigurable(
                DDMFormUploadProcessorConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
