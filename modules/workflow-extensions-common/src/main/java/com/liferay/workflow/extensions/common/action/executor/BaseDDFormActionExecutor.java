package com.liferay.workflow.extensions.common.action.executor;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.BaseConfigurableNode;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;
import com.liferay.workflow.extensions.common.util.DDMFormUtil;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public abstract class BaseDDFormActionExecutor<C extends BaseActionExecutorConfiguration, W extends com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper<C>, S extends SettingsHelper<C, W>> extends BaseConfigurableNode<C, W, S, WorkflowActionExecutionContext> implements ActionExecutor {

    @Override
    public final void execute(
            KaleoAction kaleoAction, ExecutionContext executionContext)
            throws ActionExecutorException {
        final ServiceContext serviceContext = executionContext.getServiceContext();
        try {
            configureWorkflowExecutionContext(kaleoAction, serviceContext);
        } catch (PortalException e) {
            throw new ActionExecutorException("Failed to configure WorkflowExecutionContext", e);
        }

        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();

        if (!DDMFormUtil.isDDMFormEntryClass(workflowContext)) {
            _log.debug("Entry class is not the correct type");
            return;
        }

        final long formInstanceRecordVersionId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));
        final DDMFormInstance formInstance;
        try {
            formInstance = DDMFormUtil.getDDMFormInstance(formInstanceRecordVersionId);
        } catch (WorkflowException e) {
            throw new ActionExecutorException("See inner exception", e);
        }
        final long formInstanceId = formInstance.getFormInstanceId();
        final W configuration;
        try {
            configuration = getConfigurationWrapper(String.valueOf(formInstanceId));
        } catch (WorkflowException e) {
            throw new ActionExecutorException("See inner exception", e);
        }

        if (!configuration.isEnabled()) {
            _log.debug("Configuration is disabled : {}", formInstanceId);
            return;
        }

        execute(kaleoAction, executionContext, getWorkflowExecutionContext(), configuration, formInstanceRecordVersionId);
    }

    private void configureWorkflowExecutionContext(KaleoAction kaleoAction, ServiceContext serviceContext) throws ActionExecutorException {
        final Locale serviceContextLocale = serviceContext.getLocale();
        final WorkflowActionExecutionContext executionContext = getWorkflowActionExecutionContextService().buildWorkflowActionExecutionContext(kaleoAction, serviceContextLocale);
        setWorkflowExecutionContext(executionContext);
    }

    protected abstract WorkflowActionExecutionContextService getWorkflowActionExecutionContextService();

    protected abstract void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final W configuration, long formInstanceRecordVersionId) throws ActionExecutorException;
}
