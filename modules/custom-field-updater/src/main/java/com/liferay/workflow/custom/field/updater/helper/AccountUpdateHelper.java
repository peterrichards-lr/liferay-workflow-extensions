package com.liferay.workflow.custom.field.updater.helper;

import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
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
public class AccountUpdateHelper extends BaseUpdateHelper implements EntityUpdateHelper {

    @Reference
    private AccountEntryLocalService _accountEntryLocalService;

    public int getEntityType() {
        return CustomFieldUpdaterConstants.ACCOUNT_UPDATE_HELPER;
    }

    @Override
    public boolean updateCustomFields(User user, long companyId, String lookupType, String lookupValue, List<CustomFieldPair> customFields, Map<String, Serializable> workflowContext, ServiceContext serviceContext) throws PortalException {
        setupPermissionChecker(user);

        if (StringUtil.isBlank(lookupValue)) {
            throw new PortalException("Unable to find the entity because the lookup value was blank");
        }

        final AccountEntry entity = lookupEntity(companyId, lookupType, lookupValue);

        if (entity == null) {
            _log.warn("The account entry could not be found. The update cannot take place");
            return false;
        }

        if (updateCustomFields(customFields, workflowContext, entity)) return false;

        _accountEntryLocalService.updateAccountEntry(entity);

        WorkflowExtensionsUtil.runIndexer(entity, serviceContext);

        return true;
    }

    private AccountEntry lookupEntity(long companyId, String lookupType, String lookupValue) throws PortalException {
        final AccountEntry entity;
        switch (lookupType) {
            case "external-reference":
                entity = _accountEntryLocalService.fetchAccountEntryByExternalReferenceCode(companyId, lookupValue);
                break;
            case "id":
                final long accountEntityId = GetterUtil.getLong(lookupValue);
                entity = _accountEntryLocalService.fetchAccountEntry(accountEntityId);
                break;
            default:
                throw new PortalException("Unknown lookup type: " + lookupType);
        }
        return entity;
    }
}