package com.liferay.workflow.extensions.common.context.service;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.model.KaleoCondition;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinition;
import com.liferay.portal.workflow.kaleo.model.KaleoNode;
import com.liferay.portal.workflow.kaleo.service.KaleoDefinitionLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoNodeLocalService;
import com.liferay.workflow.extensions.common.context.WorkflowConditionExecutionContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Locale;

@Component(
        service = WorkflowConditionExecutionContextService.class
)
public class WorkflowConditionExecutionContextServiceImpl implements WorkflowConditionExecutionContextService {
    @Reference
    private KaleoDefinitionLocalService kaleoDefinitionLocalService;
    @Reference
    private KaleoNodeLocalService kaleoNodeLocalService;

    public WorkflowConditionExecutionContext buildWorkflowConditionExecutionContext(final KaleoCondition kaleoCondition, final ServiceContext serviceContext) {
        final Locale serviceContextLocale = serviceContext.getLocale();
        return buildWorkflowConditionExecutionContext(kaleoCondition, serviceContextLocale);
    }

    public WorkflowConditionExecutionContext buildWorkflowConditionExecutionContext(final KaleoCondition kaleoCondition, final Locale locale) {
        final long kaleoDefinitionId = kaleoCondition.getKaleoDefinitionId();
        final long kaleoNodeId = kaleoCondition.getKaleoNodeId();
        final KaleoDefinition kaleoDefinition = kaleoDefinitionLocalService.fetchKaleoDefinition(kaleoDefinitionId);
        final KaleoNode kaleoNode = kaleoNodeLocalService.fetchKaleoNode(kaleoNodeId);
        final String nodeName = kaleoNode.getName();
        final String nodeDescription = kaleoNode.getDescription();
        return kaleoDefinition != null
                ? new WorkflowConditionExecutionContext(kaleoDefinition.getName(), kaleoDefinition.getTitle(locale), nodeName, nodeDescription)
                : new WorkflowConditionExecutionContext(null, null, nodeName, nodeDescription);

    }
}
