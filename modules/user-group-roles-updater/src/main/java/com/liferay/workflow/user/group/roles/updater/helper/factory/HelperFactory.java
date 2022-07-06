package com.liferay.workflow.user.group.roles.updater.helper.factory;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.workflow.user.group.roles.updater.helper.Helper;
import org.osgi.service.component.annotations.*;

import java.util.HashMap;
import java.util.Map;

@Component(service = HelperFactory.class)
public class HelperFactory {

    private final Map<Integer, Helper> entityUpdateHelperMap = new HashMap<>();

    public Helper getHelper(final int entityType) throws PortalException {
        final Helper entityUpdateHelper = entityUpdateHelperMap.get(entityType);
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
    protected void addHelper(
            final Helper
                    entityUpdateHelper) {
        if (entityUpdateHelper != null) {
            entityUpdateHelperMap.put(entityUpdateHelper.getEntityType(), entityUpdateHelper);
        }
    }

    @SuppressWarnings("unused")
    protected void removeHelper(
            final Helper
                    entityUpdateHelper) {
        if (entityUpdateHelper != null) {
            entityUpdateHelperMap.remove(entityUpdateHelper.getEntityType());
        }
    }
}
