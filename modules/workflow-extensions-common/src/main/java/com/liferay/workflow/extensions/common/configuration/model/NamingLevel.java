package com.liferay.workflow.extensions.common.configuration.model;

@SuppressWarnings("rawtypes")
public interface NamingLevel<T extends Enum> {

    T decrementLevel();
}
