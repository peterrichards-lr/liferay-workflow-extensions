package com.liferay.workflow.user.custom.field.updater.configuration.listener;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListenerException;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.user.custom.field.updater.configuration.UserCustomFieldUpdaterConfiguration;
import org.jsoup.helper.StringUtil;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.Objects;
import java.util.ResourceBundle;

@Component(
        immediate = true,
        property = "model.class.name=" + UserCustomFieldUpdaterConfiguration.PID,
        service = ConfigurationModelListener.class
)
public class DDMFormInstanceMailerConfigurationModelListener implements ConfigurationModelListener {
    private static final Logger _log = LoggerFactory.getLogger(DDMFormInstanceMailerConfigurationModelListener.class);
    @Reference
    private ConfigurationAdmin _configurationAdmin;

    @Override
    public void onBeforeSave(String pid, Dictionary<String, Object> properties)
            throws ConfigurationModelListenerException {
        _log.trace("Start {}.onBeforeSave", getClass().getSimpleName());
        try {
            final String identifier = (String) properties.get(WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID);

            _validateNameExists(identifier);

            _validateConfigurationName(pid, identifier);

            _validateUniqueConfiguration(pid, identifier);
        } catch (Exception exception) {
            throw new ConfigurationModelListenerException(
                    exception, UserCustomFieldUpdaterConfiguration.class, getClass(),
                    properties);
        } finally {
            _log.trace("Finish {}}.onBeforeSave", getClass().getSimpleName());
        }
    }

    private ResourceBundle _getResourceBundle() {
        return ResourceBundleUtil.getBundle(
                "content.Language", LocaleUtil.getMostRelevantLocale(), getClass());
    }

    private void _validateConfigurationName(String pid, String identifier)
            throws Exception {

        Configuration configuration = _configurationAdmin.getConfiguration(
                pid, StringPool.QUESTION);

        if (configuration == null) {
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

    private void _validateNameExists(String identifier) throws Exception {
        if (StringUtil.isBlank(identifier) || WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID_DEFAULT.equals(identifier)) {
            String message = ResourceBundleUtil.getString(
                    _getResourceBundle(),
                    "a-user-custom-field-updater-config-must-have-a-valid-workflow-node-identifier");

            throw new Exception(message);
        }
    }

    private void _validateUniqueConfiguration(String pid, String identifier)
            throws Exception {

        String filterString = String.format(
                "(&(service.factoryPid=%s)(%s=%s))",
                UserCustomFieldUpdaterConfiguration.class.getName(),
                WorkflowExtensionsConstants.CONFIG_WORKFLOW_NODE_ID, identifier);

        Configuration[] configurations = _configurationAdmin.listConfigurations(
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
                "there-is-already-a-upload-processor-config-with-the-workflow-node-identifier-x", identifier);

        throw new Exception(message);
    }
}
