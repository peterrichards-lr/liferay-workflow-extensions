package com.liferay.workflow.account.entry.finder.settings;

import com.liferay.workflow.account.entry.finder.configuration.AccountEntryFinderConfiguration;
import com.liferay.workflow.account.entry.finder.configuration.AccountEntryFinderConfigurationWrapper;
import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = AccountEntryFinderSettingsHelper.class)
public class AccountEntryFinderSettingsHelperImpl extends BaseSettingsHelper<AccountEntryFinderConfiguration, AccountEntryFinderConfigurationWrapper> implements  AccountEntryFinderSettingsHelper {
    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addAccountEntryFinderConfigurationWrapper(
            final AccountEntryFinderConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a account entry finder configuration\n" +
                "[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    @SuppressWarnings("unused")
    protected void removeAccountEntryFinderConfigurationWrapper(
            final AccountEntryFinderConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a account entry finder configuration\n" +
                "[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
