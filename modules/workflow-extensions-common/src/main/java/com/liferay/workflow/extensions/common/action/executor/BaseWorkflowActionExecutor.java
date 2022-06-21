package com.liferay.workflow.extensions.common.action.executor;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.BaseConfigurableNode;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;

import java.util.Locale;

public abstract class BaseWorkflowActionExecutor<C extends BaseActionExecutorConfiguration, W extends BaseActionExecutorConfigurationWrapper<C>, S extends SettingsHelper<C, W>> extends BaseConfigurableNode<C, W, S, WorkflowActionExecutionContext> implements ActionExecutor {

    @Override
    public void execute(KaleoAction kaleoAction, ExecutionContext executionContext) throws ActionExecutorException {
        final ServiceContext serviceContext = executionContext.getServiceContext();
        try {
            configureWorkflowExecutionContext(kaleoAction, serviceContext);
        } catch (PortalException e) {
            throw new ActionExecutorException("Failed to configure WorkflowExecutionContext", e);
        }
        final WorkflowActionExecutionContext workflowExecutionContext = getWorkflowExecutionContext();
        final String configurationId = WorkflowExtensionsUtil.buildConfigurationId(workflowExecutionContext);
        final W configuration;
        try {
            configuration = getConfigurationWrapper(configurationId);
        } catch (WorkflowException e) {
            throw new ActionExecutorException("See inner exception", e);
        }

        if (!configuration.isEnabled()) {
            _log.debug("Configuration is disabled : {}", configurationId);
            return;
        }

        execute(kaleoAction, executionContext, workflowExecutionContext, configuration);
    }

    private void configureWorkflowExecutionContext(KaleoAction kaleoAction, ServiceContext serviceContext) throws ActionExecutorException {
        final Locale serviceContextLocale = serviceContext.getLocale();
        final WorkflowActionExecutionContext executionContext = getWorkflowActionExecutionContextService().buildWorkflowActionExecutionContext(kaleoAction, serviceContextLocale);
        setWorkflowExecutionContext(executionContext);
    }

    protected abstract WorkflowActionExecutionContextService getWorkflowActionExecutionContextService();

    protected abstract void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final W configuration) throws ActionExecutorException;
}
