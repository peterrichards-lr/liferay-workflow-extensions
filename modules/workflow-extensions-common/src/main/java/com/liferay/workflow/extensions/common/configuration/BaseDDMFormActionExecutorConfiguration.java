package com.liferay.workflow.extensions.common.configuration;

@SuppressWarnings("EmptyMethod")
public interface BaseDDMFormActionExecutorConfiguration extends BaseActionExecutorConfiguration {
    long formInstanceRecordVersionId();

    String formInstanceRecordVersionIdValueWorkflowContextKey();

    boolean useEntryClassPrimaryKey();

    boolean useWorkflowContextKeyForFormInstanceRecordVersionId();
}
