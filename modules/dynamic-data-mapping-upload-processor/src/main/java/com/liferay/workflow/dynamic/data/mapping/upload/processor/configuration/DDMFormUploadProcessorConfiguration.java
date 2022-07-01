package com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.workflow.dynamic.data.mapping.upload.processor.constants.DDMFormUploadProcessorConstants;
import com.liferay.workflow.extensions.common.configuration.BaseConfiguration;
import com.liferay.workflow.extensions.common.configuration.BaseDDMFormActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.constants.DDMFormActionExecutorConstants;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;

@ExtendedObjectClassDefinition(
        category = "workflow", scope = ExtendedObjectClassDefinition.Scope.GROUP,
        factoryInstanceLabelAttribute = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID
)
@Meta.OCD(
        factory = true,
        id = DDMFormUploadProcessorConfiguration.PID,
        localization = "content/Language", name = "config-ddm-form-upload-processor-name",
        description = "config-ddm-form-upload-processor-description"
)
public interface DDMFormUploadProcessorConfiguration extends BaseConfiguration, BaseDDMFormActionExecutorConfiguration {
    String PID = "com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration.DDMFormUploadProcessorConfiguration";

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID_DEFAULT,
            description = "config-workflow-node-identifier-description",
            id = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID,
            name = "config-workflow-node-identifier-name",
            required = false
    )
    String identifier();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_ENABLE_DEFAULT,
            description = "config-enable-description",
            name = "config-enable-name",
            required = false
    )
    boolean enable();

    @Meta.AD(
            deflt = DDMFormActionExecutorConstants.CONFIG_USE_ENTRY_CLASS_PK_DEFAULT,
            description = "config-use-entry-class-pk-description",
            name = "config-use-entry-class-pk-name",
            required = false
    )
    boolean useEntryClassPrimaryKey();

    @Meta.AD(
            deflt = DDMFormActionExecutorConstants.CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_FORM_IDENTIFIER_DEFAULT,
            description = "config-use-workflow-context-key-for-form-identifier-description",
            name = "config-use-workflow-context-key-for-form-identifier-name",
            required = false
    )
    boolean useWorkflowContextKeyForFormInstanceRecordVersionId();

    @Meta.AD(
            deflt = DDMFormActionExecutorConstants.CONFIG_WORKFLOW_CONTEXT_KEY_FOR_FORM_IDENTIFIER_DEFAULT,
            description = "config-workflow-context-key-for-form-identifier-description",
            name = "config-workflow-context-key-for-form-identifier-name",
            required = false
    )
    String formInstanceRecordVersionIdValueWorkflowContextKey();

    @Meta.AD(
            deflt = DDMFormActionExecutorConstants.CONFIG_FORM_INSTANCE_ID_DEFAULT,
            description = "config-form-instance-identifier-description",
            name = "config-form-instance-identifier-name",
            required = false
    )
    long formInstanceRecordVersionId();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_UPDATE_WORKFLOW_STATUS_ON_SUCCESS_DEFAULT,
            description = "config-update-workflow-status-on-success-description",
            name = "config-update-workflow-status-on-success-name",
            required = false
    )
    boolean updateWorkflowStatusOnSuccess();

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
