package com.liferay.workflow.user.custom.field.updater.action.executor;


import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowActionExecutor;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.user.custom.field.updater.configuration.UserCustomFieldUpdaterConfiguration;
import com.liferay.workflow.user.custom.field.updater.configuration.UserCustomFieldUpdaterConfigurationWrapper;
import com.liferay.workflow.user.custom.field.updater.configuration.model.CustomFieldPair;
import com.liferay.workflow.user.custom.field.updater.settings.UserCustomFieldUpdaterSettingsHelper;
import org.jsoup.helper.StringUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = UserCustomFieldUpdaterConfiguration.PID
)
public final class UserCustomFieldUpdater extends BaseWorkflowActionExecutor<UserCustomFieldUpdaterConfiguration, UserCustomFieldUpdaterConfigurationWrapper, UserCustomFieldUpdaterSettingsHelper> implements ActionExecutor {

    @Reference
    private UserCustomFieldUpdaterSettingsHelper _ddmUserCustomFieldUpdaterSettingsHelper;
    @Reference
    private WorkflowStatusManager _workflowStatusManager;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;
    @Reference
    private UserLocalService _userLocalService;

    @Override
    protected UserCustomFieldUpdaterSettingsHelper getSettingsHelper() {
        return _ddmUserCustomFieldUpdaterSettingsHelper;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }

    @Override
    protected void execute(KaleoAction kaleoAction, ExecutionContext executionContext, WorkflowActionExecutionContext workflowExecutionContext, UserCustomFieldUpdaterConfigurationWrapper configuration) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final ServiceContext serviceContext = executionContext.getServiceContext();
            final boolean success = updateCustomField(configuration, workflowContext, serviceContext);
            if (configuration.isWorkflowStatusUpdatedOnSuccess() && success
            ) {
                updateWorkflowStatus(configuration.getSuccessWorkflowStatus(), workflowContext);
            }
        } catch (PortalException | RuntimeException e) {
            if (configuration == null) {
                throw new ActionExecutorException("Unable to determine if workflow status is updated on exception. Configuration is null");
            } else if (configuration.isWorkflowStatusUpdatedOnException()) {
                _log.error("Unexpected exception. See inner exception for details", e);
                try {
                    updateWorkflowStatus(configuration.getExceptionWorkflowStatus(), workflowContext);
                } catch (WorkflowException ex) {
                    throw new ActionExecutorException("See inner exception", ex);
                }
            } else {
                _log.error("Unexpected exception. See inner exception for details", e);
            }
        }
    }

    private boolean updateCustomField(UserCustomFieldUpdaterConfigurationWrapper configuration, Map<String, Serializable> workflowContext, ServiceContext serviceContext) throws PortalException {
        final User user;
        if (configuration.isInContextUserRequired()) {
            final long userId = GetterUtil.getLong((String) workflowContext.get(WorkflowConstants.CONTEXT_USER_ID));
            user = lookupUser(userId);
        } else {
            user = lookupUser(configuration, workflowContext);
        }

        setupPermissionChecker(user);

        final List<CustomFieldPair> customFieldPairList = configuration.getCustomFieldPairsList();
        for (CustomFieldPair customFieldPair : customFieldPairList) {

            final String valueWorkflowContextKey = customFieldPair.getWorkflowContextKey();
            final String customFieldValue;
            if (workflowContext.containsKey(valueWorkflowContextKey)) {
                customFieldValue = GetterUtil.getString(workflowContext.get(valueWorkflowContextKey));
            } else {
                _log.debug("{} was not found in the workflowContext", valueWorkflowContextKey);
                return false;
            }
            final String customFieldName = customFieldPair.getCustomFieldName();

            _log.debug("Setting custom field {} to {}", customFieldName, customFieldValue);
            setCustomField(user, customFieldName, customFieldValue);
        }

        _userLocalService.updateUser(user);

        runIndexer(user, serviceContext);

        return true;
    }

    private void runIndexer(User user, ServiceContext serviceContext) throws SearchException {
        if ((serviceContext == null) || serviceContext.isIndexingEnabled()) {
            Indexer<User> indexer = IndexerRegistryUtil.nullSafeGetIndexer(
                    User.class);
            indexer.reindex(user);
        }
    }

    private void setCustomField(User user, String fieldName, String value) throws PortalException {
        ExpandoBridge userExpandoBridge = user.getExpandoBridge();
        if (userExpandoBridge.hasAttribute(fieldName)) {
            final int type = userExpandoBridge.getAttributeType(fieldName);
            final Serializable serializableValue = ExpandoColumnConstants.getSerializable(type, value);
            userExpandoBridge.setAttribute(fieldName, serializableValue);
        } else {
            throw new PortalException("The user object does not have a custom field called " + fieldName);
        }
    }

    private void setupPermissionChecker(User user) {
        PermissionChecker checker = PermissionCheckerFactoryUtil.create(user);
        PermissionThreadLocal.setPermissionChecker(checker);
    }

    private User lookupUser(UserCustomFieldUpdaterConfigurationWrapper configuration, Map<String, Serializable> workflowContext) throws PortalException {
        final long companyIc = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
        final String lookupValue;
        final String lookupType = configuration.getUserLookupType().toLowerCase();
        if (configuration.isWorkflowContextKeyUsedForLookup()) {
            final String workflowKey = configuration.getUserLookupValueWorkflowContextKey();
            lookupValue = workflowContext.containsKey(workflowKey) ?
                    GetterUtil.getString(workflowContext.get(workflowKey)) :
                    StringPool.BLANK;
        } else {
            lookupValue = configuration.getUserLookupValue();
        }

        if (StringUtil.isBlank(lookupValue)) {
            throw new PortalException("Unable to find user because the lookup value was blank");
        }

        switch (lookupType) {
            case "screen-name":
                return _userLocalService.fetchUserByScreenName(companyIc, lookupValue);
            case "email-address":
                return _userLocalService.fetchUserByEmailAddress(companyIc, lookupValue);
            case "user-id":
                final long userId = GetterUtil.getLong(lookupValue);
                return lookupUser(userId);
        }
        throw new PortalException("Unknown lookup type: " + lookupType);
    }

    private User lookupUser(long userId) throws PortalException {
        return _userLocalService.getUserById(userId);
    }

    private void updateWorkflowStatus(final int status, final Map<String, Serializable> workflowContext) throws WorkflowException {
        try {
            if (status > -1) {
                if (_log.isDebugEnabled()) {
                    final String workflowLabelStatus = WorkflowConstants.getStatusLabel(status);
                    _log.debug("Setting workflow status to {} [{}]", workflowLabelStatus, status);
                }
                _workflowStatusManager.updateStatus(status, workflowContext);
            }
        } catch (WorkflowException e) {
            throw new WorkflowException("Unable to update workflow status", e);
        }
    }
}
