package com.liferay.workflow.resource.permissions.updater.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.workflow.extensions.common.configuration.BaseUserActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.resource.permissions.updater.constants.ResourcePermissionsUpdaterConstants;

@ExtendedObjectClassDefinition(
        category = "workflow", scope = ExtendedObjectClassDefinition.Scope.GROUP,
        factoryInstanceLabelAttribute = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID
)
@Meta.OCD(
        factory = true,
        id = ResourcePermissionsUpdaterConfiguration.PID,
        localization = "content/Language", name = "config-resource-permissions-updater-name",
        description = "config-resource-permissions-updater-description"
)
public interface ResourcePermissionsUpdaterConfiguration extends BaseUserActionExecutorConfiguration {
    String PID = "com.liferay.workflow.resource.permissions.updater.configuration.ResourcePermissionsUpdaterConfiguration";

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
            deflt = ResourcePermissionsUpdaterConstants.CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_ENTITY_LOOKUP_VALUE_DEFAULT,
            description = "config-use-workflow-context-key-for-entity-lookup-value-description",
            name = "config-use-workflow-context-key-for-entity-lookup-value-name",
            required = false
    )
    boolean useWorkflowContextKeyForEntityLookupValue();

    @Meta.AD(
            deflt = ResourcePermissionsUpdaterConstants.CONFIG_ENTITY_LOOKUP_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-entity-lookup-value-workflow-context-key-description",
            name = "config-entity-lookup-value-workflow-context-key-name",
            required = false
    )
    String entityLookupValueWorkflowContextKey();

    @Meta.AD(
            deflt = ResourcePermissionsUpdaterConstants.CONFIG_ENTITY_LOOKUP_VALUE_DEFAULT,
            description = "config-entity-lookup-value-description",
            name = "config-entity-lookup-value-name",
            required = false
    )
    String entityLookupValue();

    @Meta.AD(
            deflt = ResourcePermissionsUpdaterConstants.CONFIG_ENTITY_LOOKUP_TYPE_DEFAULT,
            description = "config-lookup-type-description",
            name = "config-lookup-type-name",
            required = false
    )
    String entityLookupType();

    @Meta.AD(
            deflt = ResourcePermissionsUpdaterConstants.CONFIG_ENTITY_TYPE_DEFAULT,
            description = "config-entity-type-description",
            name = "config-entity-type-name",
            required = false
    )
    String entityType();

    @Meta.AD(
            deflt = ResourcePermissionsUpdaterConstants.CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_ROLE_LOOKUP_VALUE_DEFAULT,
            description = "config-use-workflow-context-key-for-role-lookup-value-description",
            name = "config-use-workflow-context-key-for-role-lookup-value-name",
            required = false
    )
    boolean useWorkflowContextKeyForRoleLookupValue();

    @Meta.AD(
            deflt = ResourcePermissionsUpdaterConstants.CONFIG_ROLE_LOOKUP_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-role-lookup-value-workflow-context-key-description",
            name = "config-role-lookup-value-workflow-context-key-name",
            required = false
    )
    String roleLookupValueWorkflowContextKey();

    @Meta.AD(
            deflt = ResourcePermissionsUpdaterConstants.CONFIG_ROLE_LOOKUP_VALUE_DEFAULT,
            description = "config-role-lookup-value-description",
            name = "config=role-lookup-value-name",
            required = false
    )
    String roleLookupValue();

    @Meta.AD(
            deflt = ResourcePermissionsUpdaterConstants.CONFIG_ROLE_LOOKUP_TYPE_DEFAULT,
            description = "config-role-lookup-type-description",
            name = "config-role-lookup-type-name",
            required = false
    )
    String roleLookupType();

    @Meta.AD(
            deflt = ResourcePermissionsUpdaterConstants.CONFIG_ACTION_IDENTIFIERS_DEFAULT,
            description = "config-action-identifiers-description",
            name = "config-action-identifiers-name",
            required = false
    )
    String[] actionIds();
    //@formatter:on
}
