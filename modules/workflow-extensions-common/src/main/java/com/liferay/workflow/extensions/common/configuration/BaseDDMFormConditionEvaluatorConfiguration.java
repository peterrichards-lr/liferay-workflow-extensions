package com.liferay.workflow.extensions.common.configuration;

@SuppressWarnings("EmptyMethod")
public interface BaseDDMFormConditionEvaluatorConfiguration extends BaseConditionEvaluatorConfiguration {
    boolean useEntryClassPrimaryKey();

    boolean useWorkflowContextKeyForFormInstanceRecordVersionId();

    String formInstanceRecordVersionIdValueWorkflowContextKey();

    long formInstanceRecordVersionId();
}
