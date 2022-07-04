package com.liferay.workflow.extensions.common.configuration.model;

public interface NamingLevel<T extends Enum> {
    T incrementLevel();

    T decrementLevel();
}
