package com.liferay.workflow.custom.field.updater.helper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.workflow.custom.field.updater.configuration.model.CustomFieldPair;
import com.liferay.workflow.custom.field.updater.constants.CustomFieldUpdaterConstants;
import com.liferay.workflow.extensions.common.util.StringUtil;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Component(immediate = true, service = EntityUpdateHelper.class)
public class OrganisationUpdateHelper extends BaseUpdateHelper implements EntityUpdateHelper {

    @Reference
    private OrganizationLocalService _organizationLocalService;

    public int getEntityType() {
        return CustomFieldUpdaterConstants.ORGANISATION_UPDATE_HELPER;
    }

    @Override
    public boolean updateCustomFields(final User user, final long companyId, final String lookupType, final String lookupValue, final List<CustomFieldPair> customFields, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext) throws PortalException {
        WorkflowExtensionsUtil.setupPermissionChecker(user);

        if (StringUtil.isBlank(lookupValue)) {
            throw new PortalException("Unable to find the entity because the lookup value was blank");
        }

        final Organization entity = lookupEntity(companyId, lookupType, lookupValue);

        if (entity == null) {
            _log.warn("The organisation could not be found. The update cannot take place");
            return false;
        }

        if (updateCustomFields(customFields, workflowContext, entity)) return false;

        _organizationLocalService.updateOrganization(entity);

        WorkflowExtensionsUtil.runIndexer(entity, serviceContext);

        return true;
    }

    private Organization lookupEntity(final long companyId, final String lookupType, final String lookupValue) throws PortalException {
        final Organization entity;
        switch (lookupType) {
            case "organisation-name":
                entity = _organizationLocalService.fetchOrganization(companyId, lookupValue);
                break;
            case "external-reference":
                entity = _organizationLocalService.fetchOrganizationByExternalReferenceCode(lookupType, companyId);
                break;
            case "id":
                final long organisationId = GetterUtil.getLong(lookupValue);
                entity = _organizationLocalService.fetchOrganization(organisationId);
                break;
            default:
                throw new PortalException("Unknown lookup type: " + lookupType);
        }
        return entity;
    }
}
