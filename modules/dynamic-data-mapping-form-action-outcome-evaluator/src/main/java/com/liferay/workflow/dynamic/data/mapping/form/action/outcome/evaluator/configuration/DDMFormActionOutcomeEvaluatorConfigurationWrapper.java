package com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.workflow.extensions.common.configuration.BaseFormConditionEvaluatorConfigurationWrapper;
import org.jsoup.helper.StringUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = DDMFormActionOutcomeEvaluatorConfiguration.PID,
        immediate = true, service = DDMFormActionOutcomeEvaluatorConfigurationWrapper.class
)
public class DDMFormActionOutcomeEvaluatorConfigurationWrapper extends BaseFormConditionEvaluatorConfigurationWrapper<DDMFormActionOutcomeEvaluatorConfiguration> {

    public List<String> getFailureStatusLabels() {
        return Arrays.stream(getConfiguration().failureStatusArray()).map(status ->
                StringUtil.isBlank(status) ? WorkflowConstants.LABEL_ANY : status.trim().toLowerCase()).collect(Collectors.toUnmodifiableList());
    }

    public List<Integer> getFailureStatuses() {
        return getFailureStatusLabels().stream().map(status -> WorkflowConstants.getLabelStatus(status)).collect(Collectors.toUnmodifiableList());
    }

    public String getFailureOutcomeTransitionName() {
        return getConfiguration().failureOutcomeTransitionName();
    }

    public String getSuccessOutcomeTransitionName() {
        return getConfiguration().successOutcomeTransitionName();
    }

    @Activate
    @Modified
    protected void activate(Map<String, Object> properties) {
        _log.trace("Activating DDMFormActionOutcomeEvaluatorConfigurationWrapper : {}", properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final DDMFormActionOutcomeEvaluatorConfiguration configuration = ConfigurableUtil.createConfigurable(
                DDMFormActionOutcomeEvaluatorConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
