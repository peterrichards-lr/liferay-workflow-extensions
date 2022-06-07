package com.liferay.workflow.extensions.common.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.workflow.extensions.common.configuration.constants.WorkflowExtensionsConstants;

public interface BaseConfiguration {
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
}
