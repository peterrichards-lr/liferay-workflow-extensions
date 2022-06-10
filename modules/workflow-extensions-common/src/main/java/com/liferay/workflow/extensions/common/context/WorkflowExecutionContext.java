package com.liferay.workflow.extensions.common.context;

import com.liferay.workflow.extensions.common.configuration.constants.WorkflowExtensionsConstants;

public class WorkflowExecutionContext {
    private final String workflowName;
    private final String workflowTitle;
    private final String nodeName;
    private final String actionName;

    public WorkflowExecutionContext(String workflowName, String workflowTitle, String nodeName, String actionName) {
        this.workflowName = workflowName;
        this.workflowTitle = workflowTitle;
        this.nodeName = nodeName;
        this.actionName = actionName;
    }

    @Override
    public String toString() {
        return "WorkflowExecutionContext{" +
                "workflowName='" +
                workflowName + '\'' +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "workflowTitle='" + workflowTitle + '\'' +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "nodeName='" + nodeName + '\'' +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "actionName='" + actionName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkflowExecutionContext that = (WorkflowExecutionContext) o;

        if (getWorkflowName() != null ? !getWorkflowName().equals(that.getWorkflowName()) : that.getWorkflowName() != null)
            return false;
        if (getWorkflowTitle() != null ? !getWorkflowTitle().equals(that.getWorkflowTitle()) : that.getWorkflowTitle() != null)
            return false;
        if (getNodeName() != null ? !getNodeName().equals(that.getNodeName()) : that.getNodeName() != null)
            return false;
        return getActionName() != null ? getActionName().equals(that.getActionName()) : that.getActionName() == null;
    }

    @Override
    public int hashCode() {
        int result = getWorkflowName() != null ? getWorkflowName().hashCode() : 0;
        result = 31 * result + (getWorkflowTitle() != null ? getWorkflowTitle().hashCode() : 0);
        result = 31 * result + (getNodeName() != null ? getNodeName().hashCode() : 0);
        result = 31 * result + (getActionName() != null ? getActionName().hashCode() : 0);
        return result;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public String getWorkflowTitle() {
        return workflowTitle;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getActionName() {
        return actionName;
    }
}
