package com.liferay.workflow.extensions.common;

import com.liferay.portal.kernel.util.StringUtil;
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

    protected final boolean isWorkflowName(final String workflowName) {
        if (workflowExecutionContext == null) {
            return false;
        }
        return StringUtil.equals(workflowExecutionContext.getWorkflowName(), workflowName);
    }

    protected final boolean isWorkflowTitle(final String workflowTitle) {
        if (workflowExecutionContext == null) {
            return false;
        }
        return StringUtil.equals(workflowExecutionContext.getWorkflowTitle(), workflowTitle);
    }

    protected final boolean isWorkflowNode(final String workflowName, final String nodeName) {
        if (workflowExecutionContext == null) {
            return false;
        }
        return StringUtil.equals(workflowExecutionContext.getWorkflowName(), workflowName) &&
                StringUtil.equals(workflowExecutionContext.getNodeName(), nodeName);
    }

    protected final boolean isNodeName(final String nodeName) {
        if (workflowExecutionContext == null) {
            return false;
        }
        return StringUtil.equals(workflowExecutionContext.getNodeName(), nodeName);
    }
}
