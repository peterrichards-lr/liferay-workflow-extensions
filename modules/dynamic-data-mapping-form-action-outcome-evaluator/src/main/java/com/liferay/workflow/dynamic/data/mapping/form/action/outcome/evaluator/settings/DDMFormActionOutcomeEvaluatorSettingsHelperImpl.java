package com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.settings;

import com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.configuration.DDMFormActionOutcomeEvaluatorConfiguration;
import com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.configuration.DDMFormActionOutcomeEvaluatorConfigurationWrapper;
import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = DDMFormActionOutcomeEvaluatorSettingsHelper.class)
public class DDMFormActionOutcomeEvaluatorSettingsHelperImpl extends BaseSettingsHelper<DDMFormActionOutcomeEvaluatorConfiguration, DDMFormActionOutcomeEvaluatorConfigurationWrapper> implements DDMFormActionOutcomeEvaluatorSettingsHelper {

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addDDMFormActionOutcomeEvaluatorConfigurationWrapper(
            final DDMFormActionOutcomeEvaluatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a form action outcome evaluator configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    @SuppressWarnings("unused")
    protected void removeDDMFormActionOutcomeEvaluatorConfigurationWrapper(
            final DDMFormActionOutcomeEvaluatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a form action outcome evaluator configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
