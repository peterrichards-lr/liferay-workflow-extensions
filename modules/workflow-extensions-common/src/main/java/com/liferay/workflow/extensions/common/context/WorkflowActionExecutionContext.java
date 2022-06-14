package com.liferay.workflow.extensions.common.context;

public class WorkflowActionExecutionContext extends WorkflowExecutionContext {
    private final String actionName;

    public WorkflowActionExecutionContext(String workflowName, String workflowTitle, String nodeName, String nodeDescription, String actionName) {
        super(workflowName, workflowTitle, nodeName, nodeDescription);
        this.actionName = actionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkflowActionExecutionContext)) return false;
        if (!super.equals(o)) return false;

        WorkflowActionExecutionContext that = (WorkflowActionExecutionContext) o;

        return getActionName() != null ? getActionName().equals(that.getActionName()) : that.getActionName() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getActionName() != null ? getActionName().hashCode() : 0);
        return result;
    }

    public String getActionName() {
        return actionName;
    }

    @Override
    public String toString() {
        return "WorkflowActionExecutionContext{" +
                "actionName='" + actionName + '\'' +
                ", workflowName='" + workflowName + '\'' +
                ", workflowTitle='" + workflowTitle + '\'' +
                ", nodeName='" + nodeName + '\'' +
                '}';
    }
}
