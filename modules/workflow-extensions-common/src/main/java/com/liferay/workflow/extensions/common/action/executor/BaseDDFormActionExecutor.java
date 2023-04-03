package com.liferay.workflow.extensions.common.action.executor;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.configuration.BaseDDMFormActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.configuration.BaseDDMFormActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;
import com.liferay.workflow.extensions.common.util.DDMFormUtil;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public abstract class BaseDDFormActionExecutor<C extends BaseDDMFormActionExecutorConfiguration, W extends BaseDDMFormActionExecutorConfigurationWrapper<C>, S extends SettingsHelper<C, W>> extends BaseWorkflowActionExecutor<C, W, S> implements ActionExecutor {

    @Override
    public final void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final W configuration)
            throws ActionExecutorException {
        final ServiceContext serviceContext = executionContext.getServiceContext();
        configureWorkflowExecutionContext(kaleoAction, serviceContext);
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        if (!DDMFormUtil.isDDMFormEntryClass(workflowContext)) {
            _log.debug("Entry class is not the correct type");
            return;
        }
        final long formInstanceRecordVersionId =
                configuration.isEntryClassPrimaryKeyUsed() ?
                        GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK)) :
                        getFormInstanceRecordVersionId(configuration, workflowContext);
        execute(kaleoAction, executionContext, getWorkflowExecutionContext(), configuration, formInstanceRecordVersionId);
    }

    private void configureWorkflowExecutionContext(final KaleoAction kaleoAction, final ServiceContext serviceContext) {
        final Locale serviceContextLocale = serviceContext.getLocale();
        final WorkflowActionExecutionContext executionContext = getWorkflowActionExecutionContextService().buildWorkflowActionExecutionContext(kaleoAction, serviceContextLocale);
        setWorkflowExecutionContext(executionContext);
    }

    private long getFormInstanceRecordVersionId(final W configuration, final Map<String, Serializable> workflowContext) {
        if (configuration.isWorkflowContextKeyUsedForFormInstanceRecordVersionId()) {
            final String workflowKey = configuration.getFormInstanceRecordVersionIdWorkflowContextKey();
            return workflowContext.containsKey(workflowKey) ?
                    GetterUtil.getLong(workflowContext.get(workflowKey)) :
                    configuration.getFormInstanceRecordVersionId();
        } else {
            return configuration.getFormInstanceRecordVersionId();
        }
    }

    @SuppressWarnings("unused")
    protected abstract void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final W configuration, long formInstanceRecordVersionId) throws ActionExecutorException;

    protected abstract WorkflowActionExecutionContextService getWorkflowActionExecutionContextService();
}
