package com.liferay.workflow.dynamic.data.mapping.form.options.translator.configuration.model;

import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;

import java.util.Collections;
import java.util.Map;

public class OptionTranslation {
    private String workflowContextKey;
    private Map<String, String> translationMap;

    @Override
    public String toString() {
        return "OptionTranslation{" +
                "workflowContextKey='" + workflowContextKey + '\'' +
                ", translationMap=[ " + WorkflowExtensionsUtil.mapAsString(translationMap) +
                " ]}";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof OptionTranslation)) return false;

        final OptionTranslation that = (OptionTranslation) o;

        if (getWorkflowContextKey() != null ? !getWorkflowContextKey().equals(that.getWorkflowContextKey()) : that.getWorkflowContextKey() != null)
            return false;
        return getTranslationMap() != null ? getTranslationMap().equals(that.getTranslationMap()) : that.getTranslationMap() == null;
    }

    @Override
    public int hashCode() {
        int result = getWorkflowContextKey() != null ? getWorkflowContextKey().hashCode() : 0;
        result = 31 * result + (getTranslationMap() != null ? getTranslationMap().hashCode() : 0);
        return result;
    }

    public String getWorkflowContextKey() {
        return workflowContextKey;
    }

    public void setWorkflowContextKey(final String workflowContextKey) {
        this.workflowContextKey = workflowContextKey;
    }

    public Map<String, String> getTranslationMap() {
        return Collections.unmodifiableMap(translationMap);
    }

    public void setTranslationMap(final Map<String, String> translationMap) {
        this.translationMap = translationMap;
    }
}
