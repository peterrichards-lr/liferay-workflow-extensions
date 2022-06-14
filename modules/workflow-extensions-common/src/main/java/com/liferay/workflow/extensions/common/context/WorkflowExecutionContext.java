package com.liferay.workflow.extensions.common.context;

public abstract class WorkflowExecutionContext {
    protected final String workflowName;
    protected final String workflowTitle;
    protected final String nodeName;
    protected final String nodeDescription;

    public WorkflowExecutionContext(String workflowName, String workflowTitle, String nodeName, String nodeDescription) {
        this.workflowName = workflowName;
        this.workflowTitle = workflowTitle;
        this.nodeName = nodeName;
        this.nodeDescription = nodeDescription;
    }

    public String getNodeDescription() {
        return nodeDescription;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkflowExecutionContext)) return false;

        WorkflowExecutionContext that = (WorkflowExecutionContext) o;

        if (getWorkflowName() != null ? !getWorkflowName().equals(that.getWorkflowName()) : that.getWorkflowName() != null)
            return false;
        if (getWorkflowTitle() != null ? !getWorkflowTitle().equals(that.getWorkflowTitle()) : that.getWorkflowTitle() != null)
            return false;
        if (getNodeName() != null ? !getNodeName().equals(that.getNodeName()) : that.getNodeName() != null)
            return false;
        return getNodeDescription() != null ? getNodeDescription().equals(that.getNodeDescription()) : that.getNodeDescription() == null;
    }

    @Override
    public int hashCode() {
        int result = getWorkflowName() != null ? getWorkflowName().hashCode() : 0;
        result = 31 * result + (getWorkflowTitle() != null ? getWorkflowTitle().hashCode() : 0);
        result = 31 * result + (getNodeName() != null ? getNodeName().hashCode() : 0);
        result = 31 * result + (getNodeDescription() != null ? getNodeDescription().hashCode() : 0);
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
}
