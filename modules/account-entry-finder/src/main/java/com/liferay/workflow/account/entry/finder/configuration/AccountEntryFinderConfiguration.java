package com.liferay.workflow.account.entry.finder.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.workflow.account.entry.finder.constants.AccountEntryFinderConstants;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;

@ExtendedObjectClassDefinition(
        category = "workflow", scope = ExtendedObjectClassDefinition.Scope.GROUP,
        factoryInstanceLabelAttribute = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID
)
@Meta.OCD(
        factory = true,
        id = AccountEntryFinderConfiguration.PID,
        localization = "content/Language", name = "config-account-entry-finder-name",
        description = "config-account-entry-finder-description"
)
public interface AccountEntryFinderConfiguration extends BaseActionExecutorConfiguration {
    String PID = "com.liferay.workflow.account.entry.finder.configuration.AccountEntryFinderConfiguration";

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
            deflt = AccountEntryFinderConstants.CONFIG_ENTITY_LOOKUP_NAME_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-entity-lookup-name-value-workflow-context-key-description",
            name = "config-entity-lookup-name-value-workflow-context-key-name",
            required = false
    )
    String entityLookupNameValueWorkflowContextKey();

    @Meta.AD(
            deflt = AccountEntryFinderConstants.CONFIG_ENTITY_LOOKUP_TYPE_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-entity-lookup-type-value-workflow-context-key-description",
            name = "config-entity-lookup-type-value-workflow-context-key-name",
            required = false
    )
    String entityLookupTypeValueWorkflowContextKey();

    @Meta.AD(
            deflt = AccountEntryFinderConstants.CONFIG_ENTITY_IDENTIFIER_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-entity-identifier-workflow-context-key-description",
            name = "config-entity-identifier-workflow-context-key-name",
            required = false
    )
    String entityIdentifierWorkflowContextKey();

    @Meta.AD(
            deflt = AccountEntryFinderConstants.CONFIG_ENTITY_ADMINISTRATOR_USER_IDENTIFIER_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-entity-administration-user-identifier-workflow-context-key-description",
            name = "config-entity-administration-user-identifier-workflow-context-key-name",
            required = false
    )
    String entityAdministrationUserIdentifier();
}
