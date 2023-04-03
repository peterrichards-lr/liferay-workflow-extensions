package com.liferay.workflow.extensions.common.context;

public class WorkflowActionExecutionContext extends WorkflowExecutionContext {
    private final String actionName;

    public WorkflowActionExecutionContext(final String workflowName, final String workflowTitle, final String nodeName, final String nodeDescription, final String actionName) {
        super(workflowName, workflowTitle, nodeName, nodeDescription);
        this.actionName = actionName;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getActionName() != null ? getActionName().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkflowActionExecutionContext)) return false;
        if (!super.equals(o)) return false;
        final WorkflowActionExecutionContext that = (WorkflowActionExecutionContext) o;
        return getActionName() != null ? getActionName().equals(that.getActionName()) : that.getActionName() == null;
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

    public String getActionName() {
        return actionName;
    }
}
