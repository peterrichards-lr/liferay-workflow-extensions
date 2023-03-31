package com.liferay.workflow.context.inspector.action.executor;

import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class
)
public class WorkflowContextInspector implements ActionExecutor {
    private static final Logger _log = LoggerFactory.getLogger(WorkflowContextInspector.class);

    @Override
    public void execute(final KaleoAction kaleoAction, final ExecutionContext executionContext) {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        _log.debug(mapAsString(workflowContext));
    }

    @Override
    public String[] getActionExecutorKeys() {
        return new String[] { "java" };
    }

    private String mapAsString(final Map<String, Serializable> map) {
        return map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
