package com.liferay.workflow.user.account.creator.action.executor;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserConstants;
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
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowEntityCreatorActionExecutor;
import com.liferay.workflow.extensions.common.configuration.model.MethodParameterConfiguration;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.util.EntityCreationAttributeUtil;
import com.liferay.workflow.extensions.common.util.StringUtil;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import com.liferay.workflow.user.account.creator.configuration.UserAccountCreatorConfiguration;
import com.liferay.workflow.user.account.creator.configuration.UserAccountCreatorConfigurationWrapper;
import com.liferay.workflow.user.account.creator.constants.UserAccountCreatorConstants;
import com.liferay.workflow.user.account.creator.settings.UserAccountCreatorSettingsHelper;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * @author peterrichards
 */
@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = UserAccountCreatorConfiguration.PID
)
public final class UserAccountCreator extends BaseWorkflowEntityCreatorActionExecutor<UserAccountCreatorConfiguration, UserAccountCreatorConfigurationWrapper, UserAccountCreatorSettingsHelper> implements ActionExecutor {
    @Reference
    private UserAccountCreatorSettingsHelper _userAccountCreatorSettingsHelper;
    @Reference
    private UserLocalService _userLocalService;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;
    @Reference
    private WorkflowStatusManager _workflowStatusManager;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private boolean createUserAccount(final User creator, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final UserAccountCreatorConfigurationWrapper configuration) throws PortalException {
        final long companyId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
        final Map<String, Object> methodParameters = buildMethodParametersMap(workflowContext, serviceContext, configuration);
        final String password = (String) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_PASSWORD);
        final boolean autoPassword =
                (boolean) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_AUTO_PASSWORD) ||
                        StringUtil.isBlank(password);
        final String screenName = (String) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_SCREEN_NAME);
        final boolean autoScreenName =
                (boolean) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_AUTO_SCREEN_NAME) ||
                        StringUtil.isBlank(screenName);
        final String emailAddress = (String) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_EMAIL_ADDRESS);
        final String localeString = (String) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_LOCALE);
        final Locale locale = StringUtil.isBlank(localeString) ? Locale.ROOT : Locale.forLanguageTag(localeString);
        final String firstName = (String) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_FIRST_NAME);
        final String middleName = (String) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_MIDDLE_NAME);
        final String lastName = (String) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_LAST_NAME);
        final long prefixId = (long) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_PREFIX_ID);
        final long suffixId = (long) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_SUFFIX_ID);
        final boolean male = (boolean) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_MALE);
        final String dobString = (String) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_DATE_OF_BIRTH);
        final Calendar dob = new GregorianCalendar();
        if (StringUtil.isBlank(dobString)) {
            dob.set(1970, Calendar.JANUARY, 1);
        } else {
            try {
                dob.setTime(WorkflowExtensionsConstants.SIMPLE_DATE_FORMAT.parse(dobString));
            } catch (final ParseException e) {
                dob.set(1970, Calendar.JANUARY, 1);
            }
        }
        _log.trace("Date of birth {}", dob);
        final String jobTitle = (String) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_JOB_TITLE);
        final long[] roleIds = EntityCreationAttributeUtil.unboxed((Long[]) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_ROLE_IDS));
        final long[] organisationIds = EntityCreationAttributeUtil.unboxed((Long[]) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_ORGANISATION_IDS));
        final long[] userGroupIds = EntityCreationAttributeUtil.unboxed((Long[]) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_USER_GROUP_IDS));
        final boolean sendEmail = (boolean) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_SEND_MAIL);
        final long[] groupIds;
        if (configuration.isUserAddedToCurrentSite()) {
            final long currentGroupId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_GROUP_ID));
            final Long[] configuredGroupIds = (Long[]) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_GROUP_IDS);
            final Set<Long> uniqueGroupIds;
            if (configuredGroupIds == null) {
                uniqueGroupIds = new HashSet<>() {{
                    add(currentGroupId);
                }};
            } else {
                uniqueGroupIds = new HashSet<>() {{
                    add(currentGroupId);
                    addAll(List.of(configuredGroupIds));
                }};
            }
            groupIds = EntityCreationAttributeUtil.unboxed(uniqueGroupIds.toArray(Long[]::new));
        } else {
            groupIds = EntityCreationAttributeUtil.unboxed((Long[]) methodParameters.get(UserAccountCreatorConstants.METHOD_PARAM_GROUP_IDS));
        }
        try {
            final User newUser = _userLocalService.addUser(creator.getUserId(), companyId, autoPassword, password, password, autoScreenName, screenName, emailAddress, locale, firstName, middleName, lastName, prefixId, suffixId, male, dob.get(Calendar.MONTH),
                    dob.get(Calendar.DATE), dob.get(Calendar.YEAR), jobTitle, UserConstants.TYPE_REGULAR, groupIds, organisationIds, roleIds, userGroupIds, sendEmail, serviceContext);
            WorkflowExtensionsUtil.runIndexer(newUser, serviceContext);
            if (newUser != null) {
                final String identifierWorkflowKey = configuration.getCreatedEntityIdentifierWorkflowContextKey();
                final long userId = newUser.getUserId();
                _log.debug("New user account created: {}", userId);
                workflowContext.put(identifierWorkflowKey, userId);
                return true;
            }
            _log.warn("The addUser returned null");
            return false;
        } catch (final PortalException e) {
            _log.error("Unable to create user", e);
            return false;
        }
    }

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final UserAccountCreatorConfigurationWrapper configuration, final User actionUser) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final ServiceContext serviceContext = executionContext.getServiceContext();
            final boolean success = createUserAccount(actionUser, workflowContext, serviceContext, configuration);
            if (configuration.isWorkflowStatusUpdatedOnSuccess() && success
            ) {
                updateWorkflowStatus(configuration.getSuccessWorkflowStatus(), workflowContext);
            }
        } catch (final PortalException | RuntimeException e) {
            if (configuration == null) {
                throw new ActionExecutorException("Unable to determine if workflow status is updated on exception. Configuration is null");
            } else if (configuration.isWorkflowStatusUpdatedOnException()) {
                _log.error("Unexpected exception. See inner exception for details", e);
                try {
                    updateWorkflowStatus(configuration.getExceptionWorkflowStatus(), workflowContext);
                } catch (final WorkflowException ex) {
                    throw new ActionExecutorException("See inner exception", ex);
                }
            } else {
                _log.error("Unexpected exception. See inner exception for details", e);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Map<String, MethodParameterConfiguration> getEntityCreationAttributeMap() {
        return new HashMap<>() {{
            put(UserAccountCreatorConstants.METHOD_PARAM_AUTO_PASSWORD, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_AUTO_PASSWORD, Boolean.class, false,
                    true));
            put(UserAccountCreatorConstants.METHOD_PARAM_PASSWORD, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_PASSWORD, String.class, false,
                    null));
            put(UserAccountCreatorConstants.METHOD_PARAM_AUTO_SCREEN_NAME, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_AUTO_SCREEN_NAME, Boolean.class, false,
                    true));
            put(UserAccountCreatorConstants.METHOD_PARAM_SCREEN_NAME, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_SCREEN_NAME, String.class, false,
                    null));
            put(UserAccountCreatorConstants.METHOD_PARAM_EMAIL_ADDRESS, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_EMAIL_ADDRESS, String.class, true,
                    null));
            put(UserAccountCreatorConstants.METHOD_PARAM_LOCALE, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_LOCALE, String.class, false,
                    "en-US"));
            put(UserAccountCreatorConstants.METHOD_PARAM_FIRST_NAME, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_FIRST_NAME, String.class, true,
                    null));
            put(UserAccountCreatorConstants.METHOD_PARAM_MIDDLE_NAME, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_MIDDLE_NAME, String.class, false,
                    null));
            put(UserAccountCreatorConstants.METHOD_PARAM_LAST_NAME, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_LAST_NAME, String.class, true,
                    null));
            put(UserAccountCreatorConstants.METHOD_PARAM_PREFIX_ID, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_PREFIX_ID, Long.class, false,
                    -1L));
            put(UserAccountCreatorConstants.METHOD_PARAM_SUFFIX_ID, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_SUFFIX_ID, Long.class, false,
                    -1L));
            put(UserAccountCreatorConstants.METHOD_PARAM_MALE, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_MALE, Boolean.class, false,
                    false));
            put(UserAccountCreatorConstants.METHOD_PARAM_JOB_TITLE, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_JOB_TITLE, String.class, false,
                    null));
            put(UserAccountCreatorConstants.METHOD_PARAM_GROUP_IDS, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_GROUP_IDS, Long[].class, false,
                    null));
            put(UserAccountCreatorConstants.METHOD_PARAM_ORGANISATION_IDS, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_ORGANISATION_IDS, Long[].class, false,
                    null));
            put(UserAccountCreatorConstants.METHOD_PARAM_ROLE_IDS, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_ROLE_IDS, Long[].class, false,
                    null));
            put(UserAccountCreatorConstants.METHOD_PARAM_USER_GROUP_IDS, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_USER_GROUP_IDS, Long[].class, false,
                    null));
            put(UserAccountCreatorConstants.METHOD_PARAM_SEND_MAIL, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_SEND_MAIL, Boolean.class, false,
                    false));
            put(UserAccountCreatorConstants.METHOD_PARAM_DATE_OF_BIRTH, new MethodParameterConfiguration(UserAccountCreatorConstants.METHOD_PARAM_DATE_OF_BIRTH, String.class, false,
                    null));
        }};
    }

    @Override
    protected UserAccountCreatorSettingsHelper getSettingsHelper() {
        return _userAccountCreatorSettingsHelper;
    }

    @Override
    protected UserLocalService getUserLocalService() {
        return _userLocalService;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }

    @Override
    protected WorkflowStatusManager getWorkflowStatusManager() {
        return _workflowStatusManager;
    }
}