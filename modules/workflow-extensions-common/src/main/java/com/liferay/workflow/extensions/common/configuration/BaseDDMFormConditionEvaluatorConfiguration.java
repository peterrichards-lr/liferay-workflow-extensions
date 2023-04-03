package com.liferay.workflow.extensions.common.configuration;

@SuppressWarnings("EmptyMethod")
public interface BaseDDMFormConditionEvaluatorConfiguration extends BaseConditionEvaluatorConfiguration {
    long formInstanceRecordVersionId();

    String formInstanceRecordVersionIdValueWorkflowContextKey();

    boolean useEntryClassPrimaryKey();

    boolean useWorkflowContextKeyForFormInstanceRecordVersionId();
}
