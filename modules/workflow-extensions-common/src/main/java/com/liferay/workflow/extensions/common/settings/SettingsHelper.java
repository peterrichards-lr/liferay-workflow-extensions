package com.liferay.workflow.extensions.common.settings;

import com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper;

public interface SettingsHelper<T extends BaseConfigurationWrapper> {
    boolean isEnabled(long id);
    long[] getFormInstanceIdentifiers();
    T getConfigurationWrapper(long id);
}
