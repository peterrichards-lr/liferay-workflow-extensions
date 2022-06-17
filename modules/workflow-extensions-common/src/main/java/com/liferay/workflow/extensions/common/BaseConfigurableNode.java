package com.liferay.workflow.extensions.common;

import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.workflow.extensions.common.context.WorkflowExecutionContext;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;

import java.util.Arrays;

public abstract class BaseConfigurableNode<T extends com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper, S extends SettingsHelper<T>, W extends WorkflowExecutionContext> extends BaseNode<W> {
    protected abstract S getSettingsHelper();

    protected T getConfigurationWrapper(final String identifier) throws WorkflowException {
        final T config;
        final S settingsHelper = getSettingsHelper();
        if (settingsHelper == null) {
            throw new WorkflowException("SettingsHelper is null");
        }
        config = settingsHelper.getConfigurationWrapper(identifier);
        if (config == null) {
            throw new WorkflowException("Unable to retrieve configuration : " + identifier);
        }
        return config;
    }
}
