package com.liferay.workflow.extensions.common.settings;

public interface SettingsHelper<T extends com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper> {
    boolean isEnabled(String identifier);

    String[] getIdentifiers();

    T getConfigurationWrapper(String identifier);
}
