package com.liferay.workflow.account.entry.finder.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseActionExecutorConfigurationWrapper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = AccountEntryFinderConfiguration.PID,
        immediate = true, service = AccountEntryFinderConfigurationWrapper.class
)
public class AccountEntryFinderConfigurationWrapper extends BaseActionExecutorConfigurationWrapper<AccountEntryFinderConfiguration> {

   public String getEntityLookupNameValueWorkflowContextKey() {
       return getConfiguration().entityLookupNameValueWorkflowContextKey();
   }

   public String getEntityLookupTypeValueWorkflowContextKey() {
       return getConfiguration().entityLookupTypeValueWorkflowContextKey();
   }
    public String getEntityIdentifierWorkflowContextKey() {
        return getConfiguration().entityIdentifierWorkflowContextKey();
    }

    public String getEntityAdministrationUserIdentifierWorkflowContextKey() {
        return getConfiguration().entityAdministrationUserIdentifier();
    }

    @Activate
    @Modified
    protected void activate(final Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final AccountEntryFinderConfiguration configuration = ConfigurableUtil.createConfigurable(
                AccountEntryFinderConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
