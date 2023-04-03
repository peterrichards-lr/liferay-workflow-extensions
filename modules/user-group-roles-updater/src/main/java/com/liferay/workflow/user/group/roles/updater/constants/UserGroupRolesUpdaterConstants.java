package com.liferay.workflow.user.group.roles.updater.constants;

import com.liferay.portal.kernel.exception.PortalException;

public class UserGroupRolesUpdaterConstants {
    public static final String CONFIG_GROUP_ID_TYPE_SITE = "site";
    public static final String CONFIG_GROUP_ID_TYPE_ACCOUNT_ENTRY = "account-entry";
    public static final String CONFIG_GROUP_ID_TYPE_ORGANISATION = "organization";

    public static final int SITE_HELPER = 1;
    public static final int ACCOUNT_HELPER = 2;
    public static final int ORGANISATION_HELPER = 3;
    public static final String CONFIG_USE_IN_CONTEXT_GROUP_ID_DEFAULT = "true";
    public static final String CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_GROUP_ID_DEFAULT = "true";
    public static final String CONFIG_GROUP_ID_LOOKUP_WORKFLOW_CONTEXT_KEY_DEFAULT = "groupId";
    public static final String CONFIG_GROUP_ID_LOOKUP_VALUE_DEFAULT = "";
    public static final String CONFIG_GROUP_ID_LOOKUP_VALUE_TYPE_GROUP_ID = "group-id";
    public static final String CONFIG_GROUP_ID_LOOKUP_VALUE_TYPE_ENTITY = "entity";
    public static final String CONFIG_GROUP_ID_LOOKUP_VALUE_TYPE_DEFAULT = CONFIG_GROUP_ID_LOOKUP_VALUE_TYPE_ENTITY;
    public static final String CONFIG_GROUP_ID_TYPE_DEFAULT = CONFIG_GROUP_ID_TYPE_SITE;
    public static final String CONFIG_USE_IN_CONTEXT_USER_DEFAULT = "true";
    public static final String CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_USER_LOOKUP_VALUE_DEFAULT = "false";
    public static final String CONFIG_USER_LOOKUP_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT = "emailAddress";
    public static final String CONFIG_USER_LOOKUP_VALUE_DEFAULT = "";
    public static final String CONFIG_USER_LOOKUP_TYPE_DEFAULT = "email-address";
    public static final String CONFIG_ROLE_ARRAY_DEFAULT = "";

    public static int getHelperType(final String label) throws PortalException {
        switch (label) {
            case CONFIG_GROUP_ID_TYPE_SITE:
                return SITE_HELPER;
            case CONFIG_GROUP_ID_TYPE_ACCOUNT_ENTRY:
                return ACCOUNT_HELPER;
            case CONFIG_GROUP_ID_TYPE_ORGANISATION:
                return ORGANISATION_HELPER;
        }
        throw new PortalException("Unknown entity type: " + label);
    }
}
