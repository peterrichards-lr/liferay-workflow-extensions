package com.liferay.workflow.extensions.common.configuration;

public interface BaseEntityCreatorActionExecutorConfiguration extends BaseUserActionExecutorConfiguration {
    String[] entityCreationAttributes();

    String createdEntityIdentifierWorkflowContextKey();
}
