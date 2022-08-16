package com.liferay.workflow.commerce.orders.remapper.account.executor;

import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.commerce.orders.remapper.configuration.CommerceOrdersRemapperConfiguration;
import com.liferay.workflow.commerce.orders.remapper.configuration.CommerceOrdersRemapperConfigurationWrapper;
import com.liferay.workflow.commerce.orders.remapper.settings.CommerceOrdersRemapperSettingsHelper;
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowUserActionExecutor;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.util.UserLookupHelper;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author peterrichards
 */
@Component(property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java", service = ActionExecutor.class, configurationPid = CommerceOrdersRemapperConfiguration.PID)
public class CommerceOrdersRemapper extends BaseWorkflowUserActionExecutor<CommerceOrdersRemapperConfiguration, CommerceOrdersRemapperConfigurationWrapper, CommerceOrdersRemapperSettingsHelper> {

    @Reference
    private CommerceOrdersRemapperSettingsHelper commerceOrdersRemapperSettingsHelper;

    @Reference
    private UserLocalService userLocalService;

    @Reference
    private AccountEntryLocalService accountEntryLocalService;

    @Reference
    private CommerceOrderLocalService commerceOrderLocalService;

    @Reference
    private WorkflowActionExecutionContextService workflowActionExecutionContextService;

    @Reference
    private WorkflowStatusManager _workflowStatusManager;

    @Override
    protected CommerceOrdersRemapperSettingsHelper getSettingsHelper() {
        return commerceOrdersRemapperSettingsHelper;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return workflowActionExecutionContextService;
    }

    @Override
    protected UserLocalService getUserLocalService() {
        return userLocalService;
    }

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final CommerceOrdersRemapperConfigurationWrapper configuration, final User actionUser) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final boolean success = remapCommerceOrders(workflowContext, configuration);
            if (configuration.isWorkflowStatusUpdatedOnSuccess() && success
            ) {
                updateWorkflowStatus(configuration.getSuccessWorkflowStatus(), workflowContext);
            }
        } catch (final PortalException | RuntimeException e) {
            if (configuration == null) {
                throw new ActionExecutorException("Unable to determine if workflow status is updated on exception. Configuration is null");
            } else if (configuration.isWorkflowStatusUpdatedOnException()) {
                _log.error("Unexpected exception. See inner exception for details", e);
                try {
                    updateWorkflowStatus(configuration.getExceptionWorkflowStatus(), workflowContext);
                } catch (final WorkflowException ex) {
                    throw new ActionExecutorException("See inner exception", ex);
                }
            } else {
                _log.error("Unexpected exception. See inner exception for details", e);
            }
        }
    }

    private UserLookupHelper getUserLookupHelper() {
        return new UserLookupHelper();
    }

    private boolean remapCommerceOrders(final Map<String, Serializable> workflowContext, final CommerceOrdersRemapperConfigurationWrapper configuration) {
        final long companyId = GetterUtil.getLong(workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));

        final long userId;
        try {
            userId = getUserLookupHelper().lookupUserId(userLocalService, companyId, workflowContext, configuration);
        } catch (final PortalException e) {
            _log.warn("Unable to lookup user", e);
            return false;
        }

        final AccountEntry personalAccount = accountEntryLocalService.fetchPersonAccountEntry(userId);
        if (personalAccount == null) {
            _log.debug("User does not have a personal account");
            return true;
        }

        final long commerceAccountId = personalAccount.getAccountEntryId();

        if (commerceOrderLocalService.getCommerceOrdersCountByCommerceAccountId(commerceAccountId) == 0) {
            _log.debug("User does not have any commerce orders");
            return true;
        }

        final int start = -1;
        final int end = -1;
        final OrderByComparator<CommerceOrder> orderByComparator = getOrderByComparator();

        final List<CommerceOrder> commerceOrders = commerceOrderLocalService.getCommerceOrdersByCommerceAccountId(
                commerceAccountId, start, end, orderByComparator);

        try {
            for (final CommerceOrder commerceOrder : commerceOrders) {
                _log.debug(commerceOrder.getCommerceAccountName());
            }
        } catch (final PortalException e) {
            _log.error("Unable to get account name", e);
            return false;
        }

        return true;
    }

    private OrderByComparator<CommerceOrder> getOrderByComparator() {
        return OrderByComparatorFactoryUtil.create(
                "CommerceOrder", "mvccVersion", true, "uuid", true,
                "externalReferenceCode", true, "commerceOrderId", true, "groupId",
                true, "companyId", true, "userId", true, "userName", true,
                "createDate", true, "modifiedDate", true, "billingAddressId", true,
                "commerceAccountId", true, "commerceCurrencyId", true,
                "commerceOrderTypeId", true, "commerceShippingMethodId", true,
                "deliveryCommerceTermEntryId", true, "paymentCommerceTermEntryId",
                true, "shippingAddressId", true, "advanceStatus", true,
                "commercePaymentMethodKey", true, "couponCode", true,
                "deliveryCommerceTermEntryName", true, "lastPriceUpdateDate", true,
                "manuallyAdjusted", true, "orderDate", true, "orderStatus", true,
                "paymentCommerceTermEntryName", true, "paymentStatus", true,
                "printedNote", true, "purchaseOrderNumber", true,
                "requestedDeliveryDate", true, "shippingAmount", true,
                "shippingDiscountAmount", true, "shippingDiscountPercentageLevel1",
                true, "shippingDiscountPercentageLevel2", true,
                "shippingDiscountPercentageLevel3", true,
                "shippingDiscountPercentageLevel4", true,
                "shippingDiscountPercentageLevel1WithTaxAmount", true,
                "shippingDiscountPercentageLevel2WithTaxAmount", true,
                "shippingDiscountPercentageLevel3WithTaxAmount", true,
                "shippingDiscountPercentageLevel4WithTaxAmount", true,
                "shippingDiscountWithTaxAmount", true, "shippingOptionName", true,
                "shippingWithTaxAmount", true, "subtotal", true,
                "subtotalDiscountAmount", true, "subtotalDiscountPercentageLevel1",
                true, "subtotalDiscountPercentageLevel2", true,
                "subtotalDiscountPercentageLevel3", true,
                "subtotalDiscountPercentageLevel4", true,
                "subtotalDiscountPercentageLevel1WithTaxAmount", true,
                "subtotalDiscountPercentageLevel2WithTaxAmount", true,
                "subtotalDiscountPercentageLevel3WithTaxAmount", true,
                "subtotalDiscountPercentageLevel4WithTaxAmount", true,
                "subtotalDiscountWithTaxAmount", true, "subtotalWithTaxAmount",
                true, "taxAmount", true, "total", true, "totalDiscountAmount", true,
                "totalDiscountPercentageLevel1", true,
                "totalDiscountPercentageLevel2", true,
                "totalDiscountPercentageLevel3", true,
                "totalDiscountPercentageLevel4", true,
                "totalDiscountPercentageLevel1WithTaxAmount", true,
                "totalDiscountPercentageLevel2WithTaxAmount", true,
                "totalDiscountPercentageLevel3WithTaxAmount", true,
                "totalDiscountPercentageLevel4WithTaxAmount", true,
                "totalDiscountWithTaxAmount", true, "totalWithTaxAmount", true,
                "status", true, "statusByUserId", true, "statusByUserName", true,
                "statusDate", true);
    }

    private void updateWorkflowStatus(final int status, final Map<String, Serializable> workflowContext) throws WorkflowException {
        try {
            if (status > -1) {
                if (_log.isDebugEnabled()) {
                    final String workflowLabelStatus = WorkflowConstants.getStatusLabel(status);
                    _log.debug("Setting workflow status to {} [{}]", workflowLabelStatus, status);
                }
                _workflowStatusManager.updateStatus(status, workflowContext);
            }
        } catch (final WorkflowException e) {
            throw new WorkflowException("Unable to update workflow status", e);
        }
    }
}
