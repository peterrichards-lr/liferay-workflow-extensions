package com.liferay.workflow.extensions.common.action.executor;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecordVersion;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordVersionLocalServiceUtil;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.context.WorkflowExecutionContext;
import com.liferay.workflow.extensions.common.configuration.BaseConfiguration;
import com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class BaseDDMFormActionExecutor<C extends BaseConfiguration, T extends BaseConfigurationWrapper<C>, S extends SettingsHelper<T>> extends BaseActionExecutor {
    protected abstract S getSettingsHelper();

    protected final Locale getDefaultFormLocale(final DDMFormInstance formInstance) throws ActionExecutorException {
        final Locale defaultFormLocal;
        try {
            defaultFormLocal = formInstance.getDDMForm().getDefaultLocale();
        } catch (PortalException e) {
            throw new ActionExecutorException("Unable to get the default form locale : " + formInstance.getFormInstanceId(), e);
        }
        return defaultFormLocal;
    }

    protected final List<DDMFormFieldValue> getFormFieldValues(final long recVerId) throws ActionExecutorException {
        final List<DDMFormFieldValue> formFieldValues;
        try {
            formFieldValues = getDDMFormInstanceRecordVersion(recVerId).getDDMFormValues().getDDMFormFieldValues();
        } catch (PortalException e) {
            throw new ActionExecutorException("Unable to get the form field values : " + recVerId, e);
        }
        return formFieldValues;
    }

    protected final DDMFormInstanceRecordVersion getDDMFormInstanceRecordVersion(final long recVerId) throws ActionExecutorException {
        final DDMFormInstanceRecordVersion recVer;
        try {
            recVer = DDMFormInstanceRecordVersionLocalServiceUtil.getFormInstanceRecordVersion(recVerId);
        } catch (PortalException e) {
            throw new ActionExecutorException("Unable to get the DDMFormInstanceRecordVersion : " + recVerId, e);
        }
        return recVer;
    }

    protected final DDMFormInstance getDDMFormInstance(final long recVerId) throws ActionExecutorException {
        final DDMFormInstance formInstance;
        try {
            formInstance = getDDMFormInstanceRecordVersion(recVerId).getFormInstance();
        } catch (PortalException e) {
            throw new ActionExecutorException("Unable to get the DDMFormInstance : " + recVerId, e);
        }
        return formInstance;
    }

    private boolean isDDMFormEntryClass(final Map<String, Serializable> workflowContext) {
        final String entryClassName = GetterUtil.getString(workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_NAME));
        return DDMFormInstanceRecord.class.getName().equals(entryClassName);
    }

    private T getConfigurationWrapper(final long formInstanceId) throws ActionExecutorException {
        final T config;
        final S settingsHelper = getSettingsHelper();
        if (settingsHelper == null) {
            throw new ActionExecutorException("SettingsHelper is null");
        }
        config = settingsHelper.getConfigurationWrapper(formInstanceId);
        if (config == null) {
            _log.debug("Could not find the configuration for {}. There are {} configurations available", formInstanceId, settingsHelper.getFormInstanceIdentifiers().length);
            _log.trace("{}", String.join(", ", Arrays.toString(settingsHelper.getFormInstanceIdentifiers())));
            throw new ActionExecutorException("Unable to retrieve configuration : " + formInstanceId);
        }
        return config;
    }

    @Override
    public final void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowExecutionContext workflowExecutionContext) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();

        if (!isDDMFormEntryClass(workflowContext)) {
            _log.debug("Entry class is not the correct type");
            return;
        }

        final long formInstanceRecordVersionId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));
        final DDMFormInstance formInstance = getDDMFormInstance(formInstanceRecordVersionId);
        final long formInstanceId = formInstance.getFormInstanceId();
        final T configuration = getConfigurationWrapper(formInstanceId);

        if (!configuration.isEnabled()) {
            _log.debug("Configuration is disabled : {}", formInstanceId);
            return;
        }

        execute(kaleoAction, executionContext, workflowExecutionContext, configuration, formInstanceRecordVersionId);
    }

    public abstract void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowExecutionContext workflowExecutionContext, final T configurationWrapper, long formInstanceRecordVersionId) throws ActionExecutorException;
}
