package com.liferay.workflow.extensions.common.constants;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.workflow.extensions.common.util.WorkflowActionNamingLevel;
import com.liferay.workflow.extensions.common.util.WorkflowConditionNamingLevel;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class WorkflowExtensionsConstants {
    public static final String CONFIG_WORKFLOW_NODE_ID = "config-workflow-node-identifier-id";
    public static final String CONFIG_WORKFLOW_NODE_ID_DEFAULT = "::";
    public static final String CONFIG_ENABLE_DEFAULT = "true";
    public static final String CONFIG_UPDATE_WORKFLOW_STATUS_ON_EXCEPTION_DEFAULT = "true";
    public static final String CONFIG_EXCEPTION_WORKFLOW_STATUS_DEFAULT = WorkflowConstants.LABEL_INCOMPLETE;
    public static final String CONFIG_SUCCESS_WORKFLOW_STATUS_DEFAULT = WorkflowConstants.LABEL_PENDING;
    public static final String TO_STRING_SEPARATOR = ", ";
    public static final ObjectMapper DEFAULT_OBJECT_MAPPER;
    public static final MapType CONFIG_MAP_TYPE;
    public static final String CONFIG_UPDATE_WORKFLOW_STATUS_ON_SUCCESS_DEFAULT = "true";

    public static final String TOKEN_REGEX_STRING = "\\$\\{(.+?)\\}";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT;

    public static final WorkflowActionNamingLevel DEFAULT_WORKFLOW_ACTION_NAMING_LEVEL = WorkflowActionNamingLevel.ACTION;

    public static final WorkflowConditionNamingLevel DEFAULT_WORKFLOW_CONDITION_NAMING_LEVEL = WorkflowConditionNamingLevel.NODE;

    static {
        final TypeFactory factory = TypeFactory.defaultInstance();
        CONFIG_MAP_TYPE = factory.constructMapType(HashMap.class, String.class, String.class);
        DEFAULT_OBJECT_MAPPER = new ObjectMapper();
        SIMPLE_DATE_FORMAT = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
    }
}
