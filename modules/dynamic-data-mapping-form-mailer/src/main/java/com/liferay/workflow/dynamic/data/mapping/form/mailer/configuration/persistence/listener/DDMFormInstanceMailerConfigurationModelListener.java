package com.liferay.workflow.dynamic.data.mapping.form.mailer.configuration.persistence.listener;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListenerException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.workflow.dynamic.data.mapping.form.mailer.configuration.DDMFormInstanceMailerConfiguration;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
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
        property = "model.class.name=" + DDMFormInstanceMailerConfiguration.PID,
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
            final String formInstanceId = GetterUtil.getString(properties.get(WorkflowExtensionsConstants.CONFIG_FORM_INSTANCE_ID));

            _validateNameExists(formInstanceId);

            _validateConfigurationName(pid, formInstanceId);

            _validateUniqueConfiguration(pid, formInstanceId);
        } catch (Exception exception) {
            throw new ConfigurationModelListenerException(
                    exception, DDMFormInstanceMailerConfiguration.class, getClass(),
                    properties);
        } finally {
            _log.trace("Finish {}}.onBeforeSave", getClass().getSimpleName());
        }
    }

    private ResourceBundle _getResourceBundle() {
        return ResourceBundleUtil.getBundle(
                "content.Language", LocaleUtil.getMostRelevantLocale(), getClass());
    }

    private void _validateConfigurationName(String pid, String formInstanceId)
            throws Exception {

        Configuration configuration = _configurationAdmin.getConfiguration(
                pid, StringPool.QUESTION);

        if (configuration == null) {
            return;
        }

        Dictionary<String, Object> properties = configuration.getProperties();

        if ((properties == null) ||
                Objects.equals(properties.get(WorkflowExtensionsConstants.CONFIG_FORM_INSTANCE_ID), formInstanceId)) {

            return;
        }

        String message = ResourceBundleUtil.getString(
                _getResourceBundle(), "the-form-instance-identifier-cannot-be-changed");

        throw new Exception(message);
    }

    private void _validateNameExists(String identifier) throws Exception {
        final long formInstanceId = GetterUtil.getLong(identifier);
        if (formInstanceId > 0) {
            return;
        }

        String message = ResourceBundleUtil.getString(
                _getResourceBundle(),
                "a-form-extractor-config-must-have-a-valid-form-identifier");

        throw new Exception(message);
    }

    private void _validateUniqueConfiguration(String pid, String formInstanceId)
            throws Exception {

        String filterString = String.format(
                "(&(service.factoryPid=%s)(%s=%s))",
                DDMFormInstanceMailerConfiguration.class.getName(),
                WorkflowExtensionsConstants.CONFIG_FORM_INSTANCE_ID, formInstanceId);

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
                "there-is-already-a-upload-processor-config-with-the-form-identifier-x", formInstanceId);

        throw new Exception(message);
    }
}
