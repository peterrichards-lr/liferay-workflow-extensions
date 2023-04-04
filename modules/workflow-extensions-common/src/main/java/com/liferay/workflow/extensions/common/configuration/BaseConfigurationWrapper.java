package com.liferay.workflow.extensions.common.configuration;

import com.liferay.petra.string.StringPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseConfigurationWrapper<T extends BaseConfiguration> {
    protected final Logger _log = LoggerFactory.getLogger(getClass());
    private volatile T _configuration;

    public String getIdentifier() {
        return getConfiguration().identifier() != null ? getConfiguration().identifier().toLowerCase() : null;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isEnabled() {
        return _configuration.enable();
    }

    @Override
    public String toString() {
        return "BaseConfigurationWrapper{" +
                "identifier=" + StringPool.APOSTROPHE + _configuration.identifier() + StringPool.APOSTROPHE +
                StringPool.COMMA +
                "enable=" + _configuration.enable() +
                '}';
    }

    protected T getConfiguration() {
        return _configuration;
    }

    protected void setConfiguration(final T configuration) {
        this._configuration = configuration;
    }
}
