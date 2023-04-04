package com.liferay.workflow.extensions.common.configuration;

public interface UserLookupConfiguration {
    String getIdentifier();

    boolean isInContextUserRequired();

    boolean isWorkflowContextKeyUsedForUserLookup();

    String getUserLookupValueWorkflowContextKey();

    String getUserLookupValue();

    String getUserLookupType();
}
