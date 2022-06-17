package com.liferay.workflow.extensions.common.action.executor;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.BaseNode;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;

import java.util.Locale;

public abstract class BaseActionExecutor extends BaseNode<WorkflowActionExecutionContext> implements ActionExecutor {

    @Override
    public final void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext) throws ActionExecutorException {
        final ServiceContext serviceContext = executionContext.getServiceContext();
        try {
            configureWorkflowExecutionContext(kaleoAction, serviceContext);
        } catch (PortalException e) {
            throw new ActionExecutorException("Failed to configure WorkflowExecutionContext", e);
        }
        execute(kaleoAction, executionContext, getWorkflowExecutionContext());
    }

    private void configureWorkflowExecutionContext(KaleoAction kaleoAction, ServiceContext serviceContext) throws ActionExecutorException {
        final Locale serviceContextLocale = serviceContext.getLocale();
        final WorkflowActionExecutionContext executionContext = getWorkflowActionExecutionContextService().buildWorkflowActionExecutionContext(kaleoAction, serviceContextLocale);
        setWorkflowExecutionContext(executionContext);
    }

    protected abstract WorkflowActionExecutionContextService getWorkflowActionExecutionContextService();

    protected abstract void execute(KaleoAction kaleoAction, ExecutionContext executionContext, WorkflowActionExecutionContext workflowExecutionContext) throws ActionExecutorException;
}
