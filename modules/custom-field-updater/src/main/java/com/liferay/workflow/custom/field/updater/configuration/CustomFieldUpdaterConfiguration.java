package com.liferay.workflow.custom.field.updater.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.workflow.custom.field.updater.constants.CustomFieldUpdaterConstants;
import com.liferay.workflow.extensions.common.configuration.BaseUserActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.constants.UserActionExecutorConstants;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;

@ExtendedObjectClassDefinition(
        category = "workflow", scope = ExtendedObjectClassDefinition.Scope.GROUP,
        factoryInstanceLabelAttribute = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID
)
@Meta.OCD(
        factory = true,
        id = CustomFieldUpdaterConfiguration.PID,
        localization = "content/Language", name = "config-custom-field-updater-name",
        description = "config-custom-field-updater-description"
)
public interface CustomFieldUpdaterConfiguration extends BaseUserActionExecutorConfiguration {
    String PID = "com.liferay.workflow.custom.field.updater.configuration.CustomFieldUpdaterConfiguration";

    //@formatter:off
    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID_ACTION_DEFAULT,
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
            deflt = CustomFieldUpdaterConstants.CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_LOOKUP_VALUE_DEFAULT,
            description = "config-use-workflow-context-key-for-lookup-value-description",
            name = "config-use-workflow-context-key-for-lookup-value-name",
            required = false
    )
    boolean useWorkflowContextKeyForLookupValue();

    @Meta.AD(
            deflt = CustomFieldUpdaterConstants.CONFIG_ENTITY_LOOKUP_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-lookup-value-workflow-context-key-description",
            name = "config-lookup-value-workflow-context-key-name",
            required = false
    )
    String lookupValueWorkflowContextKey();

    @Meta.AD(
            deflt = CustomFieldUpdaterConstants.CONFIG_ENTITY_LOOKUP_VALUE_DEFAULT,
            description = "config-lookup-value-description",
            name = "config-lookup-value-name",
            required = false
    )
    String lookupValue();

    @Meta.AD(
            deflt = CustomFieldUpdaterConstants.CONFIG_ENTITY_LOOKUP_TYPE_DEFAULT,
            description = "config-lookup-type-description",
            name = "config-lookup-type-name",
            required = false
    )
    String lookupType();

    @Meta.AD(
            deflt = CustomFieldUpdaterConstants.CONFIG_CUSTOM_FIELD_PAIRS_DEFAULT,
            description = "config-custom-field-pairs-description",
            name = "config-custom-field-pairs-name",
            required = false
    )
    String[] customFieldPairs();

    @Meta.AD(
            deflt = CustomFieldUpdaterConstants.CONFIG_ENTITY_TYPE_DEFAULT,
            description = "config-entity-type-description",
            name = "config-entity-type-name",
            required = false
    )
    String entityType();

    @Meta.AD(
            deflt = UserActionExecutorConstants.CONFIG_USE_IN_CONTEXT_USER_FOR_ACTION_DEFAULT,
            description = "config-use-in-context-user-for-action-description",
            name = "config-use-in-context-user-for-action-name",
            required = false
    )
    boolean useInContextUserForAction();

    @Meta.AD(
            deflt = UserActionExecutorConstants.CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_ACTION_USER_LOOKUP_VALUE_DEFAULT,
            description = "config-use-workflow-context-key-for-action-user-lookup-value-description",
            name = "config-use-workflow-context-key-for-action-user-lookup-value-name",
            required = false
    )
    boolean useWorkflowContextKeyForActionUserLookupValue();

    @Meta.AD(
            deflt = UserActionExecutorConstants.CONFIG_ACTION_USER_LOOKUP_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-action-user-lookup-value-workflow-context-key-description",
            name = "config-action-user-lookup-value-workflow-context-key-name",
            required = false
    )
    String actionUserLookupValueWorkflowContextKey();

    @Meta.AD(
            deflt = UserActionExecutorConstants.CONFIG_ACTION_USER_LOOKUP_VALUE_DEFAULT,
            description = "config-action-user-lookup-value-description",
            name = "config-action-user-lookup-value-name",
            required = false
    )
    String actionUserLookupValue();

    @Meta.AD(
            deflt = UserActionExecutorConstants.CONFIG_ACTION_USER_LOOKUP_TYPE_DEFAULT,
            description = "config-action-user-lookup-type-description",
            name = "config-action-user-lookup-type-name",
            required = false
    )
    String actionUserLookupType();
    //@formatter:on
}
