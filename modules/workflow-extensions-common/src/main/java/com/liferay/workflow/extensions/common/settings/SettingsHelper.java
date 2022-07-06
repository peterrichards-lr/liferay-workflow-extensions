package com.liferay.workflow.extensions.common.settings;

import com.liferay.workflow.extensions.common.configuration.BaseConfiguration;
import com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper;

public interface SettingsHelper<C extends BaseConfiguration, T extends BaseConfigurationWrapper<C>> {

    T getConfigurationWrapper(String identifier);
}
