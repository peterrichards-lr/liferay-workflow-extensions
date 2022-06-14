package com.liferay.workflow.extensions.common.condition.evaluator;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.model.KaleoCondition;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.condition.ConditionEvaluator;
import com.liferay.workflow.extensions.common.BaseNode;
import com.liferay.workflow.extensions.common.context.WorkflowConditionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowConditionExecutionContextService;

import java.util.Locale;

public abstract class BaseConditionEvaluator extends BaseNode<WorkflowConditionExecutionContext> implements ConditionEvaluator {

    @Override
    public String evaluate(
            KaleoCondition kaleoCondition, ExecutionContext executionContext)
            throws PortalException {
        final ServiceContext serviceContext = executionContext.getServiceContext();
        configureWorkflowExecutionContext(kaleoCondition, serviceContext);
        return evaluate(kaleoCondition, executionContext, getWorkflowExecutionContext());
    }

    protected abstract String evaluate(KaleoCondition kaleoCondition, ExecutionContext executionContext, WorkflowConditionExecutionContext workflowExecutionContext);

    private void configureWorkflowExecutionContext(KaleoCondition kaleoCondition, ServiceContext serviceContext) {
        final Locale serviceContextLocale = serviceContext.getLocale();
        final WorkflowConditionExecutionContext executionContext = getWorkflowConditionExecutionContextService().buildWorkflowConditionExecutionContext(kaleoCondition, serviceContextLocale);
        setWorkflowExecutionContext(executionContext);
    }

    protected abstract WorkflowConditionExecutionContextService getWorkflowConditionExecutionContextService();
}
