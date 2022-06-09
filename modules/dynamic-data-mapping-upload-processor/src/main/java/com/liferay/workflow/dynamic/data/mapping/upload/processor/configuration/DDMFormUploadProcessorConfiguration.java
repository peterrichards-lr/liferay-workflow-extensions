package com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.workflow.dynamic.data.mapping.upload.processor.constants.DDMFormUploadProcessorConstants;
import com.liferay.workflow.extensions.common.configuration.BaseConfiguration;
import com.liferay.workflow.extensions.common.configuration.constants.WorkflowExtensionsConstants;

@ExtendedObjectClassDefinition(
        category = "workflow", scope = ExtendedObjectClassDefinition.Scope.GROUP,
        factoryInstanceLabelAttribute = WorkflowExtensionsConstants.CONFIG_FORM_INSTANCE_ID
)
@Meta.OCD(
        factory = true,
        id = DDMFormUploadProcessorConfiguration.PID,
        localization = "content/Language", name = "config-ddm-form-upload-processor-name",
        description = "config-ddm-form-upload-processor-description"
)
public interface DDMFormUploadProcessorConfiguration extends BaseConfiguration {
    String PID = "com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration.DDMFormUploadProcessorConfiguration";

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_FORM_INSTANCE_ID_DEFAULT,
            description = "config-ddm-form-instance-identifier-description",
            id = WorkflowExtensionsConstants.CONFIG_FORM_INSTANCE_ID,
            name = "config-ddm-form-instance-identifier-name",
            required = false
    )
    long formInstanceId();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_ENABLE_DEFAULT,
            description = "config-enable-description",
            name = "config-enable-name",
            required = false
    )
    boolean enable();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_SUCCESS_WORKFLOW_STATUS_DEFAULT,
            description = "config-success-workflow-status-description",
            name = "config-success-workflow-status-name",
            required = false
    )
    String successWorkflowStatus();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_UPDATE_WORKFLOW_STATUS_ON_EXCEPTION_DEFAULT,
            description = "config-update-workflow-status-on-exception-description",
            name = "config-update-workflow-status-on-exception-name",
            required = false
    )
    boolean updateWorkflowStatusOnException();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_EXCEPTION_WORKFLOW_STATUS_DEFAULT,
            description = "config-exception-workflow-status-description",
            name = "config-exception-workflow-status-name",
            required = false
    )
    String exceptionWorkflowStatus();

    @Meta.AD(
            deflt = DDMFormUploadProcessorConstants.CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_FOLDER_NAME_DEFAULT,
            description = "config-use-workflow-context-key-for-folder-name-description",
            name = "config-use-workflow-context-key-for-folder-name-name",
            required = false
    )
    boolean useWorkflowContextKeyForFolderName();

    @Meta.AD(
            deflt = DDMFormUploadProcessorConstants.CONFIG_FOLDER_NAME_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-folder-name-workflow-context-key-description",
            name = "config-folder-name-workflow-context-key-name",
            required = false
    )
    String folderNameWorkflowContextKey();

    @Meta.AD(
            deflt = DDMFormUploadProcessorConstants.CONFIG_FOLDER_NAME_USER_ATTRIBUTE_DEFAULT,
            description = "config-folder-name-user-attribute-description",
            name = "config-folder-name-user-attribute-name",
            required = false
    )
    String folderNameUserAttribute();

    @Meta.AD(
            deflt = DDMFormUploadProcessorConstants.CONFIG_ALWAYS_CREATE_FOLDER_DEFAULT,
            description = "config-always-create-folder-description",
            name = "config-always-create-folder-name",
            required = false
    )
    boolean alwaysCreateFolder();

    @Meta.AD(
            deflt = DDMFormUploadProcessorConstants.CONFIG_PARENT_FOLDER_IDENTIFIER_DEFAULT,
            description = "config-parent-folder-identifier-description",
            name = "config-parent-folder-identifier-name",
            required = false
    )
    long parentFolderId();
}
