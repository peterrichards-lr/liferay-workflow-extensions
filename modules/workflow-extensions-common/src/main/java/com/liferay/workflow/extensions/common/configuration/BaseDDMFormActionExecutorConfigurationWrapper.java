package com.liferay.workflow.extensions.common.configuration;

public class BaseDDMFormActionExecutorConfigurationWrapper<T extends BaseDDMFormActionExecutorConfiguration> extends BaseActionExecutorConfigurationWrapper<T> {
    public boolean isEntryClassPrimaryKeyUsed() {
        return getConfiguration().useEntryClassPrimaryKey();
    }

    public boolean isWorkflowContextKeyUsedForFormInstanceRecordVersionId() {
        return getConfiguration().useWorkflowContextKeyForFormInstanceRecordVersionId();
    }

    public String getFormInstanceRecordVersionIdWorkflowContextKey() {
        return getConfiguration().formInstanceRecordVersionIdValueWorkflowContextKey();
    }

    public long getFormInstanceRecordVersionId() {
        return getConfiguration().formInstanceRecordVersionId();
    }
}
