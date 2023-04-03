package com.liferay.workflow.organisation.creator.action.executor;

import com.liferay.portal.kernel.exception.DuplicateOrganizationException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ListTypeConstants;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.OrganizationConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.extensions.common.action.executor.BaseWorkflowEntityCreatorActionExecutor;
import com.liferay.workflow.extensions.common.configuration.model.MethodParameterConfiguration;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import com.liferay.workflow.extensions.common.util.StringUtil;
import com.liferay.workflow.extensions.common.util.WorkflowExtensionsUtil;
import com.liferay.workflow.organisation.creator.configuration.OrganisationCreatorConfiguration;
import com.liferay.workflow.organisation.creator.configuration.OrganisationCreatorConfigurationWrapper;
import com.liferay.workflow.organisation.creator.constants.OrganisationCreatorConstants;
import com.liferay.workflow.organisation.creator.settings.OrganisationCreatorSettingsHelper;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author peterrichards
 */
@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = OrganisationCreatorConfiguration.PID
)
public final class OrganisationCreator extends BaseWorkflowEntityCreatorActionExecutor<OrganisationCreatorConfiguration, OrganisationCreatorConfigurationWrapper, OrganisationCreatorSettingsHelper> implements ActionExecutor {
    @Reference
    private UserLocalService _userLocalService;
    @Reference
    private OrganizationLocalService _organizationLocalService;
    @Reference
    private OrganisationCreatorSettingsHelper _organisationCreatorSettingsHelper;
    @Reference
    private WorkflowStatusManager _workflowStatusManager;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;

    @Override
    protected UserLocalService getUserLocalService() {
        return _userLocalService;
    }

    @Override
    protected OrganisationCreatorSettingsHelper getSettingsHelper() {
        return _organisationCreatorSettingsHelper;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }

    @Override
    protected WorkflowStatusManager getWorkflowStatusManager() {
        return _workflowStatusManager;
    }

    @Override
    protected void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext, final WorkflowActionExecutionContext workflowExecutionContext, final OrganisationCreatorConfigurationWrapper configuration, final User actionUser) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final ServiceContext serviceContext = executionContext.getServiceContext();
            final boolean success = createOrganisation(actionUser, workflowContext, serviceContext, configuration);
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


    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Map<String, MethodParameterConfiguration> getEntityCreationAttributeMap() {
        return new HashMap<>() {{
            put(OrganisationCreatorConstants.METHOD_PARAM_PARENT_ID, new MethodParameterConfiguration(OrganisationCreatorConstants.METHOD_PARAM_PARENT_ID, Long.class, false,
                    (long) OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID));
            put(OrganisationCreatorConstants.METHOD_PARAM_NAME, new MethodParameterConfiguration(OrganisationCreatorConstants.METHOD_PARAM_NAME, String.class, true, null));
            put(OrganisationCreatorConstants.METHOD_PARAM_TYPE, new MethodParameterConfiguration(OrganisationCreatorConstants.METHOD_PARAM_TYPE, String.class, false, OrganizationConstants.TYPE_ORGANIZATION));
            put(OrganisationCreatorConstants.METHOD_PARAM_REGION_ID, new MethodParameterConfiguration(OrganisationCreatorConstants.METHOD_PARAM_REGION_ID, Long.class, false, -1L));
            put(OrganisationCreatorConstants.METHOD_PARAM_COUNTRY_ID, new MethodParameterConfiguration(OrganisationCreatorConstants.METHOD_PARAM_COUNTRY_ID, Long.class, false, -1L));
            put(OrganisationCreatorConstants.METHOD_PARAM_STATUS_ID, new MethodParameterConfiguration(OrganisationCreatorConstants.METHOD_PARAM_STATUS_ID, Long.class, false, (long) ListTypeConstants.ORGANIZATION_STATUS_DEFAULT));
            put(OrganisationCreatorConstants.METHOD_PARAM_COMMENTS, new MethodParameterConfiguration(OrganisationCreatorConstants.METHOD_PARAM_COMMENTS, String.class, false, null));
            put(OrganisationCreatorConstants.METHOD_PARAM_SITE, new MethodParameterConfiguration(OrganisationCreatorConstants.METHOD_PARAM_SITE, Boolean.class, false, false));
        }};
    }

    private boolean createOrganisation(final User creator, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext, final OrganisationCreatorConfigurationWrapper configuration) throws PortalException {
        final Map<String, Object> methodParameters = buildMethodParametersMap(workflowContext, serviceContext, configuration);

        final long parentId = (long) methodParameters.get(OrganisationCreatorConstants.METHOD_PARAM_PARENT_ID);
        final String name = (String) methodParameters.get(OrganisationCreatorConstants.METHOD_PARAM_NAME);
        final String type = (String) methodParameters.get(OrganisationCreatorConstants.METHOD_PARAM_TYPE);
        final long regionId = (long) methodParameters.get(OrganisationCreatorConstants.METHOD_PARAM_REGION_ID);
        final long countryId = (long) methodParameters.get(OrganisationCreatorConstants.METHOD_PARAM_COUNTRY_ID);
        final long statusId = (long) methodParameters.get(OrganisationCreatorConstants.METHOD_PARAM_STATUS_ID);
        final String comments = (String) methodParameters.get(OrganisationCreatorConstants.METHOD_PARAM_COMMENTS);
        final boolean site = (boolean) methodParameters.get(OrganisationCreatorConstants.METHOD_PARAM_SITE);

        try {
            Organization organization;
            try {
                organization = _organizationLocalService.addOrganization(creator.getUserId(), parentId, name, type, regionId, countryId, statusId, comments, site, serviceContext);
                _log.debug("New organisation created");
            } catch (final DuplicateOrganizationException e) {
                if (!configuration.shouldRecoverFromDuplicateException()) {
                    throw e;
                }
                organization = _organizationLocalService.fetchOrganization(serviceContext.getCompanyId(), name);
                _log.debug("Existing organisation returned");
            }
            WorkflowExtensionsUtil.runIndexer(organization, serviceContext);
            if (organization != null) {
                final String identifierWorkflowKey = configuration.getCreatedEntityIdentifierWorkflowContextKey();
                final long organisationId = organization.getOrganizationId();
                if (!StringUtil.isBlank(identifierWorkflowKey)) {
                    _log.debug("Returning organisation identifier {} in {}", organisationId, identifierWorkflowKey);
                    workflowContext.put(identifierWorkflowKey, organisationId);
                }
                return true;
            }
            _log.warn("The addOrganization returned null");
            return false;
        } catch (final PortalException e) {
            _log.error("Unable to create organization", e);
            return false;
        }
    }
}