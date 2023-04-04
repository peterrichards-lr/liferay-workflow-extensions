package com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.configuration.persistence.listener;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.workflow.dynamic.data.mapping.form.action.outcome.evaluator.configuration.DDMFormActionOutcomeEvaluatorConfiguration;
import com.liferay.workflow.extensions.common.configuration.persistence.listener.BaseConfigurationModelListener;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "model.class.name=" + DDMFormActionOutcomeEvaluatorConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class DDMFormActionOutcomeEvaluatorConfigurationModelListener extends BaseConfigurationModelListener<DDMFormActionOutcomeEvaluatorConfiguration> {

    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    protected ConfigurationAdmin getConfigurationAdmin() {
        return _configurationAdmin;
    }

    @Override
    protected Class<DDMFormActionOutcomeEvaluatorConfiguration> getConfigurationClass() {
        return DDMFormActionOutcomeEvaluatorConfiguration.class;
    }
}

