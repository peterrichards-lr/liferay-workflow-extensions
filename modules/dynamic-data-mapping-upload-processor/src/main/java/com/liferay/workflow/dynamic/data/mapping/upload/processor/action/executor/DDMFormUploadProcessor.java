package com.liferay.workflow.dynamic.data.mapping.upload.processor.action.executor;

import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DDMFormUploadProcessor implements ActionExecutor {
    private static final Logger _log = LoggerFactory.getLogger(DDMFormUploadProcessor.class);
    @Reference
    private WorkflowStatusManager _workflowStatusManager;

    @Override
    public void execute(KaleoAction kaleoAction, ExecutionContext executionContext) throws ActionExecutorException {

    }
}
