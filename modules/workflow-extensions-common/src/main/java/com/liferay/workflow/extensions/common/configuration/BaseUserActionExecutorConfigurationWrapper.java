package com.liferay.workflow.extensions.common.configuration;

public class BaseUserActionExecutorConfigurationWrapper<T extends BaseUserActionExecutorConfiguration> extends BaseActionExecutorConfigurationWrapper<T> {

    public boolean isInContextUserRequired() {
        return getConfiguration().useInContextUser();
    }

    public boolean isWorkflowContextKeyUsedForUserLookup() {
        return getConfiguration().useWorkflowContextKeyForUserLookupValue();
    }

    public String getUserLookupValueWorkflowContextKey() {
        return getConfiguration().userLookupValueWorkflowContextKey();
    }

    public String getUserLookupValue() {
        return getConfiguration().userLookupValue();
    }

    public String getUserLookupType() {
        return getConfiguration().userLookupType();
    }
}
