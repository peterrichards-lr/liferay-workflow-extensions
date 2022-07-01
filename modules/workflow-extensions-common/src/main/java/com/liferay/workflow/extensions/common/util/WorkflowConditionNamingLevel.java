package com.liferay.workflow.extensions.common.util;

public enum WorkflowConditionNamingLevel {
    WORKFLOW {
        public WorkflowConditionNamingLevel decrementLevel() {
            throw new UnsupportedOperationException("No lower level");
        }
    },
    NODE {
        public WorkflowConditionNamingLevel incrementLevel() {
            throw new UnsupportedOperationException("No higher level");
        }
    };

    static final WorkflowConditionNamingLevel[] LEVELS = values();

    public WorkflowConditionNamingLevel incrementLevel() {
        return LEVELS[ordinal() + 1];
    }

    public WorkflowConditionNamingLevel decrementLevel() {
        return LEVELS[ordinal() - 1];
    }
}