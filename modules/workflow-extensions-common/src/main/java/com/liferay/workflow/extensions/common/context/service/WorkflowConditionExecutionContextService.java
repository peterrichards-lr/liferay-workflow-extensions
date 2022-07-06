package com.liferay.workflow.extensions.common.context.service;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.model.KaleoCondition;
import com.liferay.workflow.extensions.common.context.WorkflowConditionExecutionContext;

import java.util.Locale;

@SuppressWarnings("unused")
public interface WorkflowConditionExecutionContextService {
    WorkflowConditionExecutionContext buildWorkflowConditionExecutionContext(final KaleoCondition kaleoCondition, final ServiceContext serviceContext);

    WorkflowConditionExecutionContext buildWorkflowConditionExecutionContext(final KaleoCondition kaleoCondition, final Locale locale);
}
