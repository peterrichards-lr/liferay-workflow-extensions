package com.liferay.workflow.extensions.common.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.workflow.extensions.common.configuration.UserLookupConfiguration;

import java.io.Serializable;
import java.util.Map;

public class UserLookupHelper {
    private UserLocalService userLocalService;

    public UserLookupHelper(final UserLocalService userLocalService) {
        this.userLocalService = userLocalService;
    }

    public long lookupUserId(final long companyId, final Map<String, Serializable> workflowContext, final UserLookupConfiguration configuration) throws PortalException {
        final String lookupType = configuration.getUserLookupType();

        final User user;
        if (configuration.isInContextUserRequired()) {
            final long userId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_USER_ID));
            user = userLocalService.getUserById(userId);
        } else {
            final String userLookupValue = getUserLookupValue(configuration, workflowContext);
            user = getUser(companyId, lookupType, userLookupValue);
        }

        if (user == null) {
            throw new PortalException("Unable to obtain user id " + configuration.getIdentifier());
        }

        return user.getUserId();
    }

    public User getUser(final long companyId, final String lookupType, final String lookupValue) throws PortalException {
        final User entity;
        switch (lookupType) {
            case "screen-name":
                entity = userLocalService.fetchUserByScreenName(companyId, lookupValue);
                break;
            case "email-address":
                entity = userLocalService.fetchUserByEmailAddress(companyId, lookupValue);
                break;
            case "id":
                final long userId = GetterUtil.getLong(lookupValue);
                entity = userLocalService.getUserById(userId);
                break;
            default:
                throw new PortalException("Unknown lookup type: " + lookupType);
        }
        return entity;
    }

    public String getUserLookupValue(final UserLookupConfiguration configuration, final Map<String, Serializable> workflowContext) throws PortalException {
        if (configuration.isWorkflowContextKeyUsedForUserLookup()) {
            final String workflowContextKey = configuration.getUserLookupValueWorkflowContextKey();
            if (workflowContext.containsKey(workflowContextKey)) {
                return String.valueOf(workflowContext.get(workflowContextKey));
            }
            throw new PortalException(workflowContextKey + " was not found in the workflow context");
        }
        return configuration.getUserLookupValue();
    }
}
