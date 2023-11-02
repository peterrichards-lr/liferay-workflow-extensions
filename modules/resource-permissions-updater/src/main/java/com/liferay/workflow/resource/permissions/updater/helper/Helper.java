package com.liferay.workflow.resource.permissions.updater.helper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;
import java.util.Map;

public interface Helper {
    int getEntityType();

    boolean setResourcePermissions(User actionUser, long companyId, final long roleId, String lookupType, String lookupValue, String[] actionIds, Map<String, Serializable> workflowContext, ServiceContext serviceContext) throws PortalException;
}
