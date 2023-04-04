package com.liferay.workflow.twilio.whatsapp.notifier.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.twilio.whatsapp.notifier.constants.WhatsAppNotifierConstants;

@ExtendedObjectClassDefinition(
        category = "workflow", scope = ExtendedObjectClassDefinition.Scope.GROUP,
        factoryInstanceLabelAttribute = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID
)
@Meta.OCD(
        factory = true,
        id = WhatsAppNotifierConfiguration.PID,
        localization = "content/Language", name = "config-whatsapp-notifier-name",
        description = "config-whatsapp-notifier-description"
)
public interface WhatsAppNotifierConfiguration extends BaseActionExecutorConfiguration {
    String PID = "com.liferay.workflow.twilio.whatsapp.notifier.configuration.WhatsAppNotifierConfiguration";

    //@formatter:off
    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID_ACTION_DEFAULT,
            description = "config-workflow-node-identifier-description",
            id = WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID,
            name = "config-workflow-node-identifier-name",
            required = false
    )
    String identifier();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_ENABLE_DEFAULT,
            description = "config-enable-description",
            name = "config-enable-name",
            required = false
    )
    boolean enable();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_UPDATE_WORKFLOW_STATUS_ON_SUCCESS_DEFAULT,
            description = "config-update-workflow-status-on-success-description",
            name = "config-update-workflow-status-on-success-name",
            required = false
    )
    boolean updateWorkflowStatusOnSuccess();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_SUCCESS_WORKFLOW_STATUS_DEFAULT,
            description = "config-success-workflow-status-description",
            name = "config-success-workflow-status-name",
            required = false
    )
    String successWorkflowStatus();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_UPDATE_WORKFLOW_STATUS_ON_EXCEPTION_DEFAULT,
            description = "config-update-workflow-status-on-exception-description",
            name = "config-update-workflow-status-on-exception-name",
            required = false
    )
    boolean updateWorkflowStatusOnException();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_EXCEPTION_WORKFLOW_STATUS_DEFAULT,
            description = "config-exception-workflow-status-description",
            name = "config-exception-workflow-status-name",
            required = false
    )
    String exceptionWorkflowStatus();

    @Meta.AD(
            deflt = WhatsAppNotifierConstants.CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_SENDER_NUMBER_DEFAULT,
            description = "config-use-workflow-context-key-for-sender-number-description",
            name = "config-use-workflow-context-key-for-sender-number-name",
            required = false
    )
    boolean useWorkflowContextKeyForSenderNumber();

    @Meta.AD(
            deflt = WhatsAppNotifierConstants.CONFIG_SENDER_NUMBER_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-sender-number-workflow-context-key-description",
            name = "config-sender-number-workflow-context-key-name",
            required = false
    )
    String senderNumberWorkflowContextKey();

    @Meta.AD(
            deflt = WhatsAppNotifierConstants.CONFIG_SENDER_NUMBER_DEFAULT,
            description = "config-sender-number-description",
            name = "config-sender-number-name",
            required = false
    )
    String senderNumber();

    @Meta.AD(
            deflt = WhatsAppNotifierConstants.CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_RECIPIENT_NUMBER_DEFAULT,
            description = "config-use-workflow-context-key-for-recipient-number-description",
            name = "config-use-workflow-context-key-for-recipient-number-name",
            required = false
    )
    boolean useWorkflowContextKeyForRecipientNumber();

    @Meta.AD(
            deflt = WhatsAppNotifierConstants.CONFIG_RECIPIENT_NUMBER_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-recipient-number-workflow-context-key-description",
            name = "config-recipient-number-workflow-context-key-name",
            required = false
    )
    String recipientNumberWorkflowContextKey();

    @Meta.AD(
            deflt = WhatsAppNotifierConstants.CONFIG_RECIPIENT_NUMBER_DEFAULT,
            description = "config-recipient-number-description",
            name = "config-recipient-number-name",
            required = false
    )
    String recipientNumber();

    @Meta.AD(
            deflt = WhatsAppNotifierConstants.CONFIG_MESSAGE_TEMPLATE_DEFAULT,
            description = "config-message-template-description",
            name = "config-message-template-name",
            required = false
    )
    String messageTemplate();
    //@formatter:on
}
