package com.liferay.workflow.extensions.common.condition.evaluator;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.model.KaleoCondition;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.portal.workflow.kaleo.runtime.condition.ConditionEvaluator;
import com.liferay.workflow.extensions.common.BaseConfigurableNode;
import com.liferay.workflow.extensions.common.configuration.BaseConditionEvaluatorConfiguration;
import com.liferay.workflow.extensions.common.configuration.model.WorkflowConditionNamingLevel;
import com.liferay.workflow.extensions.common.context.WorkflowConditionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowConditionExecutionContextService;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;

import java.util.Locale;

public abstract class BaseWorkflowConditionEvaluator<C extends BaseConditionEvaluatorConfiguration, W extends com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper<C>, S extends SettingsHelper<C, W>> extends BaseConfigurableNode<C, W, S, WorkflowConditionExecutionContext> implements ConditionEvaluator {

    @Override
    public final String evaluate(
            final KaleoCondition kaleoCondition, final ExecutionContext executionContext)
            throws PortalException {
        final ServiceContext serviceContext = executionContext.getServiceContext();
        configureWorkflowExecutionContext(kaleoCondition, serviceContext);
        final WorkflowConditionExecutionContext workflowExecutionContext = getWorkflowExecutionContext();
        final String configurationId = WorkflowExtensionsUtil.buildConfigurationId(workflowExecutionContext);
        final W configuration = WorkflowExtensionsUtil.getConfiguration(workflowExecutionContext, this::getConfigurationWrapper, WorkflowConditionNamingLevel.NODE);
        if (configuration == null) {
            throw new ActionExecutorException("Unable to find configuration for " + configurationId);
        }
        _log.debug("Found configuration for {}", configurationId);
        if (!configuration.isEnabled()) {
            _log.debug("Configuration is disabled : {}", configurationId);
            return null;
        }
        return evaluate(kaleoCondition, executionContext, workflowExecutionContext, configuration);
    }

    private void configureWorkflowExecutionContext(final KaleoCondition kaleoCondition, final ServiceContext serviceContext) {
        final Locale serviceContextLocale = serviceContext.getLocale();
        final WorkflowConditionExecutionContext executionContext = getWorkflowConditionExecutionContextService().buildWorkflowConditionExecutionContext(kaleoCondition, serviceContextLocale);
        setWorkflowExecutionContext(executionContext);
    }

    @SuppressWarnings("unused")
    protected abstract String evaluate(final KaleoCondition kaleoCondition, final ExecutionContext executionContext, final WorkflowConditionExecutionContext workflowExecutionContext, final W configuration);

    protected abstract WorkflowConditionExecutionContextService getWorkflowConditionExecutionContextService();
}
