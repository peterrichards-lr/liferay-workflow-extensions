package com.liferay.workflow.extensions.common.action.executor;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManagerUtil;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.BaseConfigurableNode;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.configuration.model.WorkflowActionNamingLevel;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public abstract class BaseWorkflowActionExecutor<C extends BaseActionExecutorConfiguration, W extends BaseActionExecutorConfigurationWrapper<C>, S extends SettingsHelper<C, W>> extends BaseConfigurableNode<C, W, S, WorkflowActionExecutionContext> implements ActionExecutor {

    private void configureWorkflowExecutionContext(final KaleoAction kaleoAction, final ServiceContext serviceContext) {
        final Locale serviceContextLocale = serviceContext.getLocale();
        final WorkflowActionExecutionContext executionContext = getWorkflowActionExecutionContextService().buildWorkflowActionExecutionContext(kaleoAction, serviceContextLocale);
        setWorkflowExecutionContext(executionContext);
    }

    @Override
    public void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext) throws ActionExecutorException {
        final ServiceContext serviceContext = executionContext.getServiceContext();
        configureWorkflowExecutionContext(kaleoAction, serviceContext);
        final WorkflowActionExecutionContext workflowExecutionContext = getWorkflowExecutionContext();
        final String configurationId = WorkflowExtensionsUtil.buildConfigurationId(workflowExecutionContext);
        final W configuration = WorkflowExtensionsUtil.getConfiguration(workflowExecutionContext, this::getConfigurationWrapper, WorkflowActionNamingLevel.ACTION);
        if (configuration == null) {
            throw new ActionExecutorException("Unable to find configuration for " + configurationId);
        }
        _log.debug("Found configuration for {}", configurationId);
        if (!configuration.isEnabled()) {
            _log.debug("Configuration is disabled : {}", configurationId);
            return;
        }
        execute(kaleoAction, executionContext, workflowExecutionContext, configuration);
    }

    protected abstract void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final W configuration) throws ActionExecutorException;

    @Override
    public String getActionExecutorKey() {
        return WorkflowExtensionsConstants.ACTION_EXECUTOR_KEY;
    }

    protected abstract WorkflowActionExecutionContextService getWorkflowActionExecutionContextService();

    protected void updateWorkflowStatus(final int status, final Map<String, Serializable> workflowContext) throws WorkflowException {
        try {
            if (status > -1) {
                if (_log.isDebugEnabled()) {
                    final String workflowLabelStatus = WorkflowConstants.getStatusLabel(status);
                    _log.debug("Setting workflow status to {} [{}]", workflowLabelStatus, status);
                }
                WorkflowStatusManagerUtil.updateStatus(status, workflowContext);
            }
        } catch (final WorkflowException e) {
            throw new WorkflowException("Unable to update workflow status", e);
        }
    }
}
