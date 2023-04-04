package com.liferay.workflow.extensions.common.action.executor;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;
import com.liferay.workflow.extensions.common.util.DDMFormUtil;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public abstract class BaseDDFormObjectStorageActionExecutor<C extends BaseActionExecutorConfiguration, W extends BaseActionExecutorConfigurationWrapper<C>, S extends SettingsHelper<C, W>> extends BaseWorkflowActionExecutor<C, W, S> implements ActionExecutor {

    private void configureWorkflowExecutionContext(final KaleoAction kaleoAction, final ServiceContext serviceContext) {
        final Locale serviceContextLocale = serviceContext.getLocale();
        final WorkflowActionExecutionContext executionContext = getWorkflowActionExecutionContextService().buildWorkflowActionExecutionContext(kaleoAction, serviceContextLocale);
        setWorkflowExecutionContext(executionContext);
    }

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final W configuration) throws ActionExecutorException {
        if (!configuration.isEnabled()) {
            _log.debug("Configuration is disabled : {}", configuration.getIdentifier());
            return;
        }
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        if (!DDMFormUtil.isDDMFormObjectStorageClass(workflowContext)) {
            _log.debug("Entry class is not the correct type");
            return;
        }
        final ServiceContext serviceContext = executionContext.getServiceContext();
        configureWorkflowExecutionContext(kaleoAction, serviceContext);
        final long storageEntryId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));
        execute(kaleoAction, executionContext, workflowExecutionContext, configuration, storageEntryId);
    }

    @SuppressWarnings("unused")
    protected abstract void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final W configuration, long storageEntryId) throws ActionExecutorException;
}
