package com.liferay.workflow.custom.field.updater.helper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.workflow.custom.field.updater.configuration.model.CustomFieldPair;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface EntityUpdateHelper {
    int getEntityType();

    boolean updateCustomFields(User user, long companyId, String lookupType, String lookupValue, List<CustomFieldPair> customFields, Map<String, Serializable> workflowContext, ServiceContext serviceContext) throws PortalException;
}
