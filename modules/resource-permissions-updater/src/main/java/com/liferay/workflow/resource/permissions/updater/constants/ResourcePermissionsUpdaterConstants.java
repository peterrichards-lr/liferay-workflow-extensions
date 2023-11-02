package com.liferay.workflow.resource.permissions.updater.constants;

import com.liferay.portal.kernel.exception.PortalException;

public class ResourcePermissionsUpdaterConstants {
    public static final int COMMERCE_CATALOG_PERMISSIONS_HELPER = 1;
    public static final String CONFIG_ACTION_IDENTIFIERS_DEFAULT = "VIEW|UPDATE";
    public static final String CONFIG_ENTITY_LOOKUP_TYPE_DEFAULT = "primary-key";
    public static final String CONFIG_ENTITY_LOOKUP_VALUE_DEFAULT = "";
    public static final String CONFIG_ENTITY_LOOKUP_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT = "newCatalogId";
    public static final String CONFIG_ENTITY_TYPE_DEFAULT = "commerce-catalog";
    public static final String CONFIG_ROLE_ID_DEFAULT = "0";
    public static final String CONFIG_ROLE_LOOKUP_TYPE_DEFAULT = "id";
    public static final String CONFIG_ROLE_LOOKUP_VALUE_DEFAULT = "";
    public static final String CONFIG_ROLE_LOOKUP_VALUE_WORKFLOW_CONTEXT_KEY_DEFAULT = "newRoleId";
    public static final String CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_ENTITY_LOOKUP_VALUE_DEFAULT = "true";
    public static final String CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_ROLE_LOOKUP_VALUE_DEFAULT = "true";
    public static final String TYPE_COMMERCE_CATALOG_PERMISSIONS_HELPER = "commerce-catalog";

    public static int getEntityType(final String label) throws PortalException {
        switch (label) {
            case TYPE_COMMERCE_CATALOG_PERMISSIONS_HELPER:
                return COMMERCE_CATALOG_PERMISSIONS_HELPER;
        }
        throw new PortalException("Unknown entity type: " + label);
    }
}
