package com.liferay.workflow.extensions.common.context.service;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;

import java.util.Locale;

public interface WorkflowActionExecutionContextService {
    WorkflowActionExecutionContext buildWorkflowActionExecutionContext(final KaleoAction kaleoAction, final ServiceContext serviceContext);

    WorkflowActionExecutionContext buildWorkflowActionExecutionContext(final KaleoAction kaleoAction, final Locale locale);
}
