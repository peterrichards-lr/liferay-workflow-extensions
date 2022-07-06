package com.liferay.workflow.extensions.common.configuration.model;

public enum WorkflowConditionNamingLevel implements NamingLevel<WorkflowConditionNamingLevel> {
    WORKFLOW {
        public WorkflowConditionNamingLevel decrementLevel() {
            throw new UnsupportedOperationException("No lower level");
        }
    },
    NODE {
    };

    static final WorkflowConditionNamingLevel[] LEVELS = values();

    public WorkflowConditionNamingLevel decrementLevel() {
        return LEVELS[ordinal() - 1];
    }
}