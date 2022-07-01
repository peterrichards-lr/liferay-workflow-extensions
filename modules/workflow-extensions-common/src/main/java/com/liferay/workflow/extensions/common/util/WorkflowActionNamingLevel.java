package com.liferay.workflow.extensions.common.util;

public enum WorkflowActionNamingLevel {
    WORKFLOW {
        public WorkflowActionNamingLevel decrementLevel() {
            throw new UnsupportedOperationException("No lower level");
        }
    },
    NODE,
    ACTION {
        public WorkflowActionNamingLevel incrementLevel() {
            throw new UnsupportedOperationException("No higher level");
        }
    };

    static final WorkflowActionNamingLevel[] LEVELS = values();

    public WorkflowActionNamingLevel incrementLevel() {
        return LEVELS[ordinal() + 1];
    }

    public WorkflowActionNamingLevel decrementLevel() {
        return LEVELS[ordinal() - 1];
    }
}