package com.liferay.workflow.extensions.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liferay.petra.string.StringPool;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.WorkflowConditionExecutionContext;
import com.liferay.workflow.extensions.common.context.WorkflowExecutionContext;
import org.jsoup.helper.StringUtil;

import java.io.Serializable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class WorkflowExtensionsUtil {

    public static String mapAsString(Map<String, ?> map) {
        return map.keySet().stream().map(key -> key + "=" + map.get(key)).collect(Collectors.joining(", ", "{", "}"));
    }

    public static String[] normaliseValue(String value) {
        if (value == null || "".equals(value)) {
            return new String[0];
        }
        try {
            return value.startsWith("[") && value.endsWith("]") ? WorkflowExtensionsConstants.DEFAULT_OBJECT_MAPPER.readValue(value, String[].class) : new String[]{value};
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static String hyphenateWhiteSpaces(String value) {
        return value.replaceAll("\\s", "-");
    }

    public static String buildConfigurationId(WorkflowActionExecutionContext executionContext) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildConfigurationId((WorkflowExecutionContext) executionContext));
        if (!StringUtil.isBlank(executionContext.getActionName())) {
            sb.append(StringPool.COLON);
            sb.append(WorkflowExtensionsUtil.hyphenateWhiteSpaces(executionContext.getActionName()));
        }
        return sb.toString().toLowerCase();
    }

    public static String buildConfigurationId(WorkflowConditionExecutionContext executionContext) {
        return buildConfigurationId((WorkflowExecutionContext) executionContext);
    }

    private static String buildConfigurationId(WorkflowExecutionContext executionContext) {
        StringBuilder sb = new StringBuilder();
        if (!StringUtil.isBlank(executionContext.getWorkflowTitle())) {
            sb.append(WorkflowExtensionsUtil.hyphenateWhiteSpaces(executionContext.getWorkflowTitle()));
        }
        if (!StringUtil.isBlank(executionContext.getNodeName())) {
            sb.append(StringPool.COLON);
            sb.append(WorkflowExtensionsUtil.hyphenateWhiteSpaces(executionContext.getNodeName()));
        }
        return sb.toString().toLowerCase();
    }

    public static String replaceTokens(String template, Map<String, Serializable> workflowContext) {
        final Pattern pattern = Pattern.compile(WorkflowExtensionsConstants.TOKEN_REGEX_STRING);
        final Matcher matcher = pattern.matcher(template);
        final StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            final String key = matcher.group(1);
            if (workflowContext.containsKey(key)) {
                String replacement = String.valueOf(workflowContext.get(key));
                builder.append(template, i, matcher.start());
                if (replacement == null)
                    builder.append(matcher.group(0));
                else
                    builder.append(replacement);
                i = matcher.end();
            }
        }
        builder.append(template.substring(i));
        return builder.toString();
    }
}
