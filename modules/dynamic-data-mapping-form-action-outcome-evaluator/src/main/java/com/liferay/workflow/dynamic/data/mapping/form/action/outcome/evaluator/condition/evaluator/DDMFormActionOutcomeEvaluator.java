package com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.condition.evaluator;


import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecordVersion;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.workflow.kaleo.model.KaleoCondition;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.condition.ConditionEvaluator;
import com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.configuration.DDMFormActionOutcomeEvaluatorConfiguration;
import com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.configuration.DDMFormActionOutcomeEvaluatorConfigurationWrapper;
import com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.constants.DDMFormActionOutcomeEvaluatorConstants;
import com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.settings.DDMFormActionOutcomeEvaluatorSettingsHelper;
import com.liferay.workflow.extensions.common.condition.evaluator.BaseDDMFormConditionEvaluator;
import com.liferay.workflow.extensions.common.context.WorkflowConditionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowConditionExecutionContextService;
import com.liferay.workflow.extensions.common.util.DDMFormUtil;
import com.liferay.workflow.extensions.common.util.StringUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;

@Component(
        property = "scripting.language=java",
        service = ConditionEvaluator.class
)
public class DDMFormActionOutcomeEvaluator extends BaseDDMFormConditionEvaluator<DDMFormActionOutcomeEvaluatorConfiguration, DDMFormActionOutcomeEvaluatorConfigurationWrapper, DDMFormActionOutcomeEvaluatorSettingsHelper> implements ConditionEvaluator {
    @Reference
    private WorkflowConditionExecutionContextService workflowConditionExecutionContextService;
    @Reference
    private DDMFormActionOutcomeEvaluatorSettingsHelper
            ddmFormActionOutcomeEvaluatorSettingsHelper;

    @Override
    protected WorkflowConditionExecutionContextService getWorkflowConditionExecutionContextService() {
        return workflowConditionExecutionContextService;
    }

    @Override
    protected DDMFormActionOutcomeEvaluatorSettingsHelper getSettingsHelper() {
        return ddmFormActionOutcomeEvaluatorSettingsHelper;
    }

    @Override
    protected String evaluate(final KaleoCondition kaleoCondition, final ExecutionContext executionContext, final WorkflowConditionExecutionContext workflowExecutionContext, final DDMFormActionOutcomeEvaluatorConfigurationWrapper configuration, final long formInstanceRecordVersionId) {
        _log.info(workflowExecutionContext.toString());
        final String successTransitionName = configuration.getSuccessOutcomeTransitionName();
        final String failureTransitionName = configuration.getFailureOutcomeTransitionName();

        try {
            final DDMFormInstanceRecordVersion recVer = DDMFormUtil.getDDMFormInstanceRecordVersion(formInstanceRecordVersionId);
            final int workflowStatus = recVer.getStatus();

            final List<Integer> workflowStatuses = configuration.getFailureStatuses();
            if (_log.isDebugEnabled()) {
                _log.debug("Failure status: {}", String.join(",", String.valueOf(configuration.getFailureStatusLabels())));
            }

            if (workflowStatuses.contains(workflowStatus)) {
                _log.debug("Form status is {} returning {}", workflowStatus, failureTransitionName);
                return failureTransitionName;
            } else {
                _log.debug("Form status is {} returning {}", workflowStatus, successTransitionName);
                return successTransitionName;
            }
        } catch (final PortalException | RuntimeException e) {
            _log.error("Unexpected exception. See inner exception for details", e);
            return StringUtil.isBlank(failureTransitionName) ? DDMFormActionOutcomeEvaluatorConstants.CONFIG_FAILURE_OUTCOME_TRANSITION_NAME_DEFAULT : failureTransitionName;
        }
    }
}
