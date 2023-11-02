package com.liferay.workflow.resource.permissions.updater.helper;

import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.workflow.extensions.common.util.StringUtil;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import com.liferay.workflow.resource.permissions.updater.constants.ResourcePermissionsUpdaterConstants;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.Map;

public class CommerceCatalogPermissionsHelper extends BaseHelper implements Helper {
    @Reference
    private CommerceCatalogLocalService _commerceCatalogLocalService;
    @Reference
    private ResourcePermissionLocalService _resourcePermissionLocalService;

    @Override
    public int getEntityType() {
        return ResourcePermissionsUpdaterConstants.COMMERCE_CATALOG_PERMISSIONS_HELPER;
    }

    private CommerceCatalog lookupEntity(final long companyId, final String lookupType, final String lookupValue) throws PortalException {
        final CommerceCatalog entity;
        switch (lookupType) {
            case "external-reference":
                entity = _commerceCatalogLocalService.fetchByExternalReferenceCode(lookupValue, companyId);
                break;
            case "id":
                final long commerceCatalogId = GetterUtil.getLong(lookupValue);
                entity = _commerceCatalogLocalService.fetchCommerceCatalog(commerceCatalogId);
                break;
            default:
                throw new PortalException("Unknown lookup type: " + lookupType);
        }
        return entity;
    }

    @Override
    public boolean setResourcePermissions(final User actionUser, final long companyId, final long roleId, final String lookupType, final String lookupValue, final String[] actionIds, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext) throws PortalException {
        WorkflowExtensionsUtil.setupPermissionChecker(actionUser);
        if (StringUtil.isBlank(lookupValue)) {
            throw new PortalException("Unable to find the entity because the lookup value was blank");
        }
        final CommerceCatalog entity = lookupEntity(companyId, lookupType, lookupValue);
        if (entity == null) {
            _log.warn("The account entry could not be found. The update cannot take place");
            return false;
        }
        serviceContext.setUserId(actionUser.getUserId());
        _resourcePermissionLocalService.setResourcePermissions(
                companyId,
                entity.getModelClassName(),
                ResourceConstants.SCOPE_INDIVIDUAL,
                String.valueOf(entity.getPrimaryKey()),
                roleId,
                actionIds
        );

        WorkflowExtensionsUtil.runIndexer(entity, serviceContext);
        return true;
    }
}
