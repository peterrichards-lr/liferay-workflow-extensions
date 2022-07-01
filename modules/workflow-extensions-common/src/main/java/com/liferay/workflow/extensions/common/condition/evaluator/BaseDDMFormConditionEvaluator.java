package com.liferay.workflow.extensions.common.condition.evaluator;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.workflow.kaleo.model.KaleoCondition;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.condition.ConditionEvaluator;
import com.liferay.workflow.extensions.common.BaseConfigurableNode;
import com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper;
import com.liferay.workflow.extensions.common.configuration.BaseDDMFormConditionEvaluatorConfiguration;
import com.liferay.workflow.extensions.common.context.WorkflowConditionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowConditionExecutionContextService;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;
import com.liferay.workflow.extensions.common.util.DDMFormUtil;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public abstract class BaseDDMFormConditionEvaluator<C extends BaseDDMFormConditionEvaluatorConfiguration, W extends BaseConfigurationWrapper<C>, S extends SettingsHelper<C, W>> extends BaseConfigurableNode<C, W, S, WorkflowConditionExecutionContext> implements ConditionEvaluator {

    @Override
    public final String evaluate(
            KaleoCondition kaleoCondition, ExecutionContext executionContext)
            throws PortalException {
        final ServiceContext serviceContext = executionContext.getServiceContext();
        configureWorkflowExecutionContext(kaleoCondition, serviceContext);

        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();

        if (!DDMFormUtil.isDDMFormEntryClass(workflowContext)) {
            _log.debug("Entry class is not the correct type");
            return "";
        }

        final long formInstanceRecordVersionId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));
        final DDMFormInstance formInstance = DDMFormUtil.getDDMFormInstance(formInstanceRecordVersionId);
        final long formInstanceId = formInstance.getFormInstanceId();
        final W configuration = getConfigurationWrapper(String.valueOf(formInstanceId));

        if (!configuration.isEnabled()) {
            _log.debug("Configuration is disabled : {}", formInstanceId);
            return "";
        }

        return evaluate(kaleoCondition, executionContext, getWorkflowExecutionContext(), configuration, formInstanceRecordVersionId);
    }

    private void configureWorkflowExecutionContext(KaleoCondition kaleoCondition, ServiceContext serviceContext) {
        final Locale serviceContextLocale = serviceContext.getLocale();
        final WorkflowConditionExecutionContext executionContext = getWorkflowConditionExecutionContextService().buildWorkflowConditionExecutionContext(kaleoCondition, serviceContextLocale);
        setWorkflowExecutionContext(executionContext);
    }

    protected abstract WorkflowConditionExecutionContextService getWorkflowConditionExecutionContextService();


    protected abstract String evaluate(final KaleoCondition kaleoCondition, final ExecutionContext executionContext, final WorkflowConditionExecutionContext workflowExecutionContext, final W configuration, long formInstanceRecordVersionId) throws PortalException;
}
