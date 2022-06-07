package com.liferay.workflow.dynamic.data.mapping.form.extractor.action.executor;

import com.liferay.dynamic.data.mapping.model.*;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordVersionLocalServiceUtil;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.GetterUtil;
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
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class DDMFormInstanceRecordExtractor implements ActionExecutor {

    private static final Logger _log = LoggerFactory.getLogger(DDMFormInstanceRecordExtractor.class);
    @Reference
    private DDMFormInstanceRecordExtractorSettingsHelper _ddmFormInstanceRecordExtractorSettings;
    @Reference
    private WorkflowStatusManager _workflowStatusManager;

    @Override
    public void execute(KaleoAction kaleoAction, ExecutionContext executionContext) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        final long recVerId = GetterUtil.getLong((String) workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));
        DDMFormInstanceRecordExtractorConfigurationWrapper configuration = null;
        
        try {

            final DDMFormInstance formInstance = getDDMFormInstance(recVerId);
            final long formInstanceId = formInstance.getFormInstanceId();

            configuration = getConfigurationWrapper(formInstanceId);

            if (!configuration.isEnabled()) {
                _log.debug("Form extractor configuration is disabled : {}", formInstanceId);
                return;
            }

            if (!shouldUpdateWorkflowContext(configuration)) {
                _log.debug("Form extractor configuration requires no workflow context updates : {}", formInstanceId);
                return;
            }

            if (updateWorkflow(recVerId, formInstance, configuration, workflowContext)) {
                updateWorkflowStatus(configuration.getSuccessWorkflowStatus(), workflowContext);
            }
        } catch (Exception e) {
            if (configuration != null && configuration.isWorkflowStatusUpdatedOnException()) {
                updateWorkflowStatus(configuration.getExceptionWorkflowStatus(), workflowContext);
            }
            throw e;
        }
    }

    private boolean updateWorkflow(final long recVerId, final DDMFormInstance formInstance, final DDMFormInstanceRecordExtractorConfigurationWrapper configuration, final Map<String, Serializable> workflowContext) throws ActionExecutorException {
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

        final List<DDMFormFieldValue> formFieldValues = getFormFieldValues(recVerId);

        for (DDMFormFieldValue formValue : formFieldValues) {
            final DDMFormField formField = formValue.getDDMFormField();
            final String fieldReference = formValue.getFieldReference();
            if (processUploads && "document_library".equals(formField.getType())) {
                final Value val = formValue.getValue();
                uploadDocuments.add(val.getString(Locale.ROOT));
            } else if (processRequiredFieldReferences && requiredFieldReferences.contains(fieldReference)) {
                final Value val = formValue.getValue();
                final String data = normaliseValue(val.getString(Locale.ROOT));
                _log.info("Adding {} : {} to the WorkflowContext", fieldReference, data);
                workflowContext.put(fieldReference, data);
                updateWorkflow = true;
            } else if (processUserDataFields && fieldReference.contains("UsersDataField")) {
                _log.info("Processing user data field - {}", fieldReference);
                final LocalizedValue label = formField.getLabel();
                final String labelText = label.getString(Locale.ROOT);
                if (userDataFieldMap.containsKey(labelText)) {
                    final String fieldName = userDataFieldMap.get(labelText);
                    final Value val = formValue.getValue();
                    final String data = normaliseValue(val.getString(Locale.ROOT));
                    _log.info("Adding {} : {} to the WorkflowContext", fieldName, data);
                    workflowContext.put(fieldName, data);
                    updateWorkflow = true;
                } else {
                    _log.debug("User data field not found : {}", labelText);
                }
            } else {
                _log.trace("Unknown field type : {}", fieldReference);
            }
        }

        if (uploadDocuments.size() > 0) {
            _log.info("Adding {} to the WorkflowContext", uploadDocuments.size());
            workflowContext.put("uploadDocuments", (Serializable) uploadDocuments);
            updateWorkflow = true;
        }

        final Locale defaultFormLocale = getDefaultFormLocale(formInstance);
        workflowContext.put("defaultFormLocale", defaultFormLocale);

        return updateWorkflow;
    }

    private String normaliseValue(String value) {
        if (value == null || "".equals(value)) {
            return value;
        }
        return value.replaceAll("\\[\"", "").replaceAll("\"]", "");
    }

    private Locale getDefaultFormLocale(final DDMFormInstance formInstance) throws ActionExecutorException {
        final Locale defaultFormLocal;
        try {
            defaultFormLocal = formInstance.getDDMForm().getDefaultLocale();
        } catch (PortalException e) {
            throw new ActionExecutorException("Unable to get the default form locale : " + formInstance.getFormInstanceId(), e);
        }
        return defaultFormLocal;
    }

    private List<DDMFormFieldValue> getFormFieldValues(final long recVerId) throws ActionExecutorException {
        final List<DDMFormFieldValue> formFieldValues;
        try {
            formFieldValues = getDDMFormInstanceRecordVersion(recVerId).getDDMFormValues().getDDMFormFieldValues();
        } catch (PortalException e) {
            throw new ActionExecutorException("Unable to get the form field values : " + recVerId, e);
        }
        return formFieldValues;
    }

    private DDMFormInstanceRecordVersion getDDMFormInstanceRecordVersion(final long recVerId) throws ActionExecutorException {
        final DDMFormInstanceRecordVersion recVer;
        try {
            recVer = DDMFormInstanceRecordVersionLocalServiceUtil.getFormInstanceRecordVersion(recVerId);
        } catch (PortalException e) {
            throw new ActionExecutorException("Unable to get the DDMFormInstanceRecordVersion : " + recVerId, e);
        }
        return recVer;
    }

    private DDMFormInstance getDDMFormInstance(final long recVerId) throws ActionExecutorException {
        final DDMFormInstance formInstance;
        try {
            formInstance = getDDMFormInstanceRecordVersion(recVerId).getFormInstance();
        } catch (PortalException e) {
            throw new ActionExecutorException("Unable to get the DDMFormInstance : " + recVerId, e);
        }
        return formInstance;
    }

    private DDMFormInstanceRecordExtractorConfigurationWrapper getConfigurationWrapper(final long formInstanceId) throws ActionExecutorException {
        final DDMFormInstanceRecordExtractorConfigurationWrapper config;
        config = _ddmFormInstanceRecordExtractorSettings.getConfigurationWrapper(formInstanceId);
        if (config == null) {
            throw new ActionExecutorException("Unable to retrieve extractor configuration : " + formInstanceId);
        }
        return config;
    }

    private boolean shouldUpdateWorkflowContext(final DDMFormInstanceRecordExtractorConfigurationWrapper configuration) {
        boolean shouldUpdateWorkflowContext = configuration.isExtractUploadsRequired();
        shouldUpdateWorkflowContext |= configuration.getDDMFieldReferenceArray().length > 0;
        shouldUpdateWorkflowContext |= !configuration.getDDMUserDataFieldMap().isEmpty();
        return shouldUpdateWorkflowContext;
    }

    private void updateWorkflowStatus(final int status, final Map<String, Serializable> workflowContext) throws ActionExecutorException {
        try {
            if (status > -1) {
                if (_log.isDebugEnabled()) {
                    final String workflowLabelStatus = WorkflowConstants.getStatusLabel(status);
                    _log.debug("Setting workflow status to {} [{}]", workflowLabelStatus, status);
                }
                _workflowStatusManager.updateStatus(status, workflowContext);
            }
        } catch (WorkflowException e) {
            throw new ActionExecutorException("Unable to update workflow status", e);
        }
    }
}