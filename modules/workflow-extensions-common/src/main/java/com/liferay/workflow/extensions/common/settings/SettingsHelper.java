package com.liferay.workflow.extensions.common.settings;

import com.liferay.workflow.extensions.common.configuration.BaseFormConfigurationWrapper;

public interface SettingsHelper<T extends BaseFormConfigurationWrapper> {
    boolean isEnabled(long id);

    long[] getFormInstanceIdentifiers();

    T getConfigurationWrapper(long id);
}
