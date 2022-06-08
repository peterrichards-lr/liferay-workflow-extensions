package com.liferay.workflow.dynamic.data.mapping.upload.processor.action.executor;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration.DDMFormUploadProcessorConfiguration;
import com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration.DDMFormUploadProcessorConfigurationWrapper;
import com.liferay.workflow.dynamic.data.mapping.upload.processor.settings.DDMFormUploadProcessorSettingsHelper;
import com.liferay.workflow.extensions.common.action.executor.BaseActionExecutor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.Map;

@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = DDMFormUploadProcessorConfiguration.PID
)
public class DDMFormUploadProcessor extends BaseActionExecutor implements ActionExecutor {
    @Reference
    private DDMFormUploadProcessorSettingsHelper _ddmFormUploadProcessorSettingsHelper;
    @Reference
    private WorkflowStatusManager _workflowStatusManager;

    @Override
    protected WorkflowStatusManager getWorkflowStatusManager() {
        return _workflowStatusManager;
    }

    @Override
    public void execute(KaleoAction kaleoAction, ExecutionContext executionContext) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        final long recVerId = GetterUtil.getLong((String) workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));
        DDMFormUploadProcessorConfigurationWrapper configuration = null;

        try {
            final DDMFormInstance formInstance = getDDMFormInstance(recVerId);
            final long formInstanceId = formInstance.getFormInstanceId();

            configuration = getConfigurationWrapper(formInstanceId, _ddmFormUploadProcessorSettingsHelper);

            if (!configuration.isEnabled()) {
                _log.debug("Form extractor configuration is disabled : {}", formInstanceId);
                return;
            }

            if (processUploads(recVerId, formInstance, configuration, workflowContext)) {
                updateWorkflowStatus(configuration.getSuccessWorkflowStatus(), workflowContext);
            }
        } catch (PortalException e) {
            if (configuration == null) {
                _log.warn("Unable to determine if workflow status is updated on exception. Configuration is null");
                throw e;
            } else if (configuration.isWorkflowStatusUpdatedOnException()) {
                _log.error("Unexpected exception. See inner exception for details", e);
                updateWorkflowStatus(configuration.getExceptionWorkflowStatus(), workflowContext);
            } else {
                _log.error("Unexpected exception. See inner exception for details", e);
            }
        } catch (RuntimeException e) {
            if (configuration == null) {
                _log.warn("Unable to determine if workflow status is updated on exception. Configuration is null");
                throw e;
            } else if (configuration.isWorkflowStatusUpdatedOnException()) {
                _log.error("Unexpected exception. See inner exception for details", e);
                updateWorkflowStatus(configuration.getExceptionWorkflowStatus(), workflowContext);
            } else {
                _log.error("Unexpected exception. See inner exception for details", e);
            }
        }
    }

    private boolean processUploads(long recVerId, DDMFormInstance formInstance, DDMFormUploadProcessorConfigurationWrapper configuration, Map<String, Serializable> workflowContext) throws ActionExecutorException {
        return true;
    }
}
