package com.liferay.workflow.extensions.common.configuration.model;

public class MethodParameterConfiguration<T> {
    private final String methodParameterName;
    private final Class<T> methodParameterClass;
    private final boolean required;
    private final T defaultValue;

    public MethodParameterConfiguration(final String methodParameterName, final Class<T> methodParameterClass, final boolean required, final T defaultValue) {
        this.methodParameterName = methodParameterName;
        this.methodParameterClass = methodParameterClass;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    public String getMethodParameterName() {
        return methodParameterName;
    }

    public Class<T> getMethodParameterClass() {
        return methodParameterClass;
    }

    public boolean isRequired() {
        return required;
    }

    public T getDefaultValue() {
        return defaultValue;
    }
}
