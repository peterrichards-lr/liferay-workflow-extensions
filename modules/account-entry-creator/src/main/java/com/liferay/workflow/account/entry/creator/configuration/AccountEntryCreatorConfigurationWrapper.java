package com.liferay.workflow.account.entry.creator.configuration;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.workflow.extensions.common.configuration.BaseEntityCreatorActionExecutorConfigurationWrapper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import java.util.Map;
import java.util.stream.Collectors;

@Component(
        configurationPid = AccountEntryCreatorConfiguration.PID,
        immediate = true, service = AccountEntryCreatorConfigurationWrapper.class
)
public class AccountEntryCreatorConfigurationWrapper extends BaseEntityCreatorActionExecutorConfigurationWrapper<AccountEntryCreatorConfiguration> {

    public boolean useExistingIfFound() {
        return getConfiguration().useExistingIfFound();
    }

    @Activate
    @Modified
    protected void activate(final Map<String, Object> properties) {
        _log.trace("Activating {} : {}", getClass().getSimpleName(), properties.keySet().stream().map(key -> key + "=" + properties.get(key).toString()).collect(Collectors.joining(", ", "{", "}")));
        final AccountEntryCreatorConfiguration configuration = ConfigurableUtil.createConfigurable(
                AccountEntryCreatorConfiguration.class, properties);

        super.setConfiguration(configuration);
    }
}
