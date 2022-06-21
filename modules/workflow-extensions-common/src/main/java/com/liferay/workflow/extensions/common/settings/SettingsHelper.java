package com.liferay.workflow.extensions.common.settings;

import com.liferay.workflow.extensions.common.configuration.BaseConfiguration;
import com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper;

public interface SettingsHelper<C extends BaseConfiguration, T extends BaseConfigurationWrapper<C>> {
    boolean isEnabled(String identifier);

    String[] getIdentifiers();

    T getConfigurationWrapper(String identifier);
}
