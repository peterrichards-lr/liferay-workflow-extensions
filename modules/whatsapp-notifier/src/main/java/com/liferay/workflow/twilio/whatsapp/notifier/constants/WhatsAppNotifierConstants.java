package com.liferay.workflow.twilio.whatsapp.notifier.constants;

public class WhatsAppNotifierConstants {
    public static final String WHATSAPP_NUMBER_PREFIX = "whatsapp:";

    public static final String CONFIG_ACCOUNT_SID_DEFAULT = "";

    public static final String CONFIG_AUTH_TOKEN_DEFAULT = "";
    public static final String CONFIG_DEFAULT_COUNTRY_CODE_DEFAULT = "+44";
    public static final String CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_SENDER_NUMBER_DEFAULT = "false";

    public static final String CONFIG_SENDER_NUMBER_WORKFLOW_CONTEXT_KEY_DEFAULT = "senderPhoneNumber";
    public static final String CONFIG_SENDER_NUMBER_DEFAULT = "+";
    public static final String CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_RECIPIENT_NUMBER_DEFAULT = "true";
    public static final String CONFIG_RECIPIENT_NUMBER_WORKFLOW_CONTEXT_KEY_DEFAULT = "phoneNumber";
    public static final String CONFIG_RECIPIENT_NUMBER_DEFAULT = "";
    public static final String CONFIG_MESSAGE_TEMPLATE_DEFAULT = "Hello ${forename}, you have successfully registered. Please check your email for further instructions";
}
