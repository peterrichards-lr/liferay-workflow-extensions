package com.liferay.workflow.twilio.whatsapp.notifier.action.executor;

import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowActionExecutor;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import com.liferay.workflow.twilio.whatsapp.notifier.configuration.WhatsAppNotifierConfiguration;
import com.liferay.workflow.twilio.whatsapp.notifier.configuration.WhatsAppNotifierConfigurationWrapper;
import com.liferay.workflow.twilio.whatsapp.notifier.constants.WhatsAppNotifierConstants;
import com.liferay.workflow.twilio.whatsapp.notifier.settings.WhatsAppNotifierSettingsHelper;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.jsoup.helper.StringUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.Map;

/**
 * @author peterrichards
 */
@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = WhatsAppNotifierConfiguration.PID
)
public class WhatsAppNotifier extends BaseWorkflowActionExecutor<WhatsAppNotifierConfiguration, WhatsAppNotifierConfigurationWrapper, WhatsAppNotifierSettingsHelper> implements  ActionExecutor {
    @Reference
    private WhatsAppNotifierSettingsHelper whatsAppNotifierSettingsHelper;

    @Reference
    private WorkflowActionExecutionContextService workflowActionExecutionContextService;

    @Override
    protected WhatsAppNotifierSettingsHelper getSettingsHelper() {
        return whatsAppNotifierSettingsHelper;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return workflowActionExecutionContextService;
    }

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final WhatsAppNotifierConfigurationWrapper configuration) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();

        initTwilio(configuration);

        try {
            final PhoneNumber sender = getSenderNumber(configuration, workflowContext);
            final PhoneNumber recipient = getRecipientNumber(configuration, workflowContext);
            final String messageBody = getMessageBody(configuration, workflowContext);

            final Message message = Message.creator(recipient, sender, messageBody).create();

            _log.info("Message sent : {}", message.getSid());
        } catch (final RuntimeException e) {
            throw new ActionExecutorException("Unexpected exception. See inner exception for details", e);
        }
    }

    private String getMessageBody(final WhatsAppNotifierConfigurationWrapper configuration, final Map<String, Serializable> workflowContext) {
        final String template = configuration.getMessageTemplate();
        return StringUtil.isBlank(template) ? "" :
                WorkflowExtensionsUtil.replaceTokens(template, workflowContext);
    }

    private PhoneNumber getSenderNumber(final WhatsAppNotifierConfigurationWrapper configuration, final Map<String, Serializable> workflowContext) {
        final boolean useWorkflowContextKey = configuration.isWorkflowContextKeyUsedForSenderNumber();
        final String workflowContextKey = configuration.getSenderNumberWorkflowKey();
        final String defaultValue = configuration.getSenderNumber();
        final String defaultCountryCode = configuration.getDefaultCountryCode();
        final String phoneNumber = useWorkflowContextKey ? getWorkflowValueOrDefault(workflowContextKey, defaultValue, workflowContext) : defaultValue;
        return buildPhoneNumber(phoneNumber, defaultCountryCode);
    }

    private PhoneNumber getRecipientNumber(final WhatsAppNotifierConfigurationWrapper configuration, final Map<String, Serializable> workflowContext) {
        final boolean useWorkflowContextKey = configuration.isWorkflowContextKeyUsedForRecipientNumber();
        final String workflowContextKey = configuration.getRecipientNumberWorkflowKey();
        final String defaultValue = configuration.getRecipientNumber();
        final String defaultCountryCode = configuration.getDefaultCountryCode();
        final String phoneNumber = useWorkflowContextKey ? getWorkflowValueOrDefault(workflowContextKey, defaultValue, workflowContext) : defaultValue;
        return buildPhoneNumber(phoneNumber, defaultCountryCode);
    }

    private String getWorkflowValueOrDefault(final String workflowKey, final String defaultValue, final Map<String, Serializable> workflowContext) {
        return workflowContext.containsKey(workflowKey) ? (String)workflowContext.get(workflowKey) : defaultValue;
    }

    private PhoneNumber buildPhoneNumber(final String phoneNumber, final String defaultCountryCode) {
        final String internationalNumber = phoneNumber.startsWith("0") ? phoneNumber.replaceFirst("0", defaultCountryCode) : phoneNumber;
        return new PhoneNumber(String.format("%s%s", WhatsAppNotifierConstants.WHATSAPP_NUMBER_PREFIX, internationalNumber));
    }

    private void initTwilio(final WhatsAppNotifierConfigurationWrapper configuration) throws ActionExecutorException {
        try {
            final String ACCOUNT_SID = configuration.getAccountSid();
            final String AUTH_TOKEN = configuration.getAuthToken();

            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        } catch (final RuntimeException e) {
            throw new ActionExecutorException("Unable to initialise Twilio. See inner exception for details", e);
        }
    }
}
