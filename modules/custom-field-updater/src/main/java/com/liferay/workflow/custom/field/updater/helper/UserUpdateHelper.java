package com.liferay.workflow.custom.field.updater.helper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.workflow.custom.field.updater.configuration.model.CustomFieldPair;
import com.liferay.workflow.custom.field.updater.constants.CustomFieldUpdaterConstants;
import com.liferay.workflow.extensions.common.util.StringUtil;
import com.liferay.workflow.extensions.common.util.UserLookupHelper;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Component(immediate = true, service = EntityUpdateHelper.class)
public class UserUpdateHelper extends BaseUpdateHelper implements EntityUpdateHelper {

    @Reference
    private UserLocalService _userLocalService;

    public int getEntityType() {
        return CustomFieldUpdaterConstants.USER_UPDATE_HELPER;
    }

    private UserLookupHelper getUserLookupHelper() {
        return new UserLookupHelper();
    }

    @Override
    public boolean updateCustomFields(final User user, final long companyId, final String lookupType, final String lookupValue, final List<CustomFieldPair> customFields, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext) throws PortalException {
        WorkflowExtensionsUtil.setupPermissionChecker(user);

        if (StringUtil.isBlank(lookupValue)) {
            throw new PortalException("Unable to find the entity because the lookup value was blank");
        }

        final User entity = getUserLookupHelper().getUser(_userLocalService, companyId, lookupType, lookupValue);

        if (entity == null) {
            _log.warn("The user account could not be found. The update cannot take place");
            return false;
        }

        if (updateCustomFields(customFields, workflowContext, entity)) return false;

        _userLocalService.updateUser(entity);

        WorkflowExtensionsUtil.runIndexer(entity, serviceContext);

        return true;
    }
}
