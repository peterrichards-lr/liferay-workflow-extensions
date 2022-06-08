package com.liferay.workflow.dynamic.data.mapping.form.extractor.configuration;

public class DDMFormInstanceRecordExtractorConfigurationSnapshot implements DDMFormInstanceRecordExtractorConfiguration {

    public DDMFormInstanceRecordExtractorConfigurationSnapshot(String[] ddmFieldReferenceArray, String ddmUserDataFieldMap, boolean extractUploads, long formInstanceId, boolean enable, String successWorkflowStatus, boolean updateWorkflowStatusOnException, String exceptionWorkflowStatus) {
        _ddmFieldReferenceArray = ddmFieldReferenceArray;
        _ddmUserDataFieldMap = ddmUserDataFieldMap;
        _extractUploads = extractUploads;
        _formInstanceId = formInstanceId;
        _enable = enable;
        _successWorkflowStatus = successWorkflowStatus;
        _updateWorkflowStatusOnException = updateWorkflowStatusOnException;
        _exceptionWorkflowStatus = exceptionWorkflowStatus;
    }

    @Override
    public String[] ddmFieldReferenceArray() {
        return _ddmFieldReferenceArray;
    }

    @Override
    public String ddmUserDataFieldMap() {
       return _ddmUserDataFieldMap;
    }

    @Override
    public boolean extractUploads() {
        return _extractUploads;
    }

    @Override
    public long formInstanceId() {
        return _formInstanceId;
    }

    @Override
    public boolean enable() {
        return _enable;
    }

    @Override
    public String successWorkflowStatus() {
        return _successWorkflowStatus;
    }

    @Override
    public boolean updateWorkflowStatusOnException() {
        return _updateWorkflowStatusOnException;
    }

    @Override
    public String exceptionWorkflowStatus() {
        return _exceptionWorkflowStatus;
    }

    private final String[] _ddmFieldReferenceArray;
    private final String _ddmUserDataFieldMap;
    private final boolean _extractUploads;
    private final long _formInstanceId;
    private final boolean _enable;
    private final String _successWorkflowStatus;
    private final boolean _updateWorkflowStatusOnException;
    private final String _exceptionWorkflowStatus;
}
