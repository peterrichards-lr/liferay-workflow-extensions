package com.liferay.workflow.commerce.orders.remapper.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.workflow.commerce.orders.remapper.constants.CommerceOrdersRemapperConstants;
import com.liferay.workflow.extensions.common.configuration.BaseUserActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;

@ExtendedObjectClassDefinition(
        category = "workflow", scope = ExtendedObjectClassDefinition.Scope.GROUP,
        factoryInstanceLabelAttribute = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID
)
@Meta.OCD(
        factory = true,
        id = CommerceOrdersRemapperConfiguration.PID,
        localization = "content/Language", name = "config-commerce-orders-remapper-name",
        description = "config-commerce-orders-remapper-description"
)
public interface CommerceOrdersRemapperConfiguration extends BaseUserActionExecutorConfiguration {
    public String PID = "com.liferay.workflow.commerce.orders.remapper.configuration.CommerceOrdersRemapperConfiguration";

    @Meta.AD(
            deflt = CommerceOrdersRemapperConstants.CONFIG_USE_IN_CONTEXT_USER_DEFAULT,
            description = "config-use-in-context-user-description",
            name = "config-use-in-context-user-name",
            required = false
    )
    boolean useInContextUser();

    @Meta.AD(
            deflt = CommerceOrdersRemapperConstants.CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_USER_LOOKUP_VALUE_DEFAULT,
            description = "config-use-workflow-context-key-for-user-lookup-value-description",
            name = "config-use-workflow-context-key-for-user-lookup-value-name",
            required = false
    )
    boolean useWorkflowContextKeyForUserLookupValue();

    @Meta.AD(
            deflt = CommerceOrdersRemapperConstants.CONFIG_USER_LOOKUP_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-user-lookup-value-workflow-context-key-description",
            name = "config-user-lookup-value-workflow-context-key-name",
            required = false
    )
    String userLookupValueWorkflowContextKey();

    @Meta.AD(
            deflt = CommerceOrdersRemapperConstants.CONFIG_USER_LOOKUP_VALUE_DEFAULT,
            description = "config-user-lookup-value-description",
            name = "config-user-lookup-value-name",
            required = false
    )
    String userLookupValue();

    @Meta.AD(
            deflt = CommerceOrdersRemapperConstants.CONFIG_USER_LOOKUP_TYPE_DEFAULT,
            description = "config-user-lookup-type-description",
            name = "config-user-lookup-type-name",
            required = false
    )
    String userLookupType();
}
