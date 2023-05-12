package com.liferay.workflow.extensions.common.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class StringUtil {
    public static boolean isBlank(final String string) {
        return string == null || string.trim().isEmpty();
    }

    public static Map<Locale, String> localiseString(final String value) {
        return localiseString(value, Locale.ROOT);
    }

    public static Map<Locale, String> localiseString(final String value, final Locale locale) {
        return new HashMap<>() {{
            put(locale, value);
        }};
    }
}
