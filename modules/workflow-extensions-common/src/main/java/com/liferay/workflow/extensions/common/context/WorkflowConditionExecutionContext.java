package com.liferay.workflow.extensions.common.context;

public class WorkflowConditionExecutionContext extends WorkflowExecutionContext {

    private String nodeLabel;

    public WorkflowConditionExecutionContext(String workflowName, String workflowTitle, String nodeName, String nodeDescription) {
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
