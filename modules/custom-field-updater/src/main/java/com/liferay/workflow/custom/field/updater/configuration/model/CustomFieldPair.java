package com.liferay.workflow.custom.field.updater.configuration.model;

public class CustomFieldPair {
    private String workflowContextKey;
    private String customFieldName;

    @Override
    public String toString() {
        return "CustomFieldPair{" +
                "workflowContextKey='" + workflowContextKey + '\'' +
                ", customFieldName='" + customFieldName + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomFieldPair)) return false;

        final CustomFieldPair that = (CustomFieldPair) o;

        if (getWorkflowContextKey() != null ? !getWorkflowContextKey().equals(that.getWorkflowContextKey()) : that.getWorkflowContextKey() != null)
            return false;
        return getCustomFieldName() != null ? getCustomFieldName().equals(that.getCustomFieldName()) : that.getCustomFieldName() == null;
    }

    @Override
    public int hashCode() {
        int result = getWorkflowContextKey() != null ? getWorkflowContextKey().hashCode() : 0;
        result = 31 * result + (getCustomFieldName() != null ? getCustomFieldName().hashCode() : 0);
        return result;
    }

    public String getCustomFieldName() {
        return customFieldName;
    }

    public void setCustomFieldName(final String customFieldName) {
        this.customFieldName = customFieldName;
    }

    public String getWorkflowContextKey() {
        return workflowContextKey;
    }

    public void setWorkflowContextKey(final String workflowContextKey) {
        this.workflowContextKey = workflowContextKey;
    }
}
