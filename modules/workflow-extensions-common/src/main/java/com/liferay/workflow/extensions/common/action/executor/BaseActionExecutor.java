package com.liferay.workflow.extensions.common.action.executor;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.workflow.extensions.common.BaseNode;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;

import java.util.Locale;

@SuppressWarnings("unused")
public abstract class BaseActionExecutor extends BaseNode<WorkflowActionExecutionContext> implements ActionExecutor {

    @Override
    public final void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext) {
        final ServiceContext serviceContext = executionContext.getServiceContext();
        configureWorkflowExecutionContext(kaleoAction, serviceContext);
        execute(kaleoAction, executionContext, getWorkflowExecutionContext());
    }

    private void configureWorkflowExecutionContext(final KaleoAction kaleoAction, final ServiceContext serviceContext) {
        final Locale serviceContextLocale = serviceContext.getLocale();
        final WorkflowActionExecutionContext executionContext = getWorkflowActionExecutionContextService().buildWorkflowActionExecutionContext(kaleoAction, serviceContextLocale);
        setWorkflowExecutionContext(executionContext);
    }

    // This method is needed post U63
    //@Override
    @SuppressWarnings({"unused", "SameReturnValue"})
    public String[] getActionExecutorLanguages() {
        return WorkflowExtensionsConstants.ACTION_EXECUTOR_KEYS;
    }

    protected abstract WorkflowActionExecutionContextService getWorkflowActionExecutionContextService();

    protected abstract void execute(KaleoAction kaleoAction, ExecutionContext executionContext, WorkflowActionExecutionContext workflowExecutionContext);
}
