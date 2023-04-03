package com.liferay.workflow.extensions.common.action.executor;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.configuration.BaseUserActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.configuration.BaseUserActionExecutorConfigurationWrapper;
import com.liferay.workflow.extensions.common.constants.UserActionExecutorConstants;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.settings.SettingsHelper;
import com.liferay.workflow.extensions.common.util.StringUtil;

import java.io.Serializable;
import java.util.Map;

public abstract class BaseWorkflowUserActionExecutor<C extends BaseUserActionExecutorConfiguration, W extends BaseUserActionExecutorConfigurationWrapper<C>, S extends SettingsHelper<C, W>> extends BaseWorkflowActionExecutor<C, W, S> implements ActionExecutor {

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final W configuration) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final User user;
            if (configuration.isInContextUserRequiredForAction()) {
                final long userId = GetterUtil.getLong((String) workflowContext.get(WorkflowConstants.CONTEXT_USER_ID));
                user = lookupUser(userId);
            } else {
                user = lookupUser(configuration, workflowContext);
            }
            if (user != null) {
                execute(kaleoAction, executionContext, workflowExecutionContext, configuration, user);
            } else {
                throw new ActionExecutorException("The action user was null for " + configuration.getIdentifier());
            }
        } catch (final ActionExecutorException e) {
            throw e;
        } catch (final PortalException e) {
            throw new ActionExecutorException("Unable to lookup the action user for " + configuration.getIdentifier() + ". See the inner exception", e);
        }
    }

    private User lookupUser(final long userId) throws PortalException {
        return getUserLocalService().getUserById(userId);
    }

    private User lookupUser(final W configuration, final Map<String, Serializable> workflowContext) throws PortalException {
        final long companyId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
        final String lookupValue;
        final String lookupType = configuration.getActionUserLookupType() != null
                ? configuration.getActionUserLookupType().toLowerCase()
                : UserActionExecutorConstants.CONFIG_ACTION_USER_LOOKUP_TYPE_DEFAULT;
        if (configuration.isWorkflowContextKeyUsedForActionUserLookup()) {
            final String workflowKey = configuration.getActionUserLookupValueWorkflowContextKey();
            lookupValue = workflowContext.containsKey(workflowKey) ?
                    String.valueOf(workflowContext.get(workflowKey)) :
                    StringPool.BLANK;
        } else {
            lookupValue = configuration.getActionUserLookupValue();
        }
        if (StringUtil.isBlank(lookupValue)) {
            throw new PortalException("Unable to find user because the lookup value was blank for " + configuration.getIdentifier());
        }
        switch (lookupType) {
            case "screen-name":
                return getUserLocalService().fetchUserByScreenName(companyId, lookupValue);
            case "email-address":
                return getUserLocalService().fetchUserByEmailAddress(companyId, lookupValue);
            case "user-id":
                final long userId = GetterUtil.getLong(lookupValue);
                return lookupUser(userId);
        }
        throw new PortalException("Unknown lookup type " + lookupType + " for " + configuration.getIdentifier());
    }

    @SuppressWarnings("unused")
    protected abstract void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final W configuration, final User actionUser) throws ActionExecutorException;

    protected abstract UserLocalService getUserLocalService();
}
