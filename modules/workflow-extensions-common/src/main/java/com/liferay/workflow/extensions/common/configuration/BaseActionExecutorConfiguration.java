package com.liferay.workflow.extensions.common.configuration;

@SuppressWarnings("EmptyMethod")
public interface BaseActionExecutorConfiguration extends BaseConfiguration {
    String exceptionWorkflowStatus();

    String successWorkflowStatus();

    boolean updateWorkflowStatusOnException();

    boolean updateWorkflowStatusOnSuccess();

}
