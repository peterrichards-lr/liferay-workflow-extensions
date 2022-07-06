package com.liferay.workflow.extensions.common.configuration.model;

public enum WorkflowActionNamingLevel implements NamingLevel<WorkflowActionNamingLevel> {
    WORKFLOW {
        public WorkflowActionNamingLevel decrementLevel() {
            throw new UnsupportedOperationException("No lower level");
        }
    },
    NODE,
    ACTION {
    };

    static final WorkflowActionNamingLevel[] LEVELS = values();

    public WorkflowActionNamingLevel decrementLevel() {
        return LEVELS[ordinal() - 1];
    }
}