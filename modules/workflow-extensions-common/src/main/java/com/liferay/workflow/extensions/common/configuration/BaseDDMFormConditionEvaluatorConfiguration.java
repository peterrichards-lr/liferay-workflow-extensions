package com.liferay.workflow.extensions.common.configuration;

public interface BaseDDMFormConditionEvaluatorConfiguration extends BaseConditionEvaluatorConfiguration {
    boolean useEntryClassPrimaryKey();

    boolean useWorkflowContextKeyForFormInstanceRecordVersionId();

    String formInstanceRecordVersionIdValueWorkflowContextKey();

    long formInstanceRecordVersionId();
}
