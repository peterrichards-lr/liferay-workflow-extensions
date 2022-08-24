package com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.configuration;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.workflow.extensions.common.configuration.BaseDDMFormConditionEvaluatorConfigurationWrapper;
import com.liferay.workflow.extensions.common.util.StringUtil;
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
public class DDMFormActionOutcomeEvaluatorConfigurationWrapper extends BaseDDMFormConditionEvaluatorConfigurationWrapper<DDMFormActionOutcomeEvaluatorConfiguration> {

    public List<String> getFailureStatusLabels() {
        return Arrays.stream(getConfiguration().failureStatusArray()).map(status ->
                StringUtil.isBlank(status) ? WorkflowConstants.LABEL_ANY : status.trim().toLowerCase()).collect(Collectors.toUnmodifiableList());
    }

    public List<Integer> getFailureStatuses() {
        return getFailureStatusLabels().stream().map(WorkflowConstants::getLabelStatus).collect(Collectors.toUnmodifiableList());
    }

    public String getFailureOutcomeTransitionName() {
        return getConfiguration().failureOutcomeTransitionName();
    }

    public String getSuccessOutcomeTransitionName() {
        return getConfiguration().successOutcomeTransitionName();
    }

    @Override
    public String toString() {
        return "BaseActionExecutorConfigurationWrapper{" +
                "super=" + super.toString() +
                StringPool.COMMA +
                "failureStatusArray=" + StringPool.OPEN_BRACKET +
                String.join(",", getFailureStatusLabels()) +
                StringPool.CLOSE_BRACKET + StringPool.COMMA +
                "failureOutcomeTransitionName=" +
                StringPool.APOSTROPHE + getConfiguration().failureOutcomeTransitionName() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "successOutcomeTransitionName=" +
                StringPool.APOSTROPHE + getConfiguration().successOutcomeTransitionName() + StringPool.APOSTROPHE +
                '}';
    }

    @Activate
    @Modified
    protected void activate(final Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final DDMFormActionOutcomeEvaluatorConfiguration configuration = ConfigurableUtil.createConfigurable(
                DDMFormActionOutcomeEvaluatorConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
