package com.liferay.workflow.dynamic.data.mapping.form.extractor.constants;

import com.liferay.portal.kernel.workflow.WorkflowConstants;

public final class DDMFormInstanceRecordExtractorConstants {
    public static final String CONFIG_FORM_INSTANCE_ID = "config-ddm-form-instance-identifier-id";

    public static final String CONFIG_FORM_INSTANCE_ID_DEFAULT = "0";
    public static final String CONFIG_ENABLE_DEFAULT = "true";
    public static final String CONFIG_FIELD_REFERENCE_ARRAY_DEFAULT = "forename|surname";
    public static final String CONFIG_USER_DATA_FIELD_MAP_DEFAULT = "{ \"Email address\" : \"emailAddress\" }";
    public static final String CONFIG_EXTRACT_UPLOADS_DEFAULT = "false";
    public static final String CONFIG_SUCCESS_WORKFLOW_STATUS_DEFAULT = WorkflowConstants.LABEL_PENDING;
}
