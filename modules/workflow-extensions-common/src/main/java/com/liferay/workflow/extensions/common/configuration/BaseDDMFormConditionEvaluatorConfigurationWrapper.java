package com.liferay.workflow.extensions.common.configuration;

public class BaseDDMFormConditionEvaluatorConfigurationWrapper<T extends BaseDDMFormConditionEvaluatorConfiguration> extends BaseConditionEvaluatorConfigurationWrapper<T> {
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