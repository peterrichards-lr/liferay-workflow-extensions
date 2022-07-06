package com.liferay.workflow.extensions.common.context;

public class WorkflowConditionExecutionContext extends WorkflowExecutionContext {

    public WorkflowConditionExecutionContext(final String workflowName, final String workflowTitle, final String nodeName, final String nodeDescription) {
        super(workflowName, workflowTitle, nodeName, nodeDescription);
    }

    @Override
    public String toString() {
        return "WorkflowConditionExecutionContext{" +
                "workflowName='" + workflowName + '\'' +
                ", workflowTitle='" + workflowTitle + '\'' +
                ", nodeName='" + nodeName + '\'' +
                '}';
    }
}
