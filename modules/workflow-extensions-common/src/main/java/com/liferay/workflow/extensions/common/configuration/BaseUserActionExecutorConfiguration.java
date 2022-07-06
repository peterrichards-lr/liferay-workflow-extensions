package com.liferay.workflow.extensions.common.configuration;

@SuppressWarnings("EmptyMethod")
public interface BaseUserActionExecutorConfiguration extends BaseActionExecutorConfiguration {
    boolean useInContextUserForAction();

    boolean useWorkflowContextKeyForActionUserLookupValue();

    String actionUserLookupValueWorkflowContextKey();

    String actionUserLookupValue();

    String actionUserLookupType();
}
