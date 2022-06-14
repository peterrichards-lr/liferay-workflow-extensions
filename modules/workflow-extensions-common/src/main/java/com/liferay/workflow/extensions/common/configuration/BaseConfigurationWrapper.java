package com.liferay.workflow.extensions.common.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseConfigurationWrapper<T extends BaseConfiguration> {
    protected final Logger _log = LoggerFactory.getLogger(getClass());

    private volatile T _configuration;

    public boolean isEnabled() {
        return _configuration.enable();
    }

    protected T getConfiguration() {
        return _configuration;
    }

    protected void setConfiguration(T configuration) {
        this._configuration = configuration;
    }
}
