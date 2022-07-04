package com.liferay.workflow.extensions.common.condition.evaluator;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.workflow.kaleo.model.KaleoCondition;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.condition.ConditionEvaluator;
import com.liferay.workflow.extensions.common.configuration.BaseDDMFormConditionEvaluatorConfiguration;
import com.liferay.workflow.extensions.common.configuration.BaseDDMFormConditionEvaluatorConfigurationWrapper;
import com.liferay.workflow.extensions.common.context.WorkflowConditionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowConditionExecutionContextService;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;
import com.liferay.workflow.extensions.common.util.DDMFormUtil;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public abstract class BaseDDMFormConditionEvaluator<C extends BaseDDMFormConditionEvaluatorConfiguration, W extends BaseDDMFormConditionEvaluatorConfigurationWrapper<C>, S extends SettingsHelper<C, W>> extends BaseWorkflowConditionEvaluator<C, W, S> implements ConditionEvaluator {

    @Override
    public final String evaluate(
            final KaleoCondition kaleoCondition, final ExecutionContext executionContext, final WorkflowConditionExecutionContext workflowExecutionContext, final W configuration)
            throws PortalException {
        final ServiceContext serviceContext = executionContext.getServiceContext();
        configureWorkflowExecutionContext(kaleoCondition, serviceContext);

        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();

        if (!DDMFormUtil.isDDMFormEntryClass(workflowContext)) {
            _log.debug("Entry class is not the correct type");
            return "";
        }

        final long formInstanceRecordVersionId =
                configuration.isEntryClassPrimaryKeyUsed() ?
                        GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK)) :
                        getFormInstanceRecordVersionId(configuration, workflowContext);

        return evaluate(kaleoCondition, executionContext, getWorkflowExecutionContext(), configuration, formInstanceRecordVersionId);
    }

    private long getFormInstanceRecordVersionId(final W configuration, final Map<String, Serializable> workflowContext) {
        if (configuration.isWorkflowContextKeyUsedForFormInstanceRecordVersionId()) {
            final String workflowKey = configuration.getFormInstanceRecordVersionIdWorkflowContextKey();
            return workflowContext.containsKey(workflowKey) ?
                    GetterUtil.getLong(workflowContext.get(workflowKey)) :
                    configuration.getFormInstanceRecordVersionId();
        } else {
            return configuration.getFormInstanceRecordVersionId();
        }
    }

    private void configureWorkflowExecutionContext(KaleoCondition kaleoCondition, ServiceContext serviceContext) {
        final Locale serviceContextLocale = serviceContext.getLocale();
        final WorkflowConditionExecutionContext executionContext = getWorkflowConditionExecutionContextService().buildWorkflowConditionExecutionContext(kaleoCondition, serviceContextLocale);
        setWorkflowExecutionContext(executionContext);
    }

    protected abstract WorkflowConditionExecutionContextService getWorkflowConditionExecutionContextService();


    protected abstract String evaluate(final KaleoCondition kaleoCondition, final ExecutionContext executionContext, final WorkflowConditionExecutionContext workflowExecutionContext, final W configuration, long formInstanceRecordVersionId) throws PortalException;
}
