package com.liferay.workflow.extensions.common.configuration;

public class BaseDDMFormActionExecutorConfigurationWrapper<T extends BaseDDMFormActionExecutorConfiguration> extends BaseActionExecutorConfigurationWrapper<T> {
    public long getFormInstanceRecordVersionId() {
        return getConfiguration().formInstanceRecordVersionId();
    }

    public String getFormInstanceRecordVersionIdWorkflowContextKey() {
        return getConfiguration().formInstanceRecordVersionIdValueWorkflowContextKey();
    }

    public boolean isEntryClassPrimaryKeyUsed() {
        return getConfiguration().useEntryClassPrimaryKey();
    }

    public boolean isWorkflowContextKeyUsedForFormInstanceRecordVersionId() {
        return getConfiguration().useWorkflowContextKeyForFormInstanceRecordVersionId();
    }
}
