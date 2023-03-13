package com.liferay.workflow.extensions.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.ListType;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ListTypeLocalServiceUtil;
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
import org.slf4j.Logger;

import javax.ws.rs.NotSupportedException;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class WorkflowExtensionsUtil {

    public static String mapAsString(final Map<String, ?> map) {
        return map.keySet().stream().map(key -> key + "=" + map.get(key)).collect(Collectors.joining(", ", "{", "}"));
    }

    public static <T> void runIndexer(final T entity, final ServiceContext serviceContext) throws SearchException {
        if ((serviceContext == null) || serviceContext.isIndexingEnabled()) {
            final Indexer<T> indexer = IndexerRegistryUtil.nullSafeGetIndexer(
                    entity.getClass().getName());
            indexer.reindex(entity);
        }
    }

    public static String[] normaliseValue(final String value) {
        if (value == null || "".equals(value)) {
            return new String[0];
        }
        try {
            return value.startsWith("[") && value.endsWith("]") ? WorkflowExtensionsConstants.DEFAULT_OBJECT_MAPPER.readValue(value, String[].class) : new String[]{value};
        } catch (final JsonProcessingException e) {
            return null;
        }
    }

    public static String hyphenateWhiteSpaces(final String value) {
        return value.replaceAll("\\s", "-");
    }


    public static String buildConfigurationId(final WorkflowActionExecutionContext executionContext) {
        return buildConfigurationId(executionContext, WorkflowExtensionsConstants.DEFAULT_WORKFLOW_ACTION_NAMING_LEVEL);
    }

    public static String buildConfigurationId(final WorkflowActionExecutionContext executionContext, final WorkflowActionNamingLevel namingLevel) {
        final StringBuilder sb = new StringBuilder();

        if (namingLevel == WorkflowActionNamingLevel.WORKFLOW) {
            sb.append(buildConfigurationId(executionContext, false));
            return sb.toString().toLowerCase();
        }

        sb.append(buildConfigurationId(executionContext, true));

        if (namingLevel == WorkflowActionNamingLevel.ACTION && !StringUtil.isBlank(executionContext.getActionName())) {
            sb.append(StringPool.COLON);
            sb.append(WorkflowExtensionsUtil.hyphenateWhiteSpaces(executionContext.getActionName()));
        }

        return sb.toString().toLowerCase();
    }

    @SuppressWarnings("rawtypes")
    public static String buildConfigurationId(final WorkflowExecutionContext executionContext, final NamingLevel namingLevel) {
        if (executionContext instanceof WorkflowActionExecutionContext && namingLevel instanceof WorkflowActionNamingLevel) {
            return buildConfigurationId((WorkflowActionExecutionContext) executionContext, (WorkflowActionNamingLevel) namingLevel);
        } else if (executionContext instanceof WorkflowConditionExecutionContext && namingLevel instanceof WorkflowConditionNamingLevel) {
            return buildConfigurationId((WorkflowConditionExecutionContext) executionContext, (WorkflowConditionNamingLevel) namingLevel);
        } else {
            throw new NotSupportedException("Unsupported naming level enum");
        }
    }

    public static String buildConfigurationId(final WorkflowConditionExecutionContext executionContext) {
        return buildConfigurationId(executionContext, WorkflowExtensionsConstants.DEFAULT_WORKFLOW_CONDITION_NAMING_LEVEL);
    }

    public static String buildConfigurationId(final WorkflowConditionExecutionContext executionContext, final WorkflowConditionNamingLevel namingLevel) {
        switch (namingLevel) {
            case WORKFLOW:
                return buildConfigurationId(executionContext, false);
            case NODE:
                return buildConfigurationId(executionContext, true);
            default:
                throw new NotSupportedException(String.format("%s is not a supported naming level", namingLevel.name()));
        }
    }

    private static String buildConfigurationId(final WorkflowExecutionContext executionContext, final boolean includeNodeName) {
        final StringBuilder sb = new StringBuilder();
        if (!StringUtil.isBlank(executionContext.getWorkflowTitle())) {
            sb.append(WorkflowExtensionsUtil.hyphenateWhiteSpaces(executionContext.getWorkflowTitle()));
        }
        if (includeNodeName && !StringUtil.isBlank(executionContext.getNodeName())) {
            sb.append(StringPool.COLON);
            sb.append(WorkflowExtensionsUtil.hyphenateWhiteSpaces(executionContext.getNodeName()));
        }
        return sb.toString().toLowerCase();
    }

    public static String replaceTokens(final String template, final Map<String, Serializable> workflowContext) {
        final Pattern pattern = Pattern.compile(WorkflowExtensionsConstants.TOKEN_REGEX_STRING);
        final Matcher matcher = pattern.matcher(template);
        final StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            final String key = matcher.group(1);
            if (workflowContext.containsKey(key)) {
                final String replacement = String.valueOf(workflowContext.get(key));
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

    public static boolean isJSONValid(final String jsonInString) {
        try {
            WorkflowExtensionsConstants.DEFAULT_OBJECT_MAPPER.readTree(jsonInString);
            return jsonInString.contains("{") || jsonInString.contains("[");
        } catch (final IOException e) {
            return false;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <C extends WorkflowExecutionContext, W extends BaseConfigurationWrapper, N extends NamingLevel> W getConfiguration(final C workflowExecutionContext, final GetConfigurationFunction<W> getConfigurationWrapper, N namingLevel) {
        W configuration = null;
        do {
            final String searchConfigurationId = WorkflowExtensionsUtil.buildConfigurationId(workflowExecutionContext, namingLevel);
            try {
                configuration = getConfigurationWrapper.apply(searchConfigurationId);
            } catch (final WorkflowException e) {
                try {
                    namingLevel = (N) namingLevel.decrementLevel();
                } catch (final UnsupportedOperationException ex) {
                    break;
                }
            }
        } while (configuration == null);
        return configuration;
    }

    public static long getTypeId(final String name, final String type) {
        final ListType listType = ListTypeLocalServiceUtil.getListType(name, type);
        return listType != null ? listType.getListTypeId() : 0L;
    }

    public static void setupPermissionChecker(final User user) {
        final PermissionChecker checker = PermissionCheckerFactoryUtil.create(user);
        PermissionThreadLocal.setPermissionChecker(checker);
    }

    public static void setupPrincipalThread(final User user) {
        PrincipalThreadLocal.setName(user.getUserId());
    }

    public static <T> List<T> getJsonConfigurationValuesAsList(final String[] jsonStringArray, final Class<T> type) {
        return getJsonConfigurationValuesAsList(jsonStringArray, type, null);
    }

    public static <T> List<T> getJsonConfigurationValuesAsList(final String[] jsonStringArray, final Class<T> type, final Logger log) {
        if (jsonStringArray != null && jsonStringArray.length > 0) {
            final List<T> jsonObjectList = new ArrayList<>(jsonStringArray.length);
            for (final String jsonString : jsonStringArray) {
                try {
                    final String normalisedJson = normaliseJson(jsonString);
                    final T jsonObject = WorkflowExtensionsConstants.DEFAULT_OBJECT_MAPPER.readValue(normalisedJson, type);
                    jsonObjectList.add(jsonObject);
                } catch (final JsonProcessingException e) {
                    if (log != null)
                        log.warn("Failed to parse JSON object : {}", jsonString);
                }
            }
            if (log != null)
                log.debug("jsonObjectList size is {}", jsonObjectList.size());
            return jsonObjectList;
        }
        return Collections.emptyList();
    }

    public static <T, R> Map<R, T> getJsonConfigurationValuesAsMap(final String[] jsonStringArray, final Class<T> type, final Function<T, R> keyFinder) {
        return getJsonConfigurationValuesAsMap(jsonStringArray, type, keyFinder, null);
    }

    public static <T, R> Map<R, T> getJsonConfigurationValuesAsMap(final String[] jsonStringArray, final Class<T> type, final Function<T, R> keyFinder, final Logger log) {
        if (jsonStringArray != null) {
            final Map<R, T> jsonObjectMap = new HashMap<>(jsonStringArray.length);
            for (final String jsonString : jsonStringArray) {
                try {
                    final String normalisedJson = normaliseJson(jsonString);
                    final T jsonObject = WorkflowExtensionsConstants.DEFAULT_OBJECT_MAPPER.readValue(normalisedJson, type);
                    jsonObjectMap.put(keyFinder.apply(jsonObject), jsonObject);
                } catch (final JsonProcessingException e) {
                    log.warn("Failed to parse JSON object : {}", jsonString);
                }
            }
            log.debug("jsonObjectMap size is {}", jsonObjectMap.size());
            return jsonObjectMap;
        }
        return Collections.emptyMap();
    }

    public static String normaliseJson(String jsonString) {
        return jsonString.replaceAll("\\,", ",");
    }
}
