package com.liferay.workflow.extensions.common.configuration;

import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.workflow.extensions.common.configuration.constants.WorkflowExtensionsConstants;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseConfigurationWrapper<T extends BaseConfiguration> {
    protected final Logger _log = LoggerFactory.getLogger(getClass());
    private volatile T _configuration;

    public final long getFormInstanceId() {
        _log.info(_configuration.getClass().getName());
        return _configuration.formInstanceId();
    }

    public final boolean isEnabled() {
        return _configuration.enable();
    }

    public final String getSuccessWorkflowStatusLabel() {
        return StringUtil.isBlank(_configuration.successWorkflowStatus()) ? WorkflowConstants.LABEL_ANY : _configuration.successWorkflowStatus();
    }

    public final int getSuccessWorkflowStatus() {
        return WorkflowConstants.getLabelStatus(getSuccessWorkflowStatusLabel());
    }

    public final boolean isWorkflowStatusUpdatedOnException() {
        return _configuration.updateWorkflowStatusOnException();
    }

    public final String getExceptionWorkflowStatusLabel() {
        return StringUtil.isBlank(_configuration.exceptionWorkflowStatus()) ? WorkflowConstants.LABEL_ANY : _configuration.exceptionWorkflowStatus();
    }

    public final int getExceptionWorkflowStatus() {
        return WorkflowConstants.getLabelStatus(getExceptionWorkflowStatusLabel());
    }

    public final String toString() {
        return "formInstanceId : " +
                getFormInstanceId() +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "enabled : " +
                isEnabled() +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "successWorkflowStatus : " +
                getSuccessWorkflowStatusLabel() +
                " [" +
                getSuccessWorkflowStatus() +
                "]" +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "updateWorkflowStatusOnException : " +
                isWorkflowStatusUpdatedOnException() +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "exceptionWorkflowStatus : " +
                getExceptionWorkflowStatusLabel() +
                " [" +
                getExceptionWorkflowStatus() +
                "]" +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                toStringSubClass();
    }

    protected abstract String toStringSubClass();

    protected final T getConfiguration() {
        return _configuration;
    }

    protected final void setConfiguration(T configuration) {
        this._configuration = configuration;
    }
}
