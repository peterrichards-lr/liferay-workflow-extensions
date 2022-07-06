package com.liferay.workflow.user.group.roles.updater.helper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.workflow.user.group.roles.updater.configuration.UserGroupRolesUpdaterConfigurationWrapper;

import java.io.Serializable;
import java.util.Map;

public interface Helper {
    Integer getEntityType();

    boolean addUserGroupRoles(User actionUser, Map<String, Serializable> workflowContext, ServiceContext serviceContext, UserGroupRolesUpdaterConfigurationWrapper configuration) throws PortalException;
}
