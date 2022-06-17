package com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.constants.DDMFormActionOutcomeEvaluatorConstants;
import com.liferay.workflow.extensions.common.configuration.BaseConditionEvaluatorConfiguration;
import com.liferay.workflow.extensions.common.configuration.BaseConfiguration;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;

@ExtendedObjectClassDefinition(
        category = "workflow", scope = ExtendedObjectClassDefinition.Scope.GROUP,
        factoryInstanceLabelAttribute = WorkflowExtensionsConstants.CONFIG_FORM_INSTANCE_ID
)
@Meta.OCD(
        factory = true,
        id = DDMFormActionOutcomeEvaluatorConfiguration.PID,
        localization = "content/Language", name = "config-ddm-form-action-outcome-evaluator-name",
        description = "config-ddm-form-action-outcome-evaluator-description"
)
public interface DDMFormActionOutcomeEvaluatorConfiguration extends BaseConfiguration, BaseConditionEvaluatorConfiguration {
    String PID = "com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.configuration.DDMFormActionOutcomeEvaluatorConfiguration";

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_FORM_INSTANCE_ID_DEFAULT,
            description = "config-ddm-form-instance-identifier-description",
            id = WorkflowExtensionsConstants.CONFIG_FORM_INSTANCE_ID,
            name = "config-ddm-form-instance-identifier-name",
            required = false
    )
    String identifier();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_ENABLE_DEFAULT,
            description = "config-enable-description",
            name = "config-enable-name",
            required = false
    )
    boolean enable();

    @Meta.AD(
            deflt = DDMFormActionOutcomeEvaluatorConstants.CONFIG_SUCCESS_OUTCOME_TRANSITION_NAME_DEFAULT,
            description = "config-success-workflow-status-transition-name-description",
            name = "config-success-workflow-status-transition-name-name",
            required = false
    )
    String successOutcomeTransitionName();

    @Meta.AD(
            deflt = DDMFormActionOutcomeEvaluatorConstants.CONFIG_FAILURE_OUTCOME_TRANSITION_NAME_DEFAULT,
            description = "config-failure-workflow-status-transition-name-description",
            name = "config-failure-workflow-status-transition-name-name",
            required = false
    )
    String failureOutcomeTransitionName();

    @Meta.AD(
            deflt = DDMFormActionOutcomeEvaluatorConstants.CONFIG_FAILURE_STATUS_ARRAY_DEFAULT,
            description = "config-ddm-form-failure-status-array-description",
            name = "config-ddm-form-failure-status-array-name",
            required = false
    )
    String[] failureStatusArray();
}
