package com.liferay.workflow.user.custom.field.updater.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.user.custom.field.updater.constants.UserCustomFieldUpdaterConstants;

@ExtendedObjectClassDefinition(
        category = "workflow", scope = ExtendedObjectClassDefinition.Scope.GROUP,
        factoryInstanceLabelAttribute = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID
)
@Meta.OCD(
        factory = true,
        id = UserCustomFieldUpdaterConfiguration.PID,
        localization = "content/Language", name = "config-user-custom-field-updater-name",
        description = "config-user-custom-field-updater-description"
)
public interface UserCustomFieldUpdaterConfiguration extends BaseActionExecutorConfiguration {
    String PID = "com.liferay.workflow.user.custom.field.updater.configuration.UserCustomFieldUpdaterConfiguration";

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
            deflt = UserCustomFieldUpdaterConstants.CONFIG_CUSTOM_FIELD_PAIRS_DEFAULT,
            description = "config-custom-field-pairs-description",
            name = "config-custom-pairs-name-name",
            required = false
    )
    String[] customFieldPairs();

    @Meta.AD(
            deflt = UserCustomFieldUpdaterConstants.CONFIG_USE_IN_CONTEXT_USER_DEFAULT,
            description = "config-use-in-context-user-description",
            name = "config-use-in-context-user-name",
            required = false
    )
    boolean useInContextUser();

    @Meta.AD(
            deflt = UserCustomFieldUpdaterConstants.CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_USER_LOOKUP_VALUE_DEFAULT,
            description = "config-use-workflow-context-key-for-user-lookup-value-description",
            name = "config-use-workflow-context-key-for-user-lookup-value-name",
            required = false
    )
    boolean useWorkflowContextKeyForUserLookupValue();

    @Meta.AD(
            deflt = UserCustomFieldUpdaterConstants.CONFIG_USER_LOOKUP_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-user-lookup-value-workflow-context-key-description",
            name = "config-user-lookup-value-workflow-context-key-name",
            required = false
    )
    String userLookupValueWorkflowContextKey();

    @Meta.AD(
            deflt = UserCustomFieldUpdaterConstants.CONFIG_USER_LOOKUP_VALUE_DEFAULT,
            description = "config-user-lookup-value-description",
            name = "config-user-lookup-value-name",
            required = false
    )
    String userLookupValue();

    @Meta.AD(
            deflt = UserCustomFieldUpdaterConstants.CONFIG_USER_LOOKUP_TYPE_DEFAULT,
            description = "config-user-lookup-type-description",
            name = "config-user-lookup-type-name",
            required = false
    )
    String userLookupType();
}
