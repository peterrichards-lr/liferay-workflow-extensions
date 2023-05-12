package com.liferay.workflow.user.group.roles.updater.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.workflow.extensions.common.configuration.BaseUserActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.constants.UserActionExecutorConstants;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.user.group.roles.updater.constants.UserGroupRolesUpdaterConstants;

@ExtendedObjectClassDefinition(
        category = "workflow", scope = ExtendedObjectClassDefinition.Scope.GROUP,
        factoryInstanceLabelAttribute = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID
)
@Meta.OCD(
        factory = true,
        id = UserGroupRolesUpdaterConfiguration.PID,
        localization = "content/Language", name = "config-user-group-roles-updater-name",
        description = "config-user-group-roles-updater-description"
)
public interface UserGroupRolesUpdaterConfiguration extends BaseUserActionExecutorConfiguration {
    String PID = "com.liferay.workflow.user.group.roles.updater.configuration.UserGroupRolesUpdaterConfiguration";

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

    @Meta.AD(
            deflt = UserGroupRolesUpdaterConstants.CONFIG_USE_IN_CONTEXT_GROUP_ID_DEFAULT,
            description = "config-use-in-context-group-id-description",
            name = "config-use-in-context-group-id-name",
            required = false
    )
    boolean useInContextGroupId();

    @Meta.AD(
            deflt = UserGroupRolesUpdaterConstants.CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_GROUP_ID_DEFAULT,
            description = "config-use-workflow-context-key-for-group-id-lookup-value-description",
            name = "config-use-workflow-context-key-for-group-id-lookup-value-name",
            required = false
    )
    boolean useWorkflowContextKeyForGroupIdLookup();

    @Meta.AD(
            deflt = UserGroupRolesUpdaterConstants.CONFIG_GROUP_ID_LOOKUP_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-group-id-lookup-value-workflow-context-key-description",
            name = "config-group-id-lookup-value-workflow-context-key-name",
            required = false
    )
    String groupIdLookupValueWorkflowContextKey();

    @Meta.AD(
            deflt = UserGroupRolesUpdaterConstants.CONFIG_GROUP_ID_LOOKUP_VALUE_DEFAULT,
            description = "config-group-id-lookup-value-description",
            name = "config-group-id-lookup-value-name",
            required = false
    )
    String groupIdLookupValue();

    @Meta.AD(
            deflt = UserGroupRolesUpdaterConstants.CONFIG_GROUP_ID_LOOKUP_VALUE_TYPE_DEFAULT,
            description = "config-group-id-lookup-value-type-description",
            name = "config-group-id-lookup-value-type-name",
            required = false
    )
    String groupIdLookupValueType();

    @Meta.AD(
            deflt = UserGroupRolesUpdaterConstants.CONFIG_GROUP_ID_TYPE_DEFAULT,
            description = "config-group-id-type-description",
            name = "config-group-id-type-name",
            required = false
    )
    String groupIdType();

    @Meta.AD(
            deflt = UserGroupRolesUpdaterConstants.CONFIG_USE_IN_CONTEXT_USER_DEFAULT,
            description = "config-use-in-context-user-description",
            name = "config-use-in-context-user-name",
            required = false
    )
    boolean useInContextUser();

    @Meta.AD(
            deflt = UserGroupRolesUpdaterConstants.CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_USER_LOOKUP_VALUE_DEFAULT,
            description = "config-use-workflow-context-key-for-user-lookup-value-description",
            name = "config-use-workflow-context-key-for-user-lookup-value-name",
            required = false
    )
    boolean useWorkflowContextKeyForUserLookupValue();

    @Meta.AD(
            deflt = UserGroupRolesUpdaterConstants.CONFIG_USER_LOOKUP_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-user-lookup-value-workflow-context-key-description",
            name = "config-user-lookup-value-workflow-context-key-name",
            required = false
    )
    String userLookupValueWorkflowContextKey();

    @Meta.AD(
            deflt = UserGroupRolesUpdaterConstants.CONFIG_USER_LOOKUP_VALUE_DEFAULT,
            description = "config-user-lookup-value-description",
            name = "config-user-lookup-value-name",
            required = false
    )
    String userLookupValue();

    @Meta.AD(
            deflt = UserGroupRolesUpdaterConstants.CONFIG_USER_LOOKUP_TYPE_DEFAULT,
            description = "config-user-lookup-type-description",
            name = "config-user-lookup-type-name",
            required = false
    )
    String userLookupType();

    @Meta.AD(
            deflt = UserGroupRolesUpdaterConstants.CONFIG_ROLE_ARRAY_DEFAULT,
            description = "config-role-array-description",
            name = "config-role-array-name",
            required = false
    )
    String[] roles();
    //@formatter:on
}
