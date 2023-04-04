package com.liferay.workflow.extensions.common.configuration;

public class BaseDDMFormConditionEvaluatorConfigurationWrapper<T extends BaseDDMFormConditionEvaluatorConfiguration> extends BaseConditionEvaluatorConfigurationWrapper<T> {
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