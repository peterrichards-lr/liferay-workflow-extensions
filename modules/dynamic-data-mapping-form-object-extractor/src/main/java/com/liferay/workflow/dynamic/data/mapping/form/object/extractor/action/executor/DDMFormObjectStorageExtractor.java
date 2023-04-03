package com.liferay.workflow.dynamic.data.mapping.form.object.extractor.action.executor;

import com.liferay.object.model.ObjectEntry;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.dynamic.data.mapping.form.object.extractor.configuration.DDMFormObjectStorageExtractorConfiguration;
import com.liferay.workflow.dynamic.data.mapping.form.object.extractor.configuration.DDMFormObjectStorageExtractorConfigurationWrapper;
import com.liferay.workflow.dynamic.data.mapping.form.object.extractor.settings.DDMFormObjectStorageExtractorSettingsHelper;
import com.liferay.workflow.extensions.common.action.executor.BaseDDFormObjectStorageActionExecutor;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author peterrichards
 */
@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = DDMFormObjectStorageExtractorConfiguration.PID
)
public class DDMFormObjectStorageExtractor extends BaseDDFormObjectStorageActionExecutor<DDMFormObjectStorageExtractorConfiguration, DDMFormObjectStorageExtractorConfigurationWrapper, DDMFormObjectStorageExtractorSettingsHelper> {
    @Reference
    private WorkflowStatusManager _workflowStatusManager;
    @Reference
    private DDMFormObjectStorageExtractorSettingsHelper ddmFormObjectStorageExtractorSettingsHelper;
    @Reference
    private ObjectEntryLocalService objectEntryLocalService;
    @Reference
    private WorkflowActionExecutionContextService workflowActionExecutionContextService;

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final DDMFormObjectStorageExtractorConfigurationWrapper configuration, final long storageEntryId) throws ActionExecutorException {
        _log.info(workflowExecutionContext.toString());
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final ObjectEntry objectEntry;
            try {
                objectEntry = objectEntryLocalService.getObjectEntry(storageEntryId);
            } catch (final PortalException e) {
                throw new ActionExecutorException("Unable to retrieve object for " + storageEntryId, e);
            }
            if (!shouldUpdateWorkflowContext(configuration)) {
                _log.debug("Form object storage extractor configuration requires no workflow context updates : {}", storageEntryId);
                return;
            }
            final boolean success = updateWorkflowContext(storageEntryId, objectEntry, configuration, workflowContext, workflowExecutionContext);
            if (configuration.isWorkflowStatusUpdatedOnSuccess() && success
            ) {
                updateWorkflowStatus(configuration.getSuccessWorkflowStatus(), workflowContext);
            }
        } catch (final PortalException | RuntimeException e) {
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

    private boolean shouldUpdateWorkflowContext(final DDMFormObjectStorageExtractorConfigurationWrapper configuration) {
        boolean shouldUpdateWorkflowContext = configuration.getDDMFieldReferenceArray().length > 0;
        shouldUpdateWorkflowContext |= configuration.isWorkflowInformationRequired();
        return shouldUpdateWorkflowContext;
    }

    private boolean updateWorkflowContext(final long storageEntryId, final ObjectEntry objectEntry, final DDMFormObjectStorageExtractorConfigurationWrapper configuration, final Map<String, Serializable> workflowContext, final WorkflowActionExecutionContext workflowExecutionContext) {
        boolean updateWorkflow = false;
        final String entityType = GetterUtil.getString(workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_TYPE));
        _log.debug("Extracting data from {} [{}]", entityType, storageEntryId);
        final boolean processRequiredFieldReferences = configuration.getDDMFieldReferenceArray().length > 0;
        final List<String> requiredFieldReferences = processRequiredFieldReferences ? Arrays.asList(configuration.getDDMFieldReferenceArray()) : null;
        for (final String fieldReference : objectEntry.getValues().keySet()) {
            if (processRequiredFieldReferences && requiredFieldReferences.contains(fieldReference)) {
                workflowContext.put(fieldReference, objectEntry.getValues().get(fieldReference));
                updateWorkflow = true;
            }
        }
        if (configuration.isWorkflowInformationRequired()) {
            workflowContext.put("workflowName", workflowExecutionContext.getWorkflowName());
            workflowContext.put("workflowTitle", workflowExecutionContext.getWorkflowTitle());
            updateWorkflow = true;
        }
        return updateWorkflow;
    }

    @Override
    protected DDMFormObjectStorageExtractorSettingsHelper getSettingsHelper() {
        return ddmFormObjectStorageExtractorSettingsHelper;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return workflowActionExecutionContextService;
    }

    @Override
    protected WorkflowStatusManager getWorkflowStatusManager() {
        return _workflowStatusManager;
    }
}
