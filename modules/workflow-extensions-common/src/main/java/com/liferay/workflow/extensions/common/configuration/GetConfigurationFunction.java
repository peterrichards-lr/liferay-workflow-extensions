package com.liferay.workflow.extensions.common.configuration;

import com.liferay.portal.kernel.workflow.WorkflowException;

@FunctionalInterface
public interface GetConfigurationFunction<R extends BaseConfigurationWrapper> {
    R apply(String t) throws WorkflowException;
}
