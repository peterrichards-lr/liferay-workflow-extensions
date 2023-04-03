package com.liferay.workflow.extensions.common.configuration.model;

public class EntityCreationAttributeConfiguration {
    private String defaultValue;
    private String entityAttributeName;
    private boolean useWorkflowContextKey;
    private String workflowContextKey;

    @Override
    public int hashCode() {
        int result = getEntityAttributeName() != null ? getEntityAttributeName().hashCode() : 0;
        result = 31 * result + (isUseWorkflowContextKey() ? 1 : 0);
        result = 31 * result + (getWorkflowContextKey() != null ? getWorkflowContextKey().hashCode() : 0);
        result = 31 * result + (getDefaultValue() != null ? getDefaultValue().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityCreationAttributeConfiguration)) return false;
        final EntityCreationAttributeConfiguration that = (EntityCreationAttributeConfiguration) o;
        if (isUseWorkflowContextKey() != that.isUseWorkflowContextKey()) return false;
        if (getEntityAttributeName() != null ? !getEntityAttributeName().equals(that.getEntityAttributeName()) : that.getEntityAttributeName() != null)
            return false;
        if (getWorkflowContextKey() != null ? !getWorkflowContextKey().equals(that.getWorkflowContextKey()) : that.getWorkflowContextKey() != null)
            return false;
        return getDefaultValue() != null ? getDefaultValue().equals(that.getDefaultValue()) : that.getDefaultValue() == null;
    }

    @Override
    public String toString() {
        return "EntityCreationAttributeConfiguration{" +
                "entityAttribute='" + entityAttributeName + '\'' +
                ", useWorkflowContextKey=" + useWorkflowContextKey +
                ", workflowContextKey='" + workflowContextKey + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }

    public String getEntityAttributeName() {
        return entityAttributeName;
    }

    public boolean isUseWorkflowContextKey() {
        return useWorkflowContextKey;
    }

    public String getWorkflowContextKey() {
        return workflowContextKey;
    }

    public void setWorkflowContextKey(final String workflowContextKey) {
        this.workflowContextKey = workflowContextKey;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setUseWorkflowContextKey(final boolean useWorkflowContextKey) {
        this.useWorkflowContextKey = useWorkflowContextKey;
    }

    public void setEntityAttributeName(final String entityAttributeName) {
        this.entityAttributeName = entityAttributeName;
    }
}
