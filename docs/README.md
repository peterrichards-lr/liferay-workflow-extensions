# Liferay Workflow Extensions

A collection of Liferay Workflow Action Executor examples.

The base package for these modules is com.liferay.workflow and this can be used when configuring loggers, if required.

[Use case example](example/README.md)

## Workflow Extensions Common

This module provides common functionality required by the majority the other modules.

Please look at the description of each module below to understand if this is a dependency of not.

## Workflow Context Inspector

A very simply but useful module which outputs the content of the Liferay workflowContext to the logs.

## Dynamic Data Mapping Form Extractor

This module will extract the configured form fields (by field reference) from the Liferay form

It also supports custom fields introduced by the user-data modules, as a separate configurable JSON map.

These fields can then be used in notifications, email templates and other workflow steps.

The module can be configured on a per form instance basis.

![DDM Form Extractor](images/ddm-form-extractor.png)

### Dependencies

This module is dependent on the Workflow Extensions Common module.

## Dynamic Data Mapping Upload Processor

This module moves the upload documents into a configurable user specific folder.

The folder to which the uploads are moved can be named either using workflow context values or user attributes, such as
their screen name.

The module can be configured on a per form instance basis.

![DDM Upload Processor](images/ddm-form-upload-processor.png)

### Dependencies

This module is dependent on the Workflow Extensions Common and Dynamic Data Mapping Form Extractor modules.