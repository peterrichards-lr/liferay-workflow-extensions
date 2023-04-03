package com.liferay.workflow.custom.field.updater.helper;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.workflow.custom.field.updater.configuration.model.CustomFieldPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class BaseUpdateHelper {
    protected final Logger _log = LoggerFactory.getLogger(getClass());

    protected <T extends BaseModel<T>> boolean updateCustomFields(final List<CustomFieldPair> customFields, final Map<String, Serializable> workflowContext, final T entity) throws PortalException {
        for (final CustomFieldPair customFieldPair : customFields) {
            final String valueWorkflowContextKey = customFieldPair.getWorkflowContextKey();
            final String customFieldValue;
            if (workflowContext.containsKey(valueWorkflowContextKey)) {
                customFieldValue = GetterUtil.getString(workflowContext.get(valueWorkflowContextKey));
            } else {
                _log.debug("{} was not found in the workflowContext", valueWorkflowContextKey);
                return true;
            }
            final String customFieldName = customFieldPair.getCustomFieldName();
            _log.debug("Setting custom field {} to {}", customFieldName, customFieldValue);
            setCustomField(entity, customFieldName, customFieldValue);
        }
        return false;
    }

    protected <T extends BaseModel<T>> void setCustomField(final T entity, final String fieldName, final String value) throws PortalException {
        final ExpandoBridge expandoBridge = entity.getExpandoBridge();
        if (expandoBridge.hasAttribute(fieldName)) {
            final int type = expandoBridge.getAttributeType(fieldName);
            final Serializable serializableValue = ExpandoColumnConstants.getSerializable(type, value);
            expandoBridge.setAttribute(fieldName, serializableValue);
        } else {
            throw new PortalException("The entity does not have a custom field called " + fieldName);
        }
    }
}
