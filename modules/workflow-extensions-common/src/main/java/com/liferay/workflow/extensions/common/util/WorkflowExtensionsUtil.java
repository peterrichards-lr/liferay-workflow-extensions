package com.liferay.workflow.extensions.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.workflow.extensions.common.configuration.BaseConfigurationWrapper;
import com.liferay.workflow.extensions.common.configuration.GetConfigurationFunction;
import com.liferay.workflow.extensions.common.configuration.model.NamingLevel;
import com.liferay.workflow.extensions.common.configuration.model.WorkflowActionNamingLevel;
import com.liferay.workflow.extensions.common.configuration.model.WorkflowConditionNamingLevel;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.WorkflowConditionExecutionContext;
import com.liferay.workflow.extensions.common.context.WorkflowExecutionContext;
import org.jsoup.helper.StringUtil;

import javax.ws.rs.NotSupportedException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class WorkflowExtensionsUtil {

    public static String mapAsString(Map<String, ?> map) {
        return map.keySet().stream().map(key -> key + "=" + map.get(key)).collect(Collectors.joining(", ", "{", "}"));
    }

    public static <T> void runIndexer(T entity, ServiceContext serviceContext) throws SearchException {
        if ((serviceContext == null) || serviceContext.isIndexingEnabled()) {
            Indexer<T> indexer = IndexerRegistryUtil.nullSafeGetIndexer(
                    entity.getClass().getName());
            indexer.reindex(entity);
        }
    }

    public static String[] normaliseValue(String value) {
        if (value == null || "".equals(value)) {
            return new String[0];
        }
        try {
            return value.startsWith("[") && value.endsWith("]") ? WorkflowExtensionsConstants.DEFAULT_OBJECT_MAPPER.readValue(value, String[].class) : new String[]{value};
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static String hyphenateWhiteSpaces(String value) {
        return value.replaceAll("\\s", "-");
    }


    public static String buildConfigurationId(WorkflowActionExecutionContext executionContext) {
        return buildConfigurationId(executionContext, WorkflowExtensionsConstants.DEFAULT_WORKFLOW_ACTION_NAMING_LEVEL);
    }

    public static String buildConfigurationId(WorkflowActionExecutionContext executionContext, WorkflowActionNamingLevel namingLevel) {
        StringBuilder sb = new StringBuilder();

        if (namingLevel == WorkflowActionNamingLevel.WORKFLOW) {
            sb.append(buildConfigurationId((WorkflowExecutionContext) executionContext, false));
            return sb.toString().toLowerCase();
        }

        sb.append(buildConfigurationId((WorkflowExecutionContext) executionContext, true));

        if (namingLevel == WorkflowActionNamingLevel.ACTION && !StringUtil.isBlank(executionContext.getActionName())) {
            sb.append(StringPool.COLON);
            sb.append(WorkflowExtensionsUtil.hyphenateWhiteSpaces(executionContext.getActionName()));
        }

        return sb.toString().toLowerCase();
    }

    public static String buildConfigurationId(WorkflowExecutionContext executionContext, NamingLevel namingLevel) {
        if (executionContext instanceof WorkflowActionExecutionContext && namingLevel instanceof WorkflowActionNamingLevel) {
            return buildConfigurationId((WorkflowActionExecutionContext) executionContext, (WorkflowActionNamingLevel) namingLevel);
        } else if (executionContext instanceof WorkflowConditionExecutionContext && namingLevel instanceof WorkflowConditionNamingLevel) {
            return buildConfigurationId((WorkflowConditionExecutionContext) executionContext, (WorkflowConditionNamingLevel) namingLevel);
        } else {
            throw new NotSupportedException("Unsupported naming level enum");
        }
    }

    public static String buildConfigurationId(WorkflowConditionExecutionContext executionContext) {
        return buildConfigurationId(executionContext, WorkflowExtensionsConstants.DEFAULT_WORKFLOW_CONDITION_NAMING_LEVEL);
    }

    public static String buildConfigurationId(WorkflowConditionExecutionContext executionContext, WorkflowConditionNamingLevel namingLevel) {
        switch (namingLevel) {
            case WORKFLOW:
                return buildConfigurationId((WorkflowExecutionContext) executionContext, false);
            case NODE:
                return buildConfigurationId((WorkflowExecutionContext) executionContext, true);
            default:
                throw new NotSupportedException(String.format("%s is not a supported naming level", namingLevel.name()));
        }
    }

    private static String buildConfigurationId(WorkflowExecutionContext executionContext, boolean includeNodeName) {
        StringBuilder sb = new StringBuilder();
        if (!StringUtil.isBlank(executionContext.getWorkflowTitle())) {
            sb.append(WorkflowExtensionsUtil.hyphenateWhiteSpaces(executionContext.getWorkflowTitle()));
        }
        if (includeNodeName && !StringUtil.isBlank(executionContext.getNodeName())) {
            sb.append(StringPool.COLON);
            sb.append(WorkflowExtensionsUtil.hyphenateWhiteSpaces(executionContext.getNodeName()));
        }
        return sb.toString().toLowerCase();
    }

    public static String replaceTokens(String template, Map<String, Serializable> workflowContext) {
        final Pattern pattern = Pattern.compile(WorkflowExtensionsConstants.TOKEN_REGEX_STRING);
        final Matcher matcher = pattern.matcher(template);
        final StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            final String key = matcher.group(1);
            if (workflowContext.containsKey(key)) {
                String replacement = String.valueOf(workflowContext.get(key));
                builder.append(template, i, matcher.start());
                if (replacement == null)
                    builder.append(matcher.group(0));
                else
                    builder.append(replacement);
                i = matcher.end();
            }
        }
        builder.append(template.substring(i));
        return builder.toString();
    }

    public static boolean isJSONValid(String jsonInString) {
        try {
            WorkflowExtensionsConstants.DEFAULT_OBJECT_MAPPER.readTree(jsonInString);
            return jsonInString.contains("{") || jsonInString.contains("[");
        } catch (IOException e) {
            return false;
        }
    }

    public static <C extends WorkflowExecutionContext, W extends BaseConfigurationWrapper, N extends NamingLevel> W getConfiguration(C workflowExecutionContext, GetConfigurationFunction<W> getConfigurationWrapper, N namingLevel) {
        W configuration = null;
        do {
            String searchConfigurationId = WorkflowExtensionsUtil.buildConfigurationId(workflowExecutionContext, namingLevel);
            try {
                configuration = getConfigurationWrapper.apply(searchConfigurationId);
                if (configuration != null) {
                    continue;
                }
            } catch (WorkflowException e) {
                try {
                    namingLevel = (N) namingLevel.decrementLevel();
                } catch (UnsupportedOperationException ex) {
                    break;
                }
            }
        } while (configuration == null);
        return configuration;
    }
}
