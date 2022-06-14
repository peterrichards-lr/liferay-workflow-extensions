package com.liferay.workflow.extensions.common.configuration;

import com.liferay.portal.kernel.workflow.WorkflowConstants;
import org.jsoup.helper.StringUtil;

public abstract class BaseFormActionExecutorConfigurationWrapper<T extends BaseFormActionExecutorConfiguration> extends BaseFormConfigurationWrapper<T> {
    public String getSuccessWorkflowStatusLabel() {
        return StringUtil.isBlank(getConfiguration().successWorkflowStatus()) ? WorkflowConstants.LABEL_ANY : getConfiguration().successWorkflowStatus().trim().toLowerCase();
    }

    public boolean isWorkflowStatusUpdatedOnSuccess() {
        return getConfiguration().updateWorkflowStatusOnSuccess();
    }

    public int getSuccessWorkflowStatus() {
        return WorkflowConstants.getLabelStatus(getSuccessWorkflowStatusLabel());
    }

    public boolean isWorkflowStatusUpdatedOnException() {
        return getConfiguration().updateWorkflowStatusOnException();
    }

    public String getExceptionWorkflowStatusLabel() {
        return StringUtil.isBlank(getConfiguration().exceptionWorkflowStatus()) ? WorkflowConstants.LABEL_ANY : getConfiguration().exceptionWorkflowStatus().trim().toLowerCase();
    }

    public int getExceptionWorkflowStatus() {
        return WorkflowConstants.getLabelStatus(getExceptionWorkflowStatusLabel());
    }
}
