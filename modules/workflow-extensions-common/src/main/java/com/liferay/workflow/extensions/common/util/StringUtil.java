package com.liferay.workflow.extensions.common.util;

public final class StringUtil {
    public static boolean isBlank(final String string) {
        return string == null || string.trim().isEmpty();
    }
}
