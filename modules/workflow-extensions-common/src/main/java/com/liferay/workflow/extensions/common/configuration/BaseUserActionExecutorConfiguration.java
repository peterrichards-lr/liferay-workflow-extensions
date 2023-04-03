package com.liferay.workflow.extensions.common.configuration;

@SuppressWarnings("EmptyMethod")
public interface BaseUserActionExecutorConfiguration extends BaseActionExecutorConfiguration {
    String actionUserLookupType();

    String actionUserLookupValue();

    String actionUserLookupValueWorkflowContextKey();

    boolean useInContextUserForAction();

    boolean useWorkflowContextKeyForActionUserLookupValue();
}
