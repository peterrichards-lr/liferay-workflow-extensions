package com.liferay.workflow.extensions.common.configuration;

public interface BaseActionExecutorConfiguration extends BaseConfiguration {
    boolean updateWorkflowStatusOnSuccess();

    String successWorkflowStatus();

    boolean updateWorkflowStatusOnException();

    String exceptionWorkflowStatus();

}
