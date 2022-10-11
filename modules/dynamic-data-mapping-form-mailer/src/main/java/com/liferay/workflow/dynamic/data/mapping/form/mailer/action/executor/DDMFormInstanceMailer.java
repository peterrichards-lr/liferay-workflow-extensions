package com.liferay.workflow.dynamic.data.mapping.form.mailer.action.executor;

import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailService;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.dynamic.data.mapping.form.mailer.configuration.DDMFormInstanceMailerConfiguration;
import com.liferay.workflow.dynamic.data.mapping.form.mailer.configuration.DDMFormInstanceMailerConfigurationWrapper;
import com.liferay.workflow.dynamic.data.mapping.form.mailer.settings.DDMFormInstanceMailerSettingsHelper;
import com.liferay.workflow.extensions.common.action.executor.BaseDDFormActionExecutor;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.util.StringUtil;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.Serializable;
import java.util.Map;

/**
 * @author peterrichards
 */
@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = DDMFormInstanceMailerConfiguration.PID
)
public class DDMFormInstanceMailer extends BaseDDFormActionExecutor<DDMFormInstanceMailerConfiguration, DDMFormInstanceMailerConfigurationWrapper, DDMFormInstanceMailerSettingsHelper> implements ActionExecutor {
    @Reference
    private MailService _mailService;
    @Reference
    private DDMFormInstanceMailerSettingsHelper _ddmFormInstanceMailerSettingsHelper;
    @Reference
    private WorkflowStatusManager _workflowStatusManager;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;

    @Override
    protected DDMFormInstanceMailerSettingsHelper getSettingsHelper() {
        return _ddmFormInstanceMailerSettingsHelper;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final DDMFormInstanceMailerConfigurationWrapper configuration, final long formInstanceRecordVersionId) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();

        try {
            final boolean success = sendMail(workflowContext, configuration);
            if (configuration.isWorkflowStatusUpdatedOnSuccess() && success) {
                updateWorkflowStatus(configuration.getSuccessWorkflowStatus(), workflowContext);
            }
        } catch (final WorkflowException | RuntimeException e) {
            if (configuration.isWorkflowStatusUpdatedOnException()) {
                _log.error("Unexpected exception. See inner exception for details", e);
                try {
                    updateWorkflowStatus(configuration.getExceptionWorkflowStatus(), workflowContext);
                } catch (final WorkflowException ex) {
                    throw new ActionExecutorException("See inner exception", e);
                }
            } else {
                _log.error("Unexpected exception. See inner exception for details", e);
            }
        }
    }

    private boolean sendMail(final Map<String, Serializable> workflowContext, final DDMFormInstanceMailerConfigurationWrapper configuration) {
        try {
            final String sender = getSender(workflowContext, configuration);
            final String recipient = getRecipient(workflowContext, configuration);
            final String subject = getEmailSubject(workflowContext, configuration);
            final String body = getEmailBody(workflowContext, configuration);

            final MailMessage mailMessage = new MailMessage();
            mailMessage.setFrom(buildInternetAddress(sender));
            mailMessage.setTo(buildInternetAddress(recipient));
            mailMessage.setSubject(subject);
            mailMessage.setBody(body);

            _mailService.sendEmail(mailMessage);
            _log.debug("Sent email to {} from {}", recipient, sender);
            return true;
        } catch (final ActionExecutorException  e) {
            _log.error("Unable to send email. See inner exception for details", e);
            return false;
        }
    }

    private InternetAddress buildInternetAddress(final String emailAddress) throws ActionExecutorException {
        try {
            return new InternetAddress(emailAddress);
        } catch (final AddressException e) {
            throw new ActionExecutorException("Unable to create InternetAddress. See inner exception for details", e);
        }
    }

    private String getRecipient(final Map<String, Serializable> workflowContext, final DDMFormInstanceMailerConfigurationWrapper configuration) throws ActionExecutorException {
        final String recipientWorkflowContextKey = configuration.getRecipientEmailAddressWorkflowContextKey();
        if (StringUtil.isBlank(recipientWorkflowContextKey)) {
            throw new ActionExecutorException("The recipientWorkflowContextKey was blank");
        }
        if (!workflowContext.containsKey(recipientWorkflowContextKey)) {
            throw new ActionExecutorException(recipientWorkflowContextKey + " was not found in the workflowContext");
        }
        return String.valueOf(workflowContext.get(recipientWorkflowContextKey));
    }

    private String getSender(final Map<String, Serializable> workflowContext, final DDMFormInstanceMailerConfigurationWrapper configuration) throws ActionExecutorException {
        if (configuration.isWorkflowKeyUsedForSenderEmailAddress()) {
            final String senderWorkflowContextKey = configuration.getSenderEmailAddressWorkflowContextKey();
            if (StringUtil.isBlank(senderWorkflowContextKey)) {
                throw new ActionExecutorException("The recipientWorkflowContextKey was blank");
            }
            if (!workflowContext.containsKey(senderWorkflowContextKey)) {
                throw new ActionExecutorException(senderWorkflowContextKey + " was not found in the workflowContext");
            }
            return String.valueOf(workflowContext.get(senderWorkflowContextKey));
        }
        final String senderEmailAddress = configuration.getSenderEmailAddress();
        if (StringUtil.isBlank(senderEmailAddress)) {
            throw new ActionExecutorException("The senderEmailAddress was blank");
        }
        return senderEmailAddress;
    }

    private String getEmailSubject(final Map<String, Serializable> workflowContext, final DDMFormInstanceMailerConfigurationWrapper configuration) {
        final String template = configuration.getEmailSubjectTemplate();
        return buildFromTemplate(template, workflowContext);
    }

    private String getEmailBody(final Map<String, Serializable> workflowContext, final DDMFormInstanceMailerConfigurationWrapper configuration) {
        final String template = configuration.getEmailBodyTemplate();
        return buildFromTemplate(template, workflowContext);
    }

    private String buildFromTemplate(final String template, final Map<String, Serializable> workflowContext) {
        return StringUtil.isBlank(template) ? "" :
                WorkflowExtensionsUtil.replaceTokens(template, workflowContext);
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
        } catch (final WorkflowException e) {
            throw new WorkflowException("Unable to update workflow status", e);
        }
    }
}