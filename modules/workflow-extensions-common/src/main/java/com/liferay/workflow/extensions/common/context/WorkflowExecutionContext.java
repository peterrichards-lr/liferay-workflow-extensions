package com.liferay.workflow.extensions.common.context;

public abstract class WorkflowExecutionContext {
    protected final String nodeDescription;
    protected final String nodeName;
    protected final String workflowName;
    protected final String workflowTitle;

    public WorkflowExecutionContext(final String workflowName, final String workflowTitle, final String nodeName, final String nodeDescription) {
        this.workflowName = workflowName;
        this.workflowTitle = workflowTitle;
        this.nodeName = nodeName;
        this.nodeDescription = nodeDescription;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkflowExecutionContext)) return false;
        final WorkflowExecutionContext that = (WorkflowExecutionContext) o;
        if (getWorkflowName() != null ? !getWorkflowName().equals(that.getWorkflowName()) : that.getWorkflowName() != null)
            return false;
        if (getWorkflowTitle() != null ? !getWorkflowTitle().equals(that.getWorkflowTitle()) : that.getWorkflowTitle() != null)
            return false;
        if (getNodeName() != null ? !getNodeName().equals(that.getNodeName()) : that.getNodeName() != null)
            return false;
        return getNodeDescription() != null ? getNodeDescription().equals(that.getNodeDescription()) : that.getNodeDescription() == null;
    }

    public String getNodeDescription() {
        return nodeDescription;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public String getWorkflowTitle() {
        return workflowTitle;
    }

    @Override
    public int hashCode() {
        int result = getWorkflowName() != null ? getWorkflowName().hashCode() : 0;
        result = 31 * result + (getWorkflowTitle() != null ? getWorkflowTitle().hashCode() : 0);
        result = 31 * result + (getNodeName() != null ? getNodeName().hashCode() : 0);
        result = 31 * result + (getNodeDescription() != null ? getNodeDescription().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WorkflowExecutionContext{" +
                "workflowName='" + workflowName + '\'' +
                ", workflowTitle='" + workflowTitle + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", nodeDescription='" + nodeDescription + '\'' +
                '}';
    }
}
