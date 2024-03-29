package com.liferay.workflow.dynamic.data.mapping.form.options.translator.action.executor;

import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.dynamic.data.mapping.form.options.translator.configuration.DDMFormOptionsTranslatorConfiguration;
import com.liferay.workflow.dynamic.data.mapping.form.options.translator.configuration.DDMFormOptionsTranslatorConfigurationWrapper;
import com.liferay.workflow.dynamic.data.mapping.form.options.translator.configuration.model.OptionTranslation;
import com.liferay.workflow.dynamic.data.mapping.form.options.translator.settings.DDMFormOptionsTranslatorSettingsHelper;
import com.liferay.workflow.extensions.common.action.executor.BaseDDFormActionExecutor;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author peterrichards
 */
@Component(property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java", service = ActionExecutor.class, configurationPid = DDMFormOptionsTranslatorConfiguration.PID)
public class DDMFormOptionsTranslator extends BaseDDFormActionExecutor<DDMFormOptionsTranslatorConfiguration, DDMFormOptionsTranslatorConfigurationWrapper, DDMFormOptionsTranslatorSettingsHelper> implements ActionExecutor {
    @Reference
    private DDMFormOptionsTranslatorSettingsHelper _ddmFormOptionsTranslatorSettingsHelper;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final DDMFormOptionsTranslatorConfigurationWrapper configuration, final long formInstanceRecordVersionId) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final boolean success = processTranslations(workflowContext, configuration);
            if (configuration.isWorkflowStatusUpdatedOnSuccess() && success) {
                updateWorkflowStatus(configuration.getSuccessWorkflowStatus(), workflowContext);
            }

        } catch (final WorkflowException | RuntimeException e) {
            if (configuration.isWorkflowStatusUpdatedOnException()) {
                _log.error("Unexpected exception. See inner exception for details", e);
                try {
                    updateWorkflowStatus(configuration.getExceptionWorkflowStatus(), workflowContext);
                } catch (final WorkflowException ex) {
                    throw new ActionExecutorException("See inner exception", e);
                }
            } else {
                _log.error("Unexpected exception. See inner exception for details", e);
            }
        }
    }

    @Override
    protected DDMFormOptionsTranslatorSettingsHelper getSettingsHelper() {
        return _ddmFormOptionsTranslatorSettingsHelper;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }

    private boolean processTranslations(final Map<String, Serializable> workflowContext, final DDMFormOptionsTranslatorConfigurationWrapper configuration) {
        final List<OptionTranslation> optionTranslations = configuration.getOptionTranslationArray();
        if (optionTranslations.isEmpty()) {
            return false;
        }
        boolean workflowContextUpdated = false;
        for (final OptionTranslation optionTranslation : optionTranslations) {
            final String workflowContextKey = optionTranslation.getWorkflowContextKey();
            if (!workflowContext.containsKey(workflowContextKey)) {
                _log.info("The workflowContext does not contain an entry for {}", workflowContextKey);
                continue;
            }
            final Map<String, String> translationMap = optionTranslation.getTranslationMap();
            final Serializable originalValue = workflowContext.get(workflowContextKey);
            if (originalValue instanceof String[]) {
                final String[] valueArray = (String[]) originalValue;
                final String[] translatedValues = new String[valueArray.length];
                for (int i = 0; i < valueArray.length; i++) {
                    final String nestedValue = valueArray[i];
                    if (!translationMap.containsKey(nestedValue)) {
                        _log.info("The translationMap does not contain an entry for {}", nestedValue);
                        continue;
                    }
                    final String translatedValue = translationMap.get(nestedValue);
                    translatedValues[i] = translatedValue;
                    workflowContextUpdated = true;
                }
                _log.info("Updating {} : {} in the WorkflowContext", workflowContextKey, translatedValues);
                workflowContext.put(workflowContextKey, translatedValues);
            } else {
                final String valueString = (String) originalValue;
                if (!translationMap.containsKey(valueString)) {
                    _log.info("The translationMap does not contain an entry for {}", valueString);
                    continue;
                }
                final String translatedValue = translationMap.get(valueString);
                _log.info("Updating {} : {} in the WorkflowContext", workflowContextKey, translatedValue);
                workflowContext.put(workflowContextKey, translatedValue);
                workflowContextUpdated = true;
            }
        }
        return workflowContextUpdated;
    }
}
