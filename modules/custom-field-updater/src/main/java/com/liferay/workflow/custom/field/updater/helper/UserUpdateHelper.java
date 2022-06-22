package com.liferay.workflow.custom.field.updater.helper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.workflow.custom.field.updater.configuration.model.CustomFieldPair;
import com.liferay.workflow.custom.field.updater.constants.CustomFieldUpdaterConstants;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import org.jsoup.helper.StringUtil;
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

    @Override
    public boolean updateCustomFields(User user, long companyId, String lookupType, String lookupValue, List<CustomFieldPair> customFields, Map<String, Serializable> workflowContext, ServiceContext serviceContext) throws PortalException {
        setupPermissionChecker(user);

        if (StringUtil.isBlank(lookupValue)) {
            throw new PortalException("Unable to find the entity because the lookup value was blank");
        }

        final User entity = lookupEntity(companyId, lookupType, lookupValue);

        if (entity == null) {
            _log.warn("The user account could not be found. The update cannot take place");
            return false;
        }

        if (updateCustomFields(customFields, workflowContext, entity)) return false;

        _userLocalService.updateUser(entity);

        WorkflowExtensionsUtil.runIndexer(entity, serviceContext);

        return true;
    }

    private User lookupEntity(long companyId, String lookupType, String lookupValue) throws PortalException {
        final User entity;
        switch (lookupType) {
            case "screen-name":
                entity = _userLocalService.fetchUserByScreenName(companyId, lookupValue);
                break;
            case "email-address":
                entity = _userLocalService.fetchUserByEmailAddress(companyId, lookupValue);
                break;
            case "id":
                final long userId = GetterUtil.getLong(lookupValue);
                entity = _userLocalService.getUserById(userId);
                break;
            default:
                throw new PortalException("Unknown lookup type: " + lookupType);
        }
        return entity;
    }
}
