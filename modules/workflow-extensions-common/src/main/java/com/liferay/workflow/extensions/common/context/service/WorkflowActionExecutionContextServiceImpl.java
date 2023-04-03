package com.liferay.workflow.extensions.common.context.service;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinition;
import com.liferay.portal.workflow.kaleo.service.KaleoDefinitionLocalService;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Locale;

@Component(
        service = WorkflowActionExecutionContextService.class
)
public class WorkflowActionExecutionContextServiceImpl implements WorkflowActionExecutionContextService {

    @Reference
    private KaleoDefinitionLocalService kaleoDefinitionLocalService;

    public WorkflowActionExecutionContext buildWorkflowActionExecutionContext(final KaleoAction kaleoAction, final ServiceContext serviceContext) {
        final Locale serviceContextLocale = serviceContext.getLocale();
        return buildWorkflowActionExecutionContext(kaleoAction, serviceContextLocale);
    }

    public WorkflowActionExecutionContext buildWorkflowActionExecutionContext(final KaleoAction kaleoAction, final Locale locale) {
        final long kaleoDefinitionId = kaleoAction.getKaleoDefinitionId();
        final String nodeName = kaleoAction.getKaleoNodeName();
        final String nodeDescription = kaleoAction.getDescription();
        final String actionName = kaleoAction.getName();
        final KaleoDefinition kaleoDefinition = kaleoDefinitionLocalService.fetchKaleoDefinition(kaleoDefinitionId);
        return kaleoDefinition != null
                ? new WorkflowActionExecutionContext(kaleoDefinition.getName(), kaleoDefinition.getTitle(locale), nodeName, nodeDescription, actionName)
                : new WorkflowActionExecutionContext(null, null, nodeName, nodeDescription, actionName);
    }
}
