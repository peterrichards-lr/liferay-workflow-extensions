package com.liferay.workflow.extensions.common.configuration;

@SuppressWarnings("EmptyMethod")
public interface BaseEntityCreatorActionExecutorConfiguration extends BaseUserActionExecutorConfiguration {
    String[] entityCreationAttributes();

    String createdEntityIdentifierWorkflowContextKey();
}
