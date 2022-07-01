package com.liferay.workflow.organisation.creator.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.workflow.extensions.common.configuration.BaseEntityCreatorActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.constants.UserActionExecutorConstants;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.organisation.creator.constants.OrganisationCreatorConstants;

@ExtendedObjectClassDefinition(
        category = "workflow", scope = ExtendedObjectClassDefinition.Scope.GROUP,
        factoryInstanceLabelAttribute = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID
)
@Meta.OCD(
        factory = true,
        id = OrganisationCreatorConfiguration.PID,
        localization = "content/Language", name = "config-organisation-creator-name",
        description = "config-organisation-creator-description"
)
public interface OrganisationCreatorConfiguration extends BaseEntityCreatorActionExecutorConfiguration {
    String PID = "com.liferay.workflow.organisation.creator.configuration.OrganisationCreatorConfiguration";

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
            deflt = UserActionExecutorConstants.CONFIG_USE_IN_CONTEXT_USER_FOR_CHANGE_DEFAULT,
            description = "config-use-in-context-user-description",
            name = "config-use-in-context-user-name",
            required = false
    )
    boolean useInContextUser();

    @Meta.AD(
            deflt = UserActionExecutorConstants.CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_USER_LOOKUP_VALUE_DEFAULT,
            description = "config-use-workflow-context-key-for-user-lookup-value-description",
            name = "config-use-workflow-context-key-for-user-lookup-value-name",
            required = false
    )
    boolean useWorkflowContextKeyForUserLookupValue();

    @Meta.AD(
            deflt = UserActionExecutorConstants.CONFIG_USER_LOOKUP_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-user-lookup-value-workflow-context-key-description",
            name = "config-user-lookup-value-workflow-context-key-name",
            required = false
    )
    String userLookupValueWorkflowContextKey();

    @Meta.AD(
            deflt = UserActionExecutorConstants.CONFIG_USER_LOOKUP_VALUE_DEFAULT,
            description = "config-user-lookup-value-description",
            name = "config-user-lookup-value-name",
            required = false
    )
    String userLookupValue();

    @Meta.AD(
            deflt = UserActionExecutorConstants.CONFIG_USER_LOOKUP_TYPE_DEFAULT,
            description = "config-user-lookup-type-description",
            name = "config-user-lookup-type-name",
            required = false
    )
    String userLookupType();

    @Meta.AD(
            deflt = OrganisationCreatorConstants.CONFIG_ENTITY_CREATION_ATTRIBUTES_DEFAULT,
            description = "config-entity-creation-attributes-description",
            name = "config-entity-creation-attributes-name",
            required = false
    )
    String[] entityCreationAttributes();

    @Meta.AD(
            deflt = OrganisationCreatorConstants.CONFIG_CREATED_ENTITY_IDENTIFIER_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-created-entity-identifier-workflow-context-key-description",
            name = "config-created-entity-identifier-workflow-context-key-name",
            required = false
    )
    String createdEntityIdentifierWorkflowContextKey();
}

