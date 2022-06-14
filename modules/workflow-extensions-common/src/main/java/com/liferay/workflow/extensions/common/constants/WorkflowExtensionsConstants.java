package com.liferay.workflow.extensions.common.constants;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.HashMap;

public class WorkflowExtensionsConstants {
    public static final String CONFIG_FORM_INSTANCE_ID = "config-ddm-form-instance-identifier-id";
    public static final String CONFIG_FORM_INSTANCE_ID_DEFAULT = "0";
    public static final String CONFIG_ENABLE_DEFAULT = "true";
    public static final String CONFIG_UPDATE_WORKFLOW_STATUS_ON_EXCEPTION_DEFAULT = "true";
    public static final String CONFIG_EXCEPTION_WORKFLOW_STATUS_DEFAULT = WorkflowConstants.LABEL_INCOMPLETE;
    public static final String CONFIG_SUCCESS_WORKFLOW_STATUS_DEFAULT = WorkflowConstants.LABEL_PENDING;
    public static final String TO_STRING_SEPARATOR = ", ";
    public static final ObjectMapper DEFAULT_OBJECT_MAPPER;
    public static final MapType CONFIG_MAP_TYPE;
    public static final String CONFIG_UPDATE_WORKFLOW_STATUS_ON_SUCCESS_DEFAULT = "true";

    static {
        final TypeFactory factory = TypeFactory.defaultInstance();
        CONFIG_MAP_TYPE = factory.constructMapType(HashMap.class, String.class, String.class);
        DEFAULT_OBJECT_MAPPER = new ObjectMapper();
    }
}
