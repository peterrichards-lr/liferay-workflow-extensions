package com.liferay.workflow.extensions.common.configuration;

public class BaseUserActionExecutorConfigurationWrapper<T extends BaseUserActionExecutorConfiguration> extends BaseActionExecutorConfigurationWrapper<T> {

    public boolean isInContextUserRequiredForAction() {
        return getConfiguration().useInContextUserForAction();
    }

    public boolean isWorkflowContextKeyUsedForActionUserLookup() {
        return getConfiguration().useWorkflowContextKeyForActionUserLookupValue();
    }

    public String getActionUserLookupValueWorkflowContextKey() {
        return getConfiguration().actionUserLookupValueWorkflowContextKey();
    }

    public String getActionUserLookupValue() {
        return getConfiguration().actionUserLookupValue();
    }

    public String getActionUserLookupType() {
        return getConfiguration().actionUserLookupType();
    }
}
