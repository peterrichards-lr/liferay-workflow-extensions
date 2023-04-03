package com.liferay.workflow.extensions.common.condition.evaluator;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.model.KaleoCondition;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.condition.ConditionEvaluator;
import com.liferay.workflow.extensions.common.BaseNode;
import com.liferay.workflow.extensions.common.context.WorkflowConditionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowConditionExecutionContextService;

import java.util.Locale;

@SuppressWarnings("unused")
public abstract class BaseConditionEvaluator extends BaseNode<WorkflowConditionExecutionContext> implements ConditionEvaluator {

    @Override
    public String evaluate(
            final KaleoCondition kaleoCondition, final ExecutionContext executionContext) {
        final ServiceContext serviceContext = executionContext.getServiceContext();
        configureWorkflowExecutionContext(kaleoCondition, serviceContext);
        return evaluate(kaleoCondition, executionContext, getWorkflowExecutionContext());
    }

    private void configureWorkflowExecutionContext(final KaleoCondition kaleoCondition, final ServiceContext serviceContext) {
        final Locale serviceContextLocale = serviceContext.getLocale();
        final WorkflowConditionExecutionContext executionContext = getWorkflowConditionExecutionContextService().buildWorkflowConditionExecutionContext(kaleoCondition, serviceContextLocale);
        setWorkflowExecutionContext(executionContext);
    }

    protected abstract String evaluate(KaleoCondition kaleoCondition, ExecutionContext executionContext, WorkflowConditionExecutionContext workflowExecutionContext);

    protected abstract WorkflowConditionExecutionContextService getWorkflowConditionExecutionContextService();
}
