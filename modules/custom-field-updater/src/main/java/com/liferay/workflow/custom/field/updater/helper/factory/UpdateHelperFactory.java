package com.liferay.workflow.custom.field.updater.helper.factory;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.workflow.custom.field.updater.helper.EntityUpdateHelper;
import org.osgi.service.component.annotations.*;

import java.util.HashMap;
import java.util.Map;

@Component(service = UpdateHelperFactory.class)
public class UpdateHelperFactory {
    private final Map<Integer, EntityUpdateHelper> entityUpdateHelperMap = new HashMap<>();

    public EntityUpdateHelper getEntityUpdateHelper(int entityType) throws PortalException {
        final EntityUpdateHelper entityUpdateHelper = entityUpdateHelperMap.get(entityType);
        if (entityUpdateHelper == null) {
            throw new PortalException("Unknown entity type");
        }
        return entityUpdateHelper;
    }

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addEntityUpdateHelper(
            EntityUpdateHelper
                    entityUpdateHelper) {
        if (entityUpdateHelper != null) {
            entityUpdateHelperMap.put(entityUpdateHelper.getEntityType(), entityUpdateHelper);
        }
    }

    protected void removeEntityUpdateHelper(
            EntityUpdateHelper
                    entityUpdateHelper) {
        if (entityUpdateHelper != null) {
            entityUpdateHelperMap.remove(entityUpdateHelper.getEntityType());
        }
    }
}
