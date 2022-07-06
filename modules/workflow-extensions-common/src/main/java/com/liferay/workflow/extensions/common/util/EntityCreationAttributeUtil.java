package com.liferay.workflow.extensions.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

public class EntityCreationAttributeUtil {
    public static final HashMap<Class<?>, Function<String, ?>> parser = new HashMap<>();

    static {
        parser.put(boolean.class, Boolean::parseBoolean); // Support boolean literals too
        parser.put(int.class, Integer::parseInt);
        parser.put(long.class, Long::parseLong);
        parser.put(Boolean.class, Boolean::valueOf);
        parser.put(Integer.class, Integer::valueOf);
        parser.put(Long.class, Long::valueOf);
        parser.put(Double.class, Double::valueOf);
        parser.put(Float.class, Float::valueOf);
        parser.put(String.class, String::valueOf);  // Handle String without special test
        parser.put(BigDecimal.class, BigDecimal::new);
        parser.put(BigInteger.class, BigInteger::new);
        parser.put(LocalDate.class, LocalDate::parse); // Java 8 time API
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object parse(final String argString, final Class param) {
        final Function<String, ?> func = parser.get(param);
        if (func != null)
            return func.apply(argString);
        if (param.isEnum()) // Special handling for enums
            return Enum.valueOf(param, argString);
        throw new UnsupportedOperationException("Cannot parse string to " + param.getName());
    }

    public static long[] unboxed(final Long[] array) {
        return array == null ? new long[0] :
                Arrays.stream(array)
                        .filter(Objects::nonNull)
                        .mapToLong(Long::longValue)
                        .toArray();
    }
}
