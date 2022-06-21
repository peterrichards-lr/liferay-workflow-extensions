package com.liferay.workflow.account.entry.creator.settings;

import com.liferay.workflow.account.entry.creator.configuration.AccountEntryCreatorConfiguration;
import com.liferay.workflow.account.entry.creator.configuration.AccountEntryCreatorConfigurationWrapper;
import com.liferay.workflow.extensions.common.settings.BaseSettingsHelper;
import org.osgi.service.component.annotations.*;

@Component(immediate = true, service = AccountEntryCreatorSettingsHelper.class)
public class AccountEntryCreatorSettingsHelperImpl extends BaseSettingsHelper<AccountEntryCreatorConfiguration, AccountEntryCreatorConfigurationWrapper> implements AccountEntryCreatorSettingsHelper {

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY
    )
    protected void addAccountEntryCreatorConfigurationWrapper(
            AccountEntryCreatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Adding a custom field updater configuration\n[{}]", configurationWrapper.toString());
        super.addConfigurationWrapper(configurationWrapper);
    }

    protected void removeAccountEntryCreatorConfigurationWrapper(
            AccountEntryCreatorConfigurationWrapper
                    configurationWrapper) {
        _log.debug("Removing a custom field updater configuration\n[{}]", configurationWrapper.toString());
        super.removeConfigurationWrapper(configurationWrapper);
    }
}
