package com.liferay.workflow.custom.field.updater.constants;

import com.liferay.portal.kernel.exception.PortalException;

public class CustomFieldUpdaterConstants {
    public static final String CONFIG_CUSTOM_FIELD_PAIRS_DEFAULT = "{ \"customFieldName\" : \"Preferred Contact Method\"\\, \"workflowContextKey\" : \"preferredContact\" }|{ \"customFieldName\" : \"Annual Turnover\"\\, \"workflowContextKey\" : \"annualTurnover\" }";
    public static final String CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_LOOKUP_VALUE_DEFAULT = "true";
    public static final String CONFIG_ENTITY_LOOKUP_VALUE_DEFAULT = "";
    public static final String CONFIG_ENTITY_LOOKUP_TYPE_DEFAULT = "email-address";
    public static final String CONFIG_ENTITY_LOOKUP_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT = "emailAddress";
    public static final String CONFIG_ENTITY_TYPE_DEFAULT = "user";

    public static final int USER_UPDATE_HELPER = 1;
    public static final int ORGANISATION_UPDATE_HELPER = 2;
    public static final int ACCOUNT_UPDATE_HELPER = 3;

    public static final String TYPE_USER_UPDATE_HELPER = "user";
    public static final String TYPE_ORGANISATION_UPDATE_HELPER = "organisation";
    public static final String TYPE_ACCOUNT_UPDATE_HELPER = "account";

    public static int getEntityType(final String label) throws PortalException {
        switch (label) {
            case TYPE_USER_UPDATE_HELPER:
                return USER_UPDATE_HELPER;
            case TYPE_ORGANISATION_UPDATE_HELPER:
                return ORGANISATION_UPDATE_HELPER;
            case TYPE_ACCOUNT_UPDATE_HELPER:
                return ACCOUNT_UPDATE_HELPER;
        }
        throw new PortalException("Unknown entity type: " + label);
    }
}
