package com.liferay.workflow.dynamic.data.mapping.upload.processor.action.executor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liferay.document.library.kernel.exception.NoSuchFolderException;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowStatusManager;
import com.liferay.portal.workflow.kaleo.model.KaleoAction;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutor;
import com.liferay.portal.workflow.kaleo.runtime.action.executor.ActionExecutorException;
import com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration.DDMFormUploadProcessorConfiguration;
import com.liferay.workflow.dynamic.data.mapping.upload.processor.configuration.DDMFormUploadProcessorConfigurationWrapper;
import com.liferay.workflow.dynamic.data.mapping.upload.processor.settings.DDMFormUploadProcessorSettingsHelper;
import com.liferay.workflow.extensions.common.action.executor.BaseDDFormActionExecutor;
import com.liferay.workflow.extensions.common.constants.WorkflowExtensionsConstants;
import com.liferay.workflow.extensions.common.context.WorkflowActionExecutionContext;
import com.liferay.workflow.extensions.common.context.service.WorkflowActionExecutionContextService;
import org.jsoup.helper.StringUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Component(
        property = "com.liferay.portal.workflow.kaleo.runtime.action.executor.language=java",
        service = ActionExecutor.class,
        configurationPid = DDMFormUploadProcessorConfiguration.PID
)
public class DDMFormUploadProcessor extends BaseDDFormActionExecutor<DDMFormUploadProcessorConfiguration, DDMFormUploadProcessorConfigurationWrapper, DDMFormUploadProcessorSettingsHelper> implements ActionExecutor {
    @Reference
    private DDMFormUploadProcessorSettingsHelper _ddmFormUploadProcessorSettingsHelper;
    @Reference
    private DLAppLocalService _dlAppLocalService;
    @Reference
    private UserLocalService _uUserLocalService;
    @Reference
    private WorkflowStatusManager _workflowStatusManager;
    @Reference
    private WorkflowActionExecutionContextService _workflowActionExecutionContextService;

    @Override
    protected DDMFormUploadProcessorSettingsHelper getSettingsHelper() {
        return _ddmFormUploadProcessorSettingsHelper;
    }

    @Override
    protected WorkflowActionExecutionContextService getWorkflowActionExecutionContextService() {
        return _workflowActionExecutionContextService;
    }

    @Override
    public void execute(KaleoAction kaleoAction, ExecutionContext executionContext, WorkflowActionExecutionContext workflowExecutionContext, DDMFormUploadProcessorConfigurationWrapper configuration, long formInstanceRecordVersionId) throws ActionExecutorException {
        final Map<String, Serializable> workflowContext = executionContext.getWorkflowContext();
        try {
            final ServiceContext serviceContext = executionContext.getServiceContext();
            final boolean success = processUploads(configuration, workflowContext, serviceContext);
            if (configuration.isWorkflowStatusUpdatedOnSuccess() && success
            ) {
                updateWorkflowStatus(configuration.getSuccessWorkflowStatus(), workflowContext);
            }
        } catch (PortalException | RuntimeException e) {
            if (configuration == null) {
                throw new ActionExecutorException("Unable to determine if workflow status is updated on exception. Configuration is null");
            } else if (configuration.isWorkflowStatusUpdatedOnException()) {
                _log.error("Unexpected exception. See inner exception for details", e);
                try {
                    updateWorkflowStatus(configuration.getExceptionWorkflowStatus(), workflowContext);
                } catch (WorkflowException ex) {
                    throw new ActionExecutorException("See inner exception", ex);
                }
            } else {
                _log.error("Unexpected exception. See inner exception for details", e);
            }
        }
    }

    private boolean processUploads(final DDMFormUploadProcessorConfigurationWrapper configuration, final Map<String, Serializable> workflowContext, final ServiceContext serviceContext) throws PortalException {
        final List<String> uploadDocuments;
        try {
            if (!workflowContext.containsKey("uploadDocuments")) {
                _log.debug("uploadDocuments was not found in the workflowContext");
                return false;
            }
            Serializable s = workflowContext.get("uploadDocuments");
            if (s instanceof List) {
                uploadDocuments = (List<String>) s;
            } else {
                _log.debug("uploadDocuments was not the expected type of List<String>");
                return false;
            }
        } catch (ClassCastException e) {
            _log.debug("uploadDocuments was not the expected type of List<String>", e);
            return false;
        }

        final long groupId = GetterUtil.getLong((workflowContext.get("groupId")));
        final String folderName = determineFolderName(configuration, workflowContext);
        final long parentFolderId = configuration.getParentFolderId();
        final long userId = GetterUtil.getLong(workflowContext.get("userId"));

        Folder documentFolder = null;
        if (configuration.isFolderAlwaysCreated()) {
            documentFolder = getDocumentFolder(groupId, folderName, parentFolderId, userId, serviceContext);
        }

        if (uploadDocuments.size() > 0) {
            if (!configuration.isFolderAlwaysCreated()) {
                documentFolder = getDocumentFolder(groupId, folderName, parentFolderId, userId, serviceContext);
            }

            if (documentFolder == null) {
                _log.warn("Unable to determine document folder");
                return false;
            }

            for (String document : uploadDocuments) {
                final Map<String, String> documentMap = getDocumentMap(document);

                if (documentMap == null) {
                    continue;
                } else if (documentMap.isEmpty()) {
                    _log.info("The documentMap returned empty for {}", document);
                    continue;
                }

                final long fileEntryId = Long.parseLong(documentMap.get("fileEntryId"));
                _log.debug("Moving file [{}] to folder {}", fileEntryId, folderName);
                final FileEntry fileEntry = _dlAppLocalService.moveFileEntry(userId, fileEntryId, documentFolder.getFolderId(), serviceContext);
                if (fileEntry != null)
                    _log.trace(String.valueOf(fileEntry));
            }
        }
        return true;
    }

    private void updateWorkflowStatus(final int status, final Map<String, Serializable> workflowContext) throws WorkflowException {
        try {
            if (status > -1) {
                if (_log.isDebugEnabled()) {
                    final String workflowLabelStatus = WorkflowConstants.getStatusLabel(status);
                    _log.debug("Setting workflow status to {} [{}]", workflowLabelStatus, status);
                }
                _workflowStatusManager.updateStatus(status, workflowContext);
            }
        } catch (WorkflowException e) {
            throw new WorkflowException("Unable to update workflow status", e);
        }
    }

    private String determineFolderName(DDMFormUploadProcessorConfigurationWrapper configuration, Map<String, Serializable> workflowContext) throws PortalException {
        if (configuration.isWorkflowKeyUsedForFolderName()) {
            final String workflowKey = configuration.getFolderNameWorkflowContextKey();
            _log.debug("The {} will be used as the folder name", workflowKey);
            final String folderName = workflowContext.containsKey(workflowKey) ? String.valueOf(workflowContext.get(workflowKey)) : null;
            if (StringUtil.isBlank(folderName)) {
                _log.debug("The folder name has an invalid value : \"{}\"", folderName);
            } else {
                _log.debug("The folder name will be {}", folderName);
            }
            return folderName;
        } else {
            Long userId = null;
            try {
                userId = GetterUtil.getLong(workflowContext.get("userId"));
                final User user = _uUserLocalService.getUser(userId);
                final String userAttribute = configuration.getFolderNameUserAttribute();
                _log.debug("The {} will be used as the folder name", userAttribute);
                final String folderName = getUserAttribute(user, userAttribute);
                _log.debug("The folder name will be {}", folderName);
                return folderName;
            } catch (PortalException e) {
                _log.warn("Unable to find user : {}", userId);
                throw e;
            }
        }
    }

    private String getUserAttribute(final User user, final String userAttribute) {
        if (user == null) {
            return null;
        }
        if (StringUtil.isBlank(userAttribute)) {
            return user.getScreenName();
        }
        switch (userAttribute.trim().toLowerCase()) {
            case "first-name":
                return user.getFirstName();
            case "last-name":
                return user.getLastName();
            case "full-name":
                return user.getFullName();
            case "email-address":
                return user.getEmailAddress();
            case "user-id":
                return String.valueOf(user.getUserId());
            case "screen-name":
            default:
                return user.getScreenName();
        }
    }

    private Folder getDocumentFolder(final long groupId, final String folderName, final long parentFolderId, final long userId, final ServiceContext serviceContext) throws PortalException {
        try {
            if (StringUtil.isBlank(folderName)) {
                return null;
            }
            final Folder documentFolder = _dlAppLocalService.getFolder(groupId, parentFolderId, folderName);
            _log.debug("Submissions folder already exists for " + folderName);
            return documentFolder;
        } catch (NoSuchFolderException nsfe) {
            try {
                _log.debug("Creating new folder for " + folderName);
                return _dlAppLocalService.addFolder(userId, groupId, parentFolderId, folderName, StringPool.BLANK, serviceContext);
            } catch (PortalException e) {
                _log.warn("Unable to create folder : {}", folderName);
                throw e;
            }
        } catch (PortalException e) {
            _log.warn("Unable to find folder : {}", folderName);
            throw e;
        }
    }

    private Map<String, String> getDocumentMap(String documentJson) {
        try {
            return WorkflowExtensionsConstants.DEFAULT_OBJECT_MAPPER.readValue(documentJson, WorkflowExtensionsConstants.CONFIG_MAP_TYPE);
        } catch (JsonProcessingException e) {
            _log.debug("Unable to parse document definition (JSON) to a Map<String, String>", e);
            return null;
        }
    }
}
