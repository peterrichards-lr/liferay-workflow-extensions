package com.liferay.workflow.extensions.common.configuration.persistence.listener;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListenerException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.workflow.extensions.common.configuration.BaseConfiguration;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import org.jsoup.helper.StringUtil;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.Objects;
import java.util.ResourceBundle;

public abstract class BaseConfigurationModelListener<T extends BaseConfiguration> implements ConfigurationModelListener {
    protected final Logger _log = LoggerFactory.getLogger(getClass());

    protected abstract ConfigurationAdmin getConfigurationAdmin();

    protected boolean canIdentifierChange() {
        return true;
    }

    protected abstract Class<T> getConfigurationClass();

    @Override
    public void onBeforeSave(String pid, Dictionary<String, Object> properties)
            throws ConfigurationModelListenerException {
        _log.trace("Start {}.onBeforeSave", getClass().getSimpleName());
        try {
            final String identifier = GetterUtil.getString(properties.get(WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID));

            _validateIdentifierProvided(identifier);

            _validateConfigurationName(pid, identifier);

            _validateUniqueConfiguration(pid, identifier);
        } catch (Exception exception) {
            throw new ConfigurationModelListenerException(
                    exception, getConfigurationClass(), getClass(),
                    properties);
        } finally {
            _log.trace("Finish {}.onBeforeSave", getClass().getSimpleName());
        }
    }

    private ResourceBundle _getResourceBundle() {
        return ResourceBundleUtil.getBundle(
                "content.Language", LocaleUtil.getMostRelevantLocale(), getClass());
    }

    private void _validateConfigurationName(String pid, String identifier)
            throws Exception {

        Configuration configuration = getConfigurationAdmin().getConfiguration(
                pid, StringPool.QUESTION);

        if (configuration == null) {
            return;
        }

        if (canIdentifierChange()) {
            return;
        }

        Dictionary<String, Object> properties = configuration.getProperties();

        if ((properties == null) ||
                Objects.equals(properties.get(WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID), identifier)) {

            return;
        }

        String message = ResourceBundleUtil.getString(
                _getResourceBundle(), "the-workflow-node-identifier-cannot-be-changed");

        throw new Exception(message);
    }

    private void _validateIdentifierProvided(String identifier) throws Exception {
        if (StringUtil.isBlank(identifier) || WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID_ACTION_DEFAULT.equals(identifier)) {
            String message = ResourceBundleUtil.getString(
                    _getResourceBundle(),
                    "a-config-must-have-a-valid-workflow-node-identifier");

            throw new Exception(message);
        }
    }

    private void _validateUniqueConfiguration(String pid, String formInstanceId)
            throws Exception {

        String filterString = String.format(
                "(&(service.factoryPid=%s)(%s=%s))",
                getConfigurationClass().getName(),
                WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID, formInstanceId);

        Configuration[] configurations = getConfigurationAdmin().listConfigurations(
                filterString);

        if (configurations == null) {
            return;
        }

        Configuration configuration = configurations[0];

        if (pid.equals(configuration.getPid())) {
            return;
        }

        String message = ResourceBundleUtil.getString(
                _getResourceBundle(),
                "there-is-already-a-config-with-the-workflow-node-identifier-x", formInstanceId);

        throw new Exception(message);
    }
}
