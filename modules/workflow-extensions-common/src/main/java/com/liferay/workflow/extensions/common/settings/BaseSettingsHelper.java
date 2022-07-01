package com.liferay.workflow.extensions.common.settings;

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.workflow.extensions.common.configuration.BaseConfiguration;
import com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseSettingsHelper<C extends BaseConfiguration, T extends BaseConfigurationWrapper<C>> implements SettingsHelper<C, T> {
    protected final Logger _log = LoggerFactory.getLogger(getClass());
    private final Map<String, T>
            _configurationWrappers = new ConcurrentHashMap<>();

    @Override
    public final boolean isEnabled(String identifier) {
        final T configurationWrapper = getConfigurationWrapper(identifier);
        if (configurationWrapper == null) {
            return Boolean.parseBoolean(WorkflowExtensionsConstants.CONFIG_ENABLE_DEFAULT);
        }
        return configurationWrapper.isEnabled();
    }

    @Override
    public final T getConfigurationWrapper(String identifier) {
        T configurationWrapper = _configurationWrappers.get(identifier);

        if (configurationWrapper == null) {
            final String[] identifiers = getIdentifiers();
            _log.debug("Could not find the configuration for {}", identifier);
            _log.trace(" There are {} configurations available", identifiers.length);
            _log.trace("{}", String.join(", ", Arrays.toString(identifiers)));
        } else {
            _log.debug("Found for configuration {}", identifier);
        }

        return configurationWrapper;
    }

    @Override
    public final String[] getIdentifiers() {
        return ArrayUtil.toStringArray(
                _configurationWrappers.keySet());
    }

    protected final void addConfigurationWrapper(T configurationWrapper) {
        if (configurationWrapper == null) {
            _log.warn("The configurationWrapper is null. It will not be stored");
            return;
        }
        if (StringUtil.isBlank(configurationWrapper.getIdentifier())) {
            _log.warn("The configurationWrapper identifier is blank. It will not be stored");
            return;
        }
        _configurationWrappers.put(
                (configurationWrapper.getIdentifier()),
                configurationWrapper);
    }

    protected final void removeConfigurationWrapper(T configurationWrapper) {
        if (configurationWrapper == null) {
            _log.warn("The configurationWrapper is null. It will not be stored");
            return;
        }
        if (StringUtil.isBlank(configurationWrapper.getIdentifier())) {
            _log.warn("The configurationWrapper identifier is blank. It will not be stored");
            return;
        }
        _configurationWrappers.remove(
                configurationWrapper.getIdentifier());
    }

    protected final Map<String, T> getConfigurationWrappers() {
        return Collections.unmodifiableMap(_configurationWrappers);
    }
}
