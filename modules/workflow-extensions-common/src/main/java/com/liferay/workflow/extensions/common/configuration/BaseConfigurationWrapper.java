package com.liferay.workflow.extensions.common.configuration;

import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.workflow.extensions.common.configuration.constants.WorkflowExtensionsConstants;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseConfigurationWrapper<T extends BaseConfiguration> {
    protected final Logger _log = LoggerFactory.getLogger(getClass());
    private volatile T _configuration;

    public long getFormInstanceId() {
        return _configuration.formInstanceId();
    }

    public boolean isEnabled() {
        return _configuration.enable();
    }

    public String getSuccessWorkflowStatusLabel() {
        return StringUtil.isBlank(_configuration.successWorkflowStatus()) ? WorkflowConstants.LABEL_ANY : _configuration.successWorkflowStatus().trim().toLowerCase();
    }

    public int getSuccessWorkflowStatus() {
        return WorkflowConstants.getLabelStatus(getSuccessWorkflowStatusLabel());
    }

    public boolean isWorkflowStatusUpdatedOnException() {
        return _configuration.updateWorkflowStatusOnException();
    }

    public String getExceptionWorkflowStatusLabel() {
        return StringUtil.isBlank(_configuration.exceptionWorkflowStatus()) ? WorkflowConstants.LABEL_ANY : _configuration.exceptionWorkflowStatus().trim().toLowerCase();
    }

    public int getExceptionWorkflowStatus() {
        return WorkflowConstants.getLabelStatus(getExceptionWorkflowStatusLabel());
    }

    public String toString() {
        return "formInstanceId=" +
                getFormInstanceId() +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "enabled=" +
                isEnabled() +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "successWorkflowStatus='" +
                getSuccessWorkflowStatusLabel() +
                "' [" +
                getSuccessWorkflowStatus() +
                "]" +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "updateWorkflowStatusOnException=" +
                isWorkflowStatusUpdatedOnException() +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                "exceptionWorkflowStatus='" +
                getExceptionWorkflowStatusLabel() +
                "' [" +
                getExceptionWorkflowStatus() +
                "]" +
                WorkflowExtensionsConstants.TO_STRING_SEPARATOR +
                toStringSubClass();
    }

    protected abstract String toStringSubClass();

    protected T getConfiguration() {
        return _configuration;
    }

    protected void setConfiguration(T configuration) {
        this._configuration = configuration;
    }
}
