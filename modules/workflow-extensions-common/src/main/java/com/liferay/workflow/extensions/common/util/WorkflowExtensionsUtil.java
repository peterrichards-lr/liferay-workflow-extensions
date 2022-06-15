package com.liferay.workflow.extensions.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;

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
