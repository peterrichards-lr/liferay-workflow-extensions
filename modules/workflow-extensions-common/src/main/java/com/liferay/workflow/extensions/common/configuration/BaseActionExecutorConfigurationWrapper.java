package com.liferay.workflow.extensions.common.configuration;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.workflow.extensions.common.util.StringUtil;

public class BaseActionExecutorConfigurationWrapper<T extends BaseActionExecutorConfiguration> extends BaseConfigurationWrapper<T> {
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

    @Override
    public String toString() {
        return "BaseActionExecutorConfigurationWrapper{" +
                "super=" + super.toString() +
                StringPool.COMMA +
                "updateWorkflowStatusOnSuccess=" + getConfiguration().updateWorkflowStatusOnSuccess() +
                StringPool.COMMA +
                "successWorkflowStatus=" + StringPool.APOSTROPHE + getSuccessWorkflowStatusLabel() + StringPool.APOSTROPHE +
                StringPool.OPEN_BRACKET + getConfiguration().successWorkflowStatus() + StringPool.CLOSE_BRACKET +
                StringPool.COMMA +
                "updateWorkflowStatusOnException=" + StringPool.APOSTROPHE + getConfiguration().updateWorkflowStatusOnException() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "exceptionWorkflowStatus=" + getExceptionWorkflowStatusLabel() +
                StringPool.OPEN_BRACKET + getConfiguration().exceptionWorkflowStatus() + StringPool.CLOSE_BRACKET +
                '}';
    }
}
