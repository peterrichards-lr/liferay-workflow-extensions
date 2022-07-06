package com.liferay.workflow.extensions.common.configuration;

@SuppressWarnings("EmptyMethod")
public interface BaseDDMFormActionExecutorConfiguration extends BaseActionExecutorConfiguration {
    boolean useEntryClassPrimaryKey();

    boolean useWorkflowContextKeyForFormInstanceRecordVersionId();

    String formInstanceRecordVersionIdValueWorkflowContextKey();

    long formInstanceRecordVersionId();
}
