package com.liferay.workflow.extensions.common.configuration;

@SuppressWarnings("EmptyMethod")
public interface BaseActionExecutorConfiguration extends BaseConfiguration {
    boolean updateWorkflowStatusOnSuccess();

    String successWorkflowStatus();

    boolean updateWorkflowStatusOnException();

    String exceptionWorkflowStatus();

}
