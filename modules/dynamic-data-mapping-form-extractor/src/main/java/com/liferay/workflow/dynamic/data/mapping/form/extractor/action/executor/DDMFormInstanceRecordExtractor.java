package com.liferay.workflow.dynamic.data.mapping.form.extractor.action.executor;

import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration.DDMFormInstanceRecordExtractorConfiguration;
import com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration.DDMFormInstanceRecordExtractorConfigurationWrapper;
import com.liferay.workflow.dynamic.data.mapping.form.extractor.settings.DDMFormInstanceRecordExtractorSettingsHelper;
import com.liferay.workflow.extensions.common.action.executor.BaseDDFormActionExecutor;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.util.DDMFormUtil;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.*;

/**
 * @author peterrichards
 */
@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = DDMFormInstanceRecordExtractorConfiguration.PID
)
public class DDMFormInstanceRecordExtractor extends BaseDDFormActionExecutor<DDMFormInstanceRecordExtractorConfiguration, DDMFormInstanceRecordExtractorConfigurationWrapper, DDMFormInstanceRecordExtractorSettingsHelper> implements ActionExecutor {
    @Reference
    private DDMFormInstanceRecordExtractorSettingsHelper _ddmFormInstanceRecordExtractorSettingsHelper;
    @Reference
    private WorkflowStatusManager _workflowStatusManager;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;

    @Override
    protected DDMFormInstanceRecordExtractorSettingsHelper getSettingsHelper() {
        return _ddmFormInstanceRecordExtractorSettingsHelper;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }

    @Override
    public void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final DDMFormInstanceRecordExtractorConfigurationWrapper configuration, final long formInstanceRecordVersionId) throws ActionExecutorException {
        _log.info(workflowExecutionContext.toString());

        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final DDMFormInstance formInstance = DDMFormUtil.getDDMFormInstance(formInstanceRecordVersionId);
            final long formInstanceId = formInstance.getFormInstanceId();

            if (!shouldUpdateWorkflowContext(configuration)) {
                _log.debug("Form extractor configuration requires no workflow context updates : {}", formInstanceId);
                return;
            }

            final boolean success = updateWorkflow(formInstanceRecordVersionId, formInstance, configuration, workflowContext, workflowExecutionContext);
            if (configuration.isWorkflowStatusUpdatedOnSuccess() && success
            ) {
                updateWorkflowStatus(configuration.getSuccessWorkflowStatus(), workflowContext);
            }
        } catch (PortalException | RuntimeException e) {
            if (configuration.isWorkflowStatusUpdatedOnException()) {
                _log.error("Unexpected exception. See inner exception for details", e);
                try {
                    updateWorkflowStatus(configuration.getExceptionWorkflowStatus(), workflowContext);
                } catch (WorkflowException ex) {
                    throw new ActionExecutorException("See inner exception", e);
                }
            } else {
                _log.error("Unexpected exception. See inner exception for details", e);
            }
        }
    }

    private boolean updateWorkflow(final long recVerId, final DDMFormInstance formInstance, final DDMFormInstanceRecordExtractorConfigurationWrapper configuration, final Map<String, Serializable> workflowContext, final WorkflowActionExecutionContext workflowExecutionContext) throws ActionExecutorException {
        boolean updateWorkflow = false;

        final String formName = formInstance.getName(Locale.getDefault());
        final long formInstanceId = formInstance.getFormInstanceId();

        _log.debug("Extracting data from {} [{}]", formName, formInstanceId);

        final boolean processRequiredFieldReferences = configuration.getDDMFieldReferenceArray().length > 0;
        final boolean processUserDataFields = !configuration.getDDMUserDataFieldMap().isEmpty();
        final boolean processUploads = configuration.isExtractUploadsRequired();

        final List<String> requiredFieldReferences = processRequiredFieldReferences ? Arrays.asList(configuration.getDDMFieldReferenceArray()) : null;
        final Map<String, String> userDataFieldMap = processUserDataFields ? configuration.getDDMUserDataFieldMap() : null;
        final List<String> uploadDocuments = processUploads ? new ArrayList<>() : null;

        final List<DDMFormFieldValue> formFieldValues;
        try {
            formFieldValues = DDMFormUtil.getFormFieldValues(recVerId);
        } catch (WorkflowException e) {
            throw new ActionExecutorException("See inner exception", e);
        }

        for (DDMFormFieldValue formValue : formFieldValues) {
            final DDMFormField formField = formValue.getDDMFormField();
            final String fieldReference = formValue.getFieldReference();
            if (processUploads && "document_library".equals(formField.getType())) {
                final Value val = formValue.getValue();
                final String documentJson = val.getString(Locale.ROOT);
                if (!"{}".equals(documentJson)) {
                    uploadDocuments.add(documentJson);
                }
            } else if (processRequiredFieldReferences && requiredFieldReferences.contains(fieldReference)) {
                final Value val = formValue.getValue();
                final String[] data = WorkflowExtensionsUtil.normaliseValue(val.getString(Locale.ROOT));
                if (data == null || data.length == 0) {
                    continue;
                }
                if (data.length == 1) {
                    _log.info("Adding {} : {} to the WorkflowContext", fieldReference, data[0]);
                    workflowContext.put(fieldReference, data[0]);
                } else {
                    _log.info("Adding {} : [{}] to the WorkflowContext", fieldReference, String.join(StringPool.COMMA, data));
                    workflowContext.put(fieldReference, data);
                }
                updateWorkflow = true;
            } else if (processUserDataFields && fieldReference.contains("UsersDataField")) {
                _log.info("Processing user data field - {}", fieldReference);
                final LocalizedValue label = formField.getLabel();
                final String labelText = label.getString(Locale.ROOT);
                if (userDataFieldMap.containsKey(labelText)) {
                    final String fieldName = userDataFieldMap.get(labelText);
                    final Value val = formValue.getValue();
                    final String[] data = WorkflowExtensionsUtil.normaliseValue(val.getString(Locale.ROOT));
                    if (data == null || data.length == 0) {
                        continue;
                    }
                    if (data.length == 1) {
                        _log.info("Adding {} : {} to the WorkflowContext", fieldName, data);
                        workflowContext.put(fieldName, data);
                    } else {
                        _log.info("Adding {} : [{}] to the WorkflowContext", fieldName, String.join(StringPool.COMMA, data));
                        workflowContext.put(fieldName, data);
                    }
                    updateWorkflow = true;
                } else {
                    _log.debug("User data field not found : {}", labelText);
                }
            } else {
                _log.trace("Unknown field type : {}", fieldReference);
            }
        }

        if (uploadDocuments != null && uploadDocuments.size() > 0) {
            _log.info("Adding {} document to the WorkflowContext", uploadDocuments.size());
            workflowContext.put("uploadDocuments", (Serializable) uploadDocuments);
            updateWorkflow = true;
        }

        if (updateWorkflow) {
            final Locale defaultFormLocale;
            try {
                defaultFormLocale = DDMFormUtil.getDefaultFormLocale(formInstance);
            } catch (WorkflowException e) {
                throw new ActionExecutorException("See inner exception", e);
            }
            workflowContext.put("defaultFormLocale", defaultFormLocale);
        }

        if (configuration.isWorkflowInformationRequired()) {
            workflowContext.put("workflowName", workflowExecutionContext.getWorkflowName());
            workflowContext.put("workflowTitle", workflowExecutionContext.getWorkflowTitle());
            updateWorkflow = true;
        }

        return updateWorkflow;
    }

    private boolean shouldUpdateWorkflowContext(final DDMFormInstanceRecordExtractorConfigurationWrapper configuration) {
        boolean shouldUpdateWorkflowContext = configuration.isExtractUploadsRequired();
        shouldUpdateWorkflowContext |= configuration.getDDMFieldReferenceArray().length > 0;
        shouldUpdateWorkflowContext |= !configuration.getDDMUserDataFieldMap().isEmpty();
        shouldUpdateWorkflowContext |= configuration.isWorkflowInformationRequired();
        return shouldUpdateWorkflowContext;
    }

    private void updateWorkflowStatus(final int status, final Map<String, Serializable> workflowContext) throws WorkflowException {
        try {
            if (status > -1) {
                if (_log.isDebugEnabled()) {
                    final String workflowLabelStatus = WorkflowConstants.getStatusLabel(status);
                    _log.debug("Setting workflow status to {} [{}]", workflowLabelStatus, status);
                }
                _workflowStatusManager.updateStatus(status, workflowContext);
            }
        } catch (WorkflowException e) {
            throw new WorkflowException("Unable to update workflow status", e);
        }
    }
}