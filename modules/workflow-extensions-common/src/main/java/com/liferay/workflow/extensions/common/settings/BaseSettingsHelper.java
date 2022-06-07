package com.liferay.workflow.extensions.common.settings;

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper;
import com.liferay.workflow.extensions.common.configuration.constants.WorkflowExtensionsConstants;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSettingsHelper<T extends BaseConfigurationWrapper> implements SettingsHelper<T> {
    @Override
    public final boolean isEnabled(long formInstanceId) {
        final T configurationWrapper = getConfigurationWrapper(formInstanceId);
        if (configurationWrapper == null) {
            return Boolean.parseBoolean(WorkflowExtensionsConstants.CONFIG_ENABLE_DEFAULT);
        }
        return configurationWrapper.isEnabled();
    }

    @Override
    public final T getConfigurationWrapper(long id) {
        T configurationWrapper = _configurationWrappers.get(id);

        if (configurationWrapper == null) {
            _log.error("Unable to get configuration wrapper: " + id);
        }

        return configurationWrapper;
    }

    @Override
    public final long[] getFormInstanceIdentifiers() {
        return ArrayUtil.toLongArray(
                _configurationWrappers.keySet());
    }

    protected final void addConfigurationWrapper(T configurationWrapper) {
        _configurationWrappers.put(
                (configurationWrapper.getFormInstanceId()),
                configurationWrapper);
    }

    protected final void removeConfigurationWrapper(T configurationWrapper) {
        _configurationWrappers.remove(
                configurationWrapper.getFormInstanceId());
    }

    protected final Map<Long, T> getConfigurationWrappers() {
        return Collections.unmodifiableMap(_configurationWrappers);
    }

    private final Map<Long, T>
            _configurationWrappers = new ConcurrentHashMap<>();

    protected final Logger _log = LoggerFactory.getLogger(getClass());
}
