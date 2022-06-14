package com.liferay.workflow.extensions.common.util;

import java.util.Map;
import java.util.stream.Collectors;

public final class WorkflowExtensionsUtil {

    public static String mapAsString(Map<String, ?> map) {
        return map.keySet().stream().map(key -> key + "=" + map.get(key)).collect(Collectors.joining(", ", "{", "}"));
    }

    public static String normaliseValue(String value) {
        if (value == null || "".equals(value)) {
            return value;
        }
        return value.replaceAll("\\[\"", "").replaceAll("\"]", "");
    }
}
