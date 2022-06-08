package com.liferay.workflow.extensions.common.action.executor;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecordVersion;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordVersionLocalServiceUtil;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class BaseActionExecutor {
    protected final Logger _log = LoggerFactory.getLogger(getClass());

    protected abstract WorkflowStatusManager getWorkflowStatusManager();

    protected String normaliseValue(String value) {
        if (value == null || "".equals(value)) {
            return value;
        }
        return value.replaceAll("\\[\"", "").replaceAll("\"]", "");
    }

    protected Locale getDefaultFormLocale(final DDMFormInstance formInstance) throws ActionExecutorException {
        final Locale defaultFormLocal;
        try {
            defaultFormLocal = formInstance.getDDMForm().getDefaultLocale();
        } catch (PortalException e) {
            throw new ActionExecutorException("Unable to get the default form locale : " + formInstance.getFormInstanceId(), e);
        }
        return defaultFormLocal;
    }

    protected List<DDMFormFieldValue> getFormFieldValues(final long recVerId) throws ActionExecutorException {
        final List<DDMFormFieldValue> formFieldValues;
        try {
            formFieldValues = getDDMFormInstanceRecordVersion(recVerId).getDDMFormValues().getDDMFormFieldValues();
        } catch (PortalException e) {
            throw new ActionExecutorException("Unable to get the form field values : " + recVerId, e);
        }
        return formFieldValues;
    }

    protected DDMFormInstanceRecordVersion getDDMFormInstanceRecordVersion(final long recVerId) throws ActionExecutorException {
        final DDMFormInstanceRecordVersion recVer;
        try {
            recVer = DDMFormInstanceRecordVersionLocalServiceUtil.getFormInstanceRecordVersion(recVerId);
        } catch (PortalException e) {
            throw new ActionExecutorException("Unable to get the DDMFormInstanceRecordVersion : " + recVerId, e);
        }
        return recVer;
    }

    protected DDMFormInstance getDDMFormInstance(final long recVerId) throws ActionExecutorException {
        final DDMFormInstance formInstance;
        try {
            formInstance = getDDMFormInstanceRecordVersion(recVerId).getFormInstance();
        } catch (PortalException e) {
            throw new ActionExecutorException("Unable to get the DDMFormInstance : " + recVerId, e);
        }
        return formInstance;
    }

    protected void updateWorkflowStatus(final int status, final Map<String, Serializable> workflowContext) throws ActionExecutorException {
        try {
            if (status > -1) {
                if (_log.isDebugEnabled()) {
                    final String workflowLabelStatus = WorkflowConstants.getStatusLabel(status);
                    _log.debug("Setting workflow status to {} [{}]", workflowLabelStatus, status);
                }
                getWorkflowStatusManager().updateStatus(status, workflowContext);
            }
        } catch (WorkflowException e) {
            throw new ActionExecutorException("Unable to update workflow status", e);
        }
    }

    protected <T extends BaseConfigurationWrapper, M extends SettingsHelper<T>> T getConfigurationWrapper(final long formInstanceId, final M settingsHelper) throws ActionExecutorException {
        final T config;
        config = settingsHelper.getConfigurationWrapper(formInstanceId);
        if (config == null) {
            _log.debug("Could not find the configuration for {}. There are {} configurations available", formInstanceId, settingsHelper.getFormInstanceIdentifiers().length);
            _log.trace("{}", String.join(", ", Arrays.toString(settingsHelper.getFormInstanceIdentifiers())));
            throw new ActionExecutorException("Unable to retrieve configuration : " + formInstanceId);
        }
        return config;
    }
}
