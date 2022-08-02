package com.liferay.workflow.commerce.orders.remapper.configuration;

import com.liferay.workflow.extensions.common.configuration.BaseUserActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.configuration.UserLookupConfiguration;

public class CommerceOrdersRemapperConfigurationWrapper extends BaseUserActionExecutorConfigurationWrapper<CommerceOrdersRemapperConfiguration> implements UserLookupConfiguration {
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
