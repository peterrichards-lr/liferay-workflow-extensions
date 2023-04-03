package com.liferay.workflow.account.entry.creator.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.workflow.account.entry.creator.constants.AccountEntryCreatorConstants;
import com.liferay.workflow.extensions.common.configuration.BaseEntityCreatorActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.constants.UserActionExecutorConstants;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;

@ExtendedObjectClassDefinition(
        category = "workflow", scope = ExtendedObjectClassDefinition.Scope.GROUP,
        factoryInstanceLabelAttribute = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID
)
@Meta.OCD(
        factory = true,
        id = AccountEntryCreatorConfiguration.PID,
        localization = "content/Language", name = "config-account-entry-creator-name",
        description = "config-account-entry-creator-description"
)
public interface AccountEntryCreatorConfiguration extends BaseEntityCreatorActionExecutorConfiguration {
    String PID = "com.liferay.workflow.account.entry.creator.configuration.AccountEntryCreatorConfiguration";

    @Meta.AD(
            deflt = UserActionExecutorConstants.CONFIG_ACTION_USER_LOOKUP_TYPE_DEFAULT,
            description = "config-action-user-lookup-type-description",
            name = "config-action-user-lookup-type-name",
            required = false
    )
    String actionUserLookupType();

    @Meta.AD(
            deflt = UserActionExecutorConstants.CONFIG_ACTION_USER_LOOKUP_VALUE_DEFAULT,
            description = "config-action-user-lookup-value-description",
            name = "config-action-user-lookup-value-name",
            required = false
    )
    String actionUserLookupValue();

    @Meta.AD(
            deflt = UserActionExecutorConstants.CONFIG_ACTION_USER_LOOKUP_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-action-user-lookup-value-workflow-context-key-description",
            name = "config-action-user-lookup-value-workflow-context-key-name",
            required = false
    )
    String actionUserLookupValueWorkflowContextKey();

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
            deflt = AccountEntryCreatorConstants.CONFIG_CREATED_ENTITY_IDENTIFIER_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-created-entity-identifier-workflow-context-key-description",
            name = "config-created-entity-identifier-workflow-context-key-name",
            required = false
    )
    String createdEntityIdentifierWorkflowContextKey();

    @Meta.AD(
            deflt = AccountEntryCreatorConstants.CONFIG_ENTITY_CREATION_ATTRIBUTES_DEFAULT,
            description = "config-entity-creation-attributes-description",
            name = "config-entity-creation-attributes-name",
            required = false
    )
    String[] entityCreationAttributes();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_ENABLE_DEFAULT,
            description = "config-enable-description",
            name = "config-enable-name",
            required = false
    )
    boolean enable();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID_ACTION_DEFAULT,
            description = "config-workflow-node-identifier-description",
            id = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID,
            name = "config-workflow-node-identifier-name",
            required = false
    )
    String identifier();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_EXCEPTION_WORKFLOW_STATUS_DEFAULT,
            description = "config-exception-workflow-status-description",
            name = "config-exception-workflow-status-name",
            required = false
    )
    String exceptionWorkflowStatus();

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
            deflt = WorkflowExtensionsConstants.CONFIG_UPDATE_WORKFLOW_STATUS_ON_SUCCESS_DEFAULT,
            description = "config-update-workflow-status-on-success-description",
            name = "config-update-workflow-status-on-success-name",
            required = false
    )
    boolean updateWorkflowStatusOnSuccess();

    @Meta.AD(
            deflt = AccountEntryCreatorConstants.CONFIG_USE_EXISTING_IF_FOUND_DEFAULT,
            description = "config-use-existing-if-found-description",
            name = "config-use-existing-if-found-name",
            required = false
    )
    boolean useExistingIfFound();
}
