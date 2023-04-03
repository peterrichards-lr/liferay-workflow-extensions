package com.liferay.workflow.extensions.common.configuration;

public interface UserLookupConfiguration {
    String getIdentifier();

    String getUserLookupType();

    String getUserLookupValue();

    String getUserLookupValueWorkflowContextKey();

    boolean isInContextUserRequired();

    boolean isWorkflowContextKeyUsedForUserLookup();
}
