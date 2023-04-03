package com.liferay.workflow.extensions.common.configuration;

public class BaseUserActionExecutorConfigurationWrapper<T extends BaseUserActionExecutorConfiguration> extends BaseActionExecutorConfigurationWrapper<T> {

    public String getActionUserLookupType() {
        return getConfiguration().actionUserLookupType();
    }

    public String getActionUserLookupValue() {
        return getConfiguration().actionUserLookupValue();
    }

    public String getActionUserLookupValueWorkflowContextKey() {
        return getConfiguration().actionUserLookupValueWorkflowContextKey();
    }

    public boolean isInContextUserRequiredForAction() {
        return getConfiguration().useInContextUserForAction();
    }

    public boolean isWorkflowContextKeyUsedForActionUserLookup() {
        return getConfiguration().useWorkflowContextKeyForActionUserLookupValue();
    }
}
