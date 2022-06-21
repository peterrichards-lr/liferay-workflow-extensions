package com.liferay.workflow.extensions.common.configuration;

public interface BaseUserActionExecutorConfiguration extends BaseActionExecutorConfiguration {
    boolean useInContextUser();

    boolean useWorkflowContextKeyForUserLookupValue();

    String userLookupValueWorkflowContextKey();

    String userLookupValue();

    String userLookupType();
}
