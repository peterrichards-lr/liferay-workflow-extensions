package com.liferay.workflow.dynamic.data.mapping.form.mailer.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.workflow.dynamic.data.mapping.form.mailer.constants.DDMFormInstanceMailerConstants;
import com.liferay.workflow.extensions.common.configuration.BaseFormActionExecutorConfiguration;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;

@ExtendedObjectClassDefinition(
        category = "workflow", scope = ExtendedObjectClassDefinition.Scope.GROUP,
        factoryInstanceLabelAttribute = WorkflowExtensionsConstants.CONFIG_FORM_INSTANCE_ID
)
@Meta.OCD(
        factory = true,
        id = DDMFormInstanceMailerConfiguration.PID,
        localization = "content/Language", name = "config-ddm-form-mailer-name",
        description = "config-ddm-form-mailer-description"
)
public interface DDMFormInstanceMailerConfiguration extends BaseFormActionExecutorConfiguration {
    String PID = "com.liferay.workflow.dynamic.data.mapping.form.mailer.configuration.DDMFormInstanceMailerConfiguration";

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_FORM_INSTANCE_ID_DEFAULT,
            description = "config-ddm-form-instance-identifier-description",
            id = WorkflowExtensionsConstants.CONFIG_FORM_INSTANCE_ID,
            name = "config-ddm-form-instance-identifier-name",
            required = false
    )
    long formInstanceId();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_ENABLE_DEFAULT,
            description = "config-enable-description",
            name = "config-enable-name",
            required = false
    )
    boolean enable();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_UPDATE_WORKFLOW_STATUS_ON_SUCCESS_DEFAULT,
            description = "config-update-workflow-status-on-success-description",
            name = "config-update-workflow-status-on-success-name",
            required = false
    )
    boolean updateWorkflowStatusOnSuccess();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_SUCCESS_WORKFLOW_STATUS_DEFAULT,
            description = "config-success-workflow-status-description",
            name = "config-success-workflow-status-name",
            required = false
    )
    String successWorkflowStatus();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_UPDATE_WORKFLOW_STATUS_ON_EXCEPTION_DEFAULT,
            description = "config-update-workflow-status-on-exception-description",
            name = "config-update-workflow-status-on-exception-name",
            required = false
    )
    boolean updateWorkflowStatusOnException();

    @Meta.AD(
            deflt = WorkflowExtensionsConstants.CONFIG_EXCEPTION_WORKFLOW_STATUS_DEFAULT,
            description = "config-exception-workflow-status-description",
            name = "config-exception-workflow-status-name",
            required = false
    )
    String exceptionWorkflowStatus();

    @Meta.AD(
            deflt = DDMFormInstanceMailerConstants.CONFIG_USE_WORKFLOW_CONTEXT_KEY_FOR_FROM_EMAIL_ADDRESS_DEFAULT,
            description = "config-use-workflow-context-key-for-from-email-address-description",
            name = "config-use-workflow-context-key-for-from-email-address-name",
            required = false
    )
    boolean useWorkflowContextKeyForFromEmailAddress();

    @Meta.AD(
            deflt = DDMFormInstanceMailerConstants.CONFIG_FROM_EMAIL_ADDRESS_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-from-email-address-workflow-context-key-description",
            name = "config-from-email-address-workflow-context-key-name",
            required = false
    )
    String fromEmailAddressWorkflowContextKey();

    @Meta.AD(
            deflt = DDMFormInstanceMailerConstants.CONFIG_FROM_EMAIL_ADDRESS_DEFAULT,
            description = "config-from-email-address-description",
            name = "config-from-email-address-name",
            required = false
    )
    String fromEmailAddress();

    @Meta.AD(
            deflt = DDMFormInstanceMailerConstants.CONFIG_TO_EMAIL_ADDRESS_WORKFLOW_CONTEXT_KEY_DEFAULT,
            description = "config-to-email-address-workflow-context-key-description",
            name = "config-to-email-address-workflow-context-key-name",
            required = false
    )
    String toEmailAddressWorkflowContextKey();

    @Meta.AD(
            deflt = DDMFormInstanceMailerConstants.CONFIG_EMAIL_SUBJECT_TEMPLATE_DEFAULT,
            description = "config-email-subject-template-description",
            name = "config-email-subject-template-name",
            required = false
    )
    String emailSubjectTemplate();

    @Meta.AD(
            deflt = DDMFormInstanceMailerConstants.CONFIG_EMAIL_BODY_TEMPLATE_DEFAULT,
            description = "config-email-body-template-description",
            name = "config-email-body-template-name",
            required = false
    )
    String emailBodyTemplate();
}
