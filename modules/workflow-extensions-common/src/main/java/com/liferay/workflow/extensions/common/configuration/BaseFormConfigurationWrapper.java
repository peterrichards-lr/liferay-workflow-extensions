package com.liferay.workflow.extensions.common.configuration;

public abstract class BaseFormConfigurationWrapper<T extends BaseFormConfiguration> extends BaseConfigurationWrapper<T> {

    public long getFormInstanceId() {
        return getConfiguration().formInstanceId();
    }
}
