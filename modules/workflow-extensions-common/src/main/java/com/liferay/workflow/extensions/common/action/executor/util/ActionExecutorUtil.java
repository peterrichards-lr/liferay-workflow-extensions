package com.liferay.workflow.extensions.common.action.executor.util;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;

public class ActionExecutorUtil {
    public static boolean isWorkflowAction(final WorkflowActionExecutionContext workflowExecutionContext, final String workflowName, final String nodeName, final String actionName) {
        if (workflowExecutionContext == null) {
            return false;
        }
        return StringUtil.equals(workflowExecutionContext.getWorkflowName(), workflowName) &&
                StringUtil.equals(workflowExecutionContext.getNodeName(), nodeName) &&
                StringUtil.equals(workflowExecutionContext.getActionName(), actionName);
    }

    public static boolean isActionName(final WorkflowActionExecutionContext workflowExecutionContext, final String actionName) {
        if (workflowExecutionContext == null) {
            return false;
        }
        return StringUtil.equals(workflowExecutionContext.getActionName(), actionName);
    }
}
