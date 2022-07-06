package com.liferay.workflow.extensions.common;

import com.liferay.workflow.extensions.common.context.WorkflowExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseNode<T extends WorkflowExecutionContext> {
    protected final Logger _log = LoggerFactory.getLogger(getClass());
    private T workflowExecutionContext;

    protected T getWorkflowExecutionContext() {
        return workflowExecutionContext;
    }

    protected void setWorkflowExecutionContext(final T workflowExecutionContext) {
        this.workflowExecutionContext = workflowExecutionContext;
    }

}
