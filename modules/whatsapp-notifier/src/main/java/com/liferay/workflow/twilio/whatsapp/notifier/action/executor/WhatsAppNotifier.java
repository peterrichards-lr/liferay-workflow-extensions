package com.liferay.workflow.twilio.whatsapp.notifier.action.executor;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.twilio.whatsapp.api.WhatsAppNotificationService;
import com.liferay.twilio.whatsapp.model.Notification;
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowActionExecutor;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import com.liferay.workflow.twilio.whatsapp.notifier.configuration.WhatsAppNotifierConfiguration;
import com.liferay.workflow.twilio.whatsapp.notifier.configuration.WhatsAppNotifierConfigurationWrapper;
import com.liferay.workflow.twilio.whatsapp.notifier.settings.WhatsAppNotifierSettingsHelper;
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
    protected WorkflowStatusManager getWorkflowStatusManager() {
        return null;
    }

    @Reference
    private WhatsAppNotificationService whatsAppNotificationService;

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final WhatsAppNotifierConfigurationWrapper configuration) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();

        try {
            final String sender = getSenderNumber(configuration, workflowContext);
            final String recipient = getRecipientNumber(configuration, workflowContext);
            final String messageBody = getMessageBody(configuration, workflowContext);

            whatsAppNotificationService.init();
            final Notification notification = whatsAppNotificationService.sendNotification(sender, recipient, messageBody);
            _log.info("Notification sent : {}", notification.getSid());

            if (configuration.isWorkflowStatusUpdatedOnSuccess()) {
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

    private String getMessageBody(final WhatsAppNotifierConfigurationWrapper configuration, final Map<String, Serializable> workflowContext) {
        final String template = configuration.getMessageTemplate();
        return Validator.isBlank(template) ? "" :
                WorkflowExtensionsUtil.replaceTokens(template, workflowContext);
    }

    private String getSenderNumber(final WhatsAppNotifierConfigurationWrapper configuration, final Map<String, Serializable> workflowContext) {
        final boolean useWorkflowContextKey = configuration.isWorkflowContextKeyUsedForSenderNumber();
        final String workflowContextKey = configuration.getSenderNumberWorkflowKey();
        final String defaultValue = configuration.getSenderNumber();
        return useWorkflowContextKey ? getWorkflowValueOrDefault(workflowContextKey, defaultValue, workflowContext) : defaultValue;
    }

    private String getRecipientNumber(final WhatsAppNotifierConfigurationWrapper configuration, final Map<String, Serializable> workflowContext) {
        final boolean useWorkflowContextKey = configuration.isWorkflowContextKeyUsedForRecipientNumber();
        final String workflowContextKey = configuration.getRecipientNumberWorkflowKey();
        final String defaultValue = configuration.getRecipientNumber();
        return useWorkflowContextKey ? getWorkflowValueOrDefault(workflowContextKey, defaultValue, workflowContext) : defaultValue;
    }

    private String getWorkflowValueOrDefault(final String workflowKey, final String defaultValue, final Map<String, Serializable> workflowContext) {
        return workflowContext.containsKey(workflowKey) ? (String)workflowContext.get(workflowKey) : defaultValue;
    }
}
