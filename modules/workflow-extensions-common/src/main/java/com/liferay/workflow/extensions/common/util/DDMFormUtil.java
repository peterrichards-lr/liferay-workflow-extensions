package com.liferay.workflow.extensions.common.util;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecordVersion;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordVersionLocalServiceUtil;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DDMFormUtil {
    public static Locale getDefaultFormLocale(final DDMFormInstance formInstance) throws WorkflowException {
        final Locale defaultFormLocal;
        try {
            defaultFormLocal = formInstance.getDDMForm().getDefaultLocale();
        } catch (PortalException e) {
            throw new WorkflowException("Unable to get the default form locale : " + formInstance.getFormInstanceId(), e);
        }
        return defaultFormLocal;
    }

    public static boolean isDDMFormEntryClass(final Map<String, Serializable> workflowContext) {
        final String entryClassName = GetterUtil.getString(workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_NAME));
        return DDMFormInstanceRecord.class.getName().equals(entryClassName);
    }

    public static boolean isDDMFormObjectStorageClass(final Map<String, Serializable> workflowContext) {
        final String entryClassName = GetterUtil.getString(workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_NAME));
        return entryClassName == null ? false :
                entryClassName.startsWith(ObjectDefinition.class.getName());
    }

    public static List<DDMFormFieldValue> getFormFieldValues(final long recVerId) throws WorkflowException {
        final List<DDMFormFieldValue> formFieldValues;
        try {
            formFieldValues = getDDMFormInstanceRecordVersion(recVerId).getDDMFormValues().getDDMFormFieldValues();
        } catch (PortalException e) {
            throw new WorkflowException("Unable to get the form field values : " + recVerId, e);
        }
        return formFieldValues;
    }

    public static DDMFormInstanceRecordVersion getDDMFormInstanceRecordVersion(final long recVerId) throws WorkflowException {
        final DDMFormInstanceRecordVersion recVer;
        try {
            recVer = DDMFormInstanceRecordVersionLocalServiceUtil.getFormInstanceRecordVersion(recVerId);
        } catch (PortalException e) {
            throw new WorkflowException("Unable to get the DDMFormInstanceRecordVersion : " + recVerId, e);
        }
        return recVer;
    }

    public static DDMFormInstance getDDMFormInstance(final long recVerId) throws WorkflowException {
        final DDMFormInstance formInstance;
        try {
            formInstance = getDDMFormInstanceRecordVersion(recVerId).getFormInstance();
        } catch (PortalException e) {
            throw new WorkflowException("Unable to get the DDMFormInstance : " + recVerId, e);
        }
        return formInstance;
    }
}
