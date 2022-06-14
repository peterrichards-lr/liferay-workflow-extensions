package com.liferay.workflow.extensions.common;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecordVersion;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordVersionLocalServiceUtil;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.workflow.extensions.common.configuration.BaseFormConfigurationWrapper;
import com.liferay.workflow.extensions.common.context.WorkflowExecutionContext;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class BaseDDMFormNode<T extends BaseFormConfigurationWrapper, S extends SettingsHelper<T>, W extends WorkflowExecutionContext> extends BaseNode<W> {
    protected abstract S getSettingsHelper();

    protected final Locale getDefaultFormLocale(final DDMFormInstance formInstance) throws WorkflowException {
        final Locale defaultFormLocal;
        try {
            defaultFormLocal = formInstance.getDDMForm().getDefaultLocale();
        } catch (PortalException e) {
            throw new WorkflowException("Unable to get the default form locale : " + formInstance.getFormInstanceId(), e);
        }
        return defaultFormLocal;
    }

    protected final List<DDMFormFieldValue> getFormFieldValues(final long recVerId) throws WorkflowException {
        final List<DDMFormFieldValue> formFieldValues;
        try {
            formFieldValues = getDDMFormInstanceRecordVersion(recVerId).getDDMFormValues().getDDMFormFieldValues();
        } catch (PortalException e) {
            throw new WorkflowException("Unable to get the form field values : " + recVerId, e);
        }
        return formFieldValues;
    }

    protected final DDMFormInstanceRecordVersion getDDMFormInstanceRecordVersion(final long recVerId) throws WorkflowException {
        final DDMFormInstanceRecordVersion recVer;
        try {
            recVer = DDMFormInstanceRecordVersionLocalServiceUtil.getFormInstanceRecordVersion(recVerId);
        } catch (PortalException e) {
            throw new WorkflowException("Unable to get the DDMFormInstanceRecordVersion : " + recVerId, e);
        }
        return recVer;
    }

    protected final DDMFormInstance getDDMFormInstance(final long recVerId) throws WorkflowException {
        final DDMFormInstance formInstance;
        try {
            formInstance = getDDMFormInstanceRecordVersion(recVerId).getFormInstance();
        } catch (PortalException e) {
            throw new WorkflowException("Unable to get the DDMFormInstance : " + recVerId, e);
        }
        return formInstance;
    }

    protected boolean isDDMFormEntryClass(final Map<String, Serializable> workflowContext) {
        final String entryClassName = GetterUtil.getString(workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_NAME));
        return DDMFormInstanceRecord.class.getName().equals(entryClassName);
    }

    protected T getConfigurationWrapper(final long formInstanceId) throws WorkflowException {
        final T config;
        final S settingsHelper = getSettingsHelper();
        if (settingsHelper == null) {
            throw new WorkflowException("SettingsHelper is null");
        }
        config = settingsHelper.getConfigurationWrapper(formInstanceId);
        if (config == null) {
            _log.debug("Could not find the configuration for {}. There are {} configurations available", formInstanceId, settingsHelper.getFormInstanceIdentifiers().length);
            _log.trace("{}", String.join(", ", Arrays.toString(settingsHelper.getFormInstanceIdentifiers())));
            throw new WorkflowException("Unable to retrieve configuration : " + formInstanceId);
        }
        return config;
    }
}
