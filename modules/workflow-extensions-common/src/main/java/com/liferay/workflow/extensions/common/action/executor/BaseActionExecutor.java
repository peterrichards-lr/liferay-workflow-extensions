package com.liferay.workflow.extensions.common.action.executor;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinition;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.portal.workflow.kaleo.service.KaleoDefinitionLocalService;
import com.liferay.workflow.extensions.common.context.WorkflowExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public abstract class BaseActionExecutor implements ActionExecutor {
    protected final Logger _log = LoggerFactory.getLogger(getClass());
    private WorkflowExecutionContext workflowExecutionContext;

    protected abstract WorkflowStatusManager getWorkflowStatusManager();

    protected abstract KaleoDefinitionLocalService getKaleoDefinitionLocalService();

    protected final String normaliseValue(String value) {
        if (value == null || "".equals(value)) {
            return value;
        }
        return value.replaceAll("\\[\"", "").replaceAll("\"]", "");
    }

    protected final void updateWorkflowStatus(final int status, final Map<String, Serializable> workflowContext) throws ActionExecutorException {
        try {
            if (status > -1) {
                if (_log.isDebugEnabled()) {
                    final String workflowLabelStatus = WorkflowConstants.getStatusLabel(status);
                    _log.debug("Setting workflow status to {} [{}]", workflowLabelStatus, status);
                }
                getWorkflowStatusManager().updateStatus(status, workflowContext);
            }
        } catch (WorkflowException e) {
            throw new ActionExecutorException("Unable to update workflow status", e);
        }
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

    protected final boolean isNodeName(final String nodeName) {
        if (workflowExecutionContext == null) {
            return false;
        }
        return StringUtil.equals(workflowExecutionContext.getNodeName(), nodeName);
    }

    protected final boolean isActionName(final String actionName) {
        if (workflowExecutionContext == null) {
            return false;
        }
        return StringUtil.equals(workflowExecutionContext.getActionName(), actionName);
    }

    protected final boolean isWorkflowNode(final String workflowName, final String nodeName) {
        if (workflowExecutionContext == null) {
            return false;
        }
        return StringUtil.equals(workflowExecutionContext.getWorkflowName(), workflowName) &&
                StringUtil.equals(workflowExecutionContext.getNodeName(), nodeName);
    }

    protected final boolean isWorkflowAction(final String workflowName, final String nodeName, final String actionName) {
        if (workflowExecutionContext == null) {
            return false;
        }
        return StringUtil.equals(workflowExecutionContext.getWorkflowName(), workflowName) &&
                StringUtil.equals(workflowExecutionContext.getNodeName(), nodeName) &&
                StringUtil.equals(workflowExecutionContext.getActionName(), actionName);
    }

    private void configureWorkflowExecutionContext(KaleoAction kaleoAction, ServiceContext serviceContext) throws ActionExecutorException {
        final Locale serviceContextLocale = serviceContext.getLocale();
        configureWorkflowExecutionContext(kaleoAction, serviceContextLocale);
    }

    private void configureWorkflowExecutionContext(KaleoAction kaleoAction, Locale locale) throws ActionExecutorException {
        if (workflowExecutionContext != null) {
            throw new ActionExecutorException("The WorkflowExecutionContext has already been configured");
        }
        final long kaleoDefinitionId = kaleoAction.getKaleoDefinitionId();
        final KaleoDefinition kaleoDefinition = getKaleoDefinitionLocalService().fetchKaleoDefinition(kaleoDefinitionId);
        workflowExecutionContext = kaleoDefinition != null
                ? new WorkflowExecutionContext(kaleoDefinition.getName(), kaleoDefinition.getTitle(locale), kaleoAction.getName(), kaleoAction.getKaleoNodeName())
                : new WorkflowExecutionContext(null, null, kaleoAction.getName(), kaleoAction.getKaleoNodeName());
    }

    @Override
    public final void execute(KaleoAction kaleoAction, ExecutionContext executionContext) throws ActionExecutorException {
        final ServiceContext serviceContext = executionContext.getServiceContext();
        configureWorkflowExecutionContext(kaleoAction, serviceContext);
        execute(kaleoAction, executionContext, workflowExecutionContext);
    }

    public abstract void execute(KaleoAction kaleoAction, ExecutionContext executionContext, WorkflowExecutionContext workflowExecutionContext) throws ActionExecutorException;
}
