# Liferay Workflow Extensions

A collection of Liferay Workflow Action Executor examples.

### Enable Logging

The base package for these modules is com.liferay.workflow and this can be used when configuring loggers, if required.

In Liferay a new logger can be created through the Server Administration option in the Control Panel. Look
on [Learn Liferay](https://learn.liferay.com/dxp/latest/en/system-administration/using-the-server-administration-panel/configuring-logging.html#configuring-logging)
for more information on how to configure the logger.

### Liferay Version Dependency

These extensions have been tested with 7.4 U30.

There is a known bug which was introduced in U21 which prevents the extensions being used for version U21 to U29
inclusively.

## Example Workflow

The following example is based on an on-boarding use case.

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

## Dynamic Data Mapping Form Action Outcome Evaluator

This module is used to check the outcome of a previous step, i.e. the workflow status. The workflow status
can then be used to follow different flows (transitions) depending on the configured statuses and transition names.

It currently only supports two transitions, i.e. success or failure and the configuration applies at a form level,
so typically this would mean all Evaluators within a single workflow will use the same configuration.

The module can be configured on a per form instance basis.

![DDM Form Action Outcome Evaluator](images/ddm-form-action-outcome-evaluator.png)

### Dependencies

This module is dependent on the Workflow Extensions Common module.

## Dynamic Data Mapping Form Options Translator

This module is used to convert or translate internal option references to their use case equivalent value.

It works with both single and multiple selection fields.

The module can be configured on a per form instance basis.

![DDM Form Options Translator](images/ddm-form-options-translator.png)

### Dependencies

This module is dependent on the Workflow Extensions Common module.

## Dynamic Data Mapping Form Mailer

This module is used to send emails to a central mailbox or anonymous user.

It supports templates for the email subject and body which can make use of values in the workflow context using ${...}
syntax.

The module can be configured on a per form instance basis.

![DDM Form Mailer](images/ddm-form-mailer.png)

### Dependencies

This module is dependent on the Workflow Extensions Common module.

## Custom Field Updater

This module is used to update one or more custom field on a user, account or organisation entity. A single instance can
only update a single entity type but multiple instances can exist within a single workflow

The custom updater needs a user context in which to perform the update. It can use the in-context user or a specific
user can be looked up by email address, screen name or user id. The user lookup value can be stored in configuration or
extracted from a workflow context key.

The entity can be looked up using different methods depending on the entity type. The lookup value can be stored in
configuration or taken from the workflow context

The module can be configured on a per workflow:node:action basis.

![Custom Field Updater](images/custom-field-updater.png)

### Dependencies

This module is dependent on the Workflow Extensions Common module.

## Account Entry Creator

This module allows a new Liferay Account Entry to be created using a mixture of workflow context values and default
values specified in the configuration.

![Account Entry Creator](images/account-entry-creator.png)

### Entry Creation Attributes

The following table indicates which Entry Creation Attributes are required, their type and their default value when not
supplied. The default value here is not the same as the default valued specified in the Entity Creation Attribute JSON.
The default value specified in the Entity Creation Attribute JSON defines the default value if the value cannot be
obtained from the workflow context, the default value specified is what is used when no Entity Creation Attribute JSON
exists for the specific attribute.

| Name          | Type     | Required | Default Value | Example                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
|---------------|----------|----------|---------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| parent-id     | Long     | false    | 0             | {"entityAttributeName":"parent-id","useWorkflowContextKey":false,"workflowContextKey":"accountEntryParentId","defaultValue":"0"}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| name          | String   | true     | N/A           | {"entityAttributeName":"name","useWorkflowContextKey":true,"workflowContextKey":"accountName","defaultValue":""}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
| description   | String   | false    | Empty String  | {"entityAttributeName":"description","useWorkflowContextKey":false,"workflowContextKey":"description","defaultValue":""}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
| domains       | String[] | false    | null          | {"entityAttributeName":"domains","useWorkflowContextKey":false,"workflowContextKey":"domains","defaultValue":"[\"domain1.com\",\"domain2.com\"]"}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
| email-address | String   | false    | null          | {"entityAttributeName":"email-address","useWorkflowContextKey":true,"workflowContextKey":"emailAddress","defaultValue":""}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| logo-base64   | String   | false    | null          | {"entityAttributeName":"logo-base64","useWorkflowContextKey":false,"workflowContextKey":"logo","defaultValue":"iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAmVBMVEUAAAAaoOgaoOgaoOgaoOgaoOgaoOgaoOgaoOgaoOgaoOgSYrsLY84LYswNbtMLY84LY84aoOgLY84LY84aoOgLY84LY84LY84LY84LY84LY84LY84aoOgaoOgamuMVfc4Ub8MTYrgWhdQRSqITQZQTQ5gRUKkSTaQLY84OV7oRVa8NWr8YkdwTXLISRpsWjNsMYcoSRZv////Q7POHAAAAHXRSTlMAQJDA8KBwIOBggLtg4RCw0FBQkNDwwHAgMIBAMA1lz0kAAAABYktHRDJA0kzIAAAAB3RJTUUH5gYWBzUr/TmCrwAAAQ5JREFUOMuVk9l6wiAQhYlJarqIW3dLkxhIxqi1ff+Xa1hGROBTz+35gWHmDCG3KBmlGWMsze/GQbtgVum95z98l1Vdl2tEisT1HxuuJdrSIKMT+2kiuFVnkBxtOoUNd1Q7BJ0BNC7Ae03M0Qd+rkpXKoHp4G89QOzwisXgQ+8BfI9VTCTQ+sCPeUNd4NUodVAEWSpABADdDDJTAI8DEAXWlwB2AWiV/xwHStMHiHyzw2G8QLhRusSMkFcFbM59O+83CHXKTJsN4X3XQB/0Vej0G79/AT+jEvjQV9hEiAqDbXJtqsA6uwP6x9X4PCEw0G7qV+avzf54mmXuatHVIDpO7ep9RdY3lwta5Am5Wv9qHmXfrKv4KQAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAyMi0wNi0yMlQwNzo1Mzo0MyswMDowML49FLsAAAAldEVYdGRhdGU6bW9kaWZ5ADIwMjItMDYtMjJUMDc6NTM6NDMrMDA6MDDPYKwHAAAAAElFTkSuQmCC"} |
| tax-id-number | String   | false    | null          | {"entityAttributeName":"tax-id-number","useWorkflowContextKey":false,"workflowContextKey":"taxNumber","defaultValue":"99L99999"}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| type          | String   | false    | guest         | {"entityAttributeName":"type","useWorkflowContextKey":false,"workflowContextKey":"organisationType","defaultValue":"guest"}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| status        | Integer  | false    | APPROVED      | {"entityAttributeName":"status","useWorkflowContextKey":false,"workflowContextKey":"status","defaultValue":"0"}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        

### Dependencies

This module is dependent on the Workflow Extensions Common module.

## Organisation Creator

This module allows a new Liferay Organisation to be created using a mixture of workflow context values and default
values specified in the configuration.

![Organisation Creator](images/organisation-creator.png)

### Entry Creation Attributes

The following table indicates which Entry Creation Attributes are required, their type and their default value when not
supplied. The default value here is not the same as the default valued specified in the Entity Creation Attribute JSON.
The default value specified in the Entity Creation Attribute JSON defines the default value if the value cannot be
obtained from the workflow context, the default value specified is what is used when no Entity Creation Attribute JSON
exists for the specific attribute.

| Name       | Type    | Required | Default Value               | Example                                                                                                                            |
|------------|---------|----------|-----------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| parent-id  | Long    | false    | 0                           | {"entityAttributeName":"parent-id","useWorkflowContextKey":false,"workflowContextKey":"accountEntryParentId","defaultValue":"0"}   |
| name       | String  | true     | N/A                         | {"entityAttributeName":"name","useWorkflowContextKey":true,"workflowContextKey":"organisationName","defaultValue":""}              |             
| type       | String  | false    | organization                | {"entityAttributeName":"type","useWorkflowContextKey":false,"workflowContextKey":"organisationType","defaultValue":"organization"} |
| region-id  | Long    | false    | -1                          | {"entityAttributeName":"region-id","useWorkflowContextKey":false,"workflowContextKey":"regionId","defaultValue":"21519"}           |
| country-id | Long    | false    | -1                          | {"entityAttributeName":"country-id","useWorkflowContextKey":false,"workflowContextKey":"countryId","defaultValue":"21316"}         |
| status-id  | Long    | false    | Organization Status Default | {"entityAttributeName":"status-id","useWorkflowContextKey":false,"workflowContextKey":"statusId","defaultValue":"12017"}           |
| comments   | String  | false    | null                        | {"entityAttributeName":"comments","useWorkflowContextKey":false,"workflowContextKey":"description","defaultValue":""}              |
| site       | Boolean | false    | false                       | {"entityAttributeName":"site","useWorkflowContextKey":false,"workflowContextKey":"organisationSite","defaultValue":"true"}         |

### Dependencies

This module is dependent on the Workflow Extensions Common module.

## User Account Creator

This module allows a new Liferay User Account to be created using a mixture of workflow context values and default
values specified in the configuration.

![User Account Creator](images/user-account-creator.png)

### Entry Creation Attributes

The following table indicates which Entry Creation Attributes are required, their type and their default value when not
supplied. The default value here is not the same as the default valued specified in the Entity Creation Attribute JSON.
The default value specified in the Entity Creation Attribute JSON defines the default value if the value cannot be
obtained from the workflow context, the default value specified is what is used when no Entity Creation Attribute JSON
exists for the specific attribute.

| Name             | Type    | Required | Default Value | Example                                                                                                                                        |
|------------------|---------|----------|---------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| auto-password    | Boolean | false    | true          | {"entityAttributeName":"auto-password","useWorkflowContextKey":false,"workflowContextKey":"autoPassword","defaultValue":"true"}                |
| auto-screen-name | Boolean | false    | true          | {"entityAttributeName":"auto-screen-name","useWorkflowContextKey":false,"workflowContextKey":"autoScreenName","defaultValue":"true"}           |   
| male             | Boolean | false    | false         | {"entityAttributeName":"male","useWorkflowContextKey":false,"workflowContextKey":"male","defaultValue":"false"}                                |
| send-mail        | Boolean | false    | false         | {"entityAttributeName":"send-mail","useWorkflowContextKey":false,"workflowContextKey":"sendMail","defaultValue":"true"}                        |
| password         | String  | false    | null          | {"entityAttributeName":"password","useWorkflowContextKey":false,"workflowContextKey":"password","defaultValue":""}                             |
| screen-name      | String  | false    | null          | {"entityAttributeName":"screen-name","useWorkflowContextKey":false,"workflowContextKey":"screenName","defaultValue":""}                        |
| email-address    | String  | true     | N/A           | {"entityAttributeName":"email-address","useWorkflowContextKey":true,"workflowContextKey":"emailAddress","defaultValue":""}                     |
| first-name       | String  | true     | N/A           | {"entityAttributeName":"first-name","useWorkflowContextKey":true,"workflowContextKey":"foreName","defaultValue":""}                            |
| middle-name      | Integer | false    | null          | {"entityAttributeName":"middle-name","useWorkflowContextKey":false,"workflowContextKey":"emailAddress","defaultValue":""}                      |
| last-name        | Integer | true     | N/A           | {"entityAttributeName":"last-name","useWorkflowContextKey":true,"workflowContextKey":"surname","defaultValue":""}                              |
| job-title        | Integer | false    | null          | {"entityAttributeName":"job-title","useWorkflowContextKey":true,"workflowContextKey":"jobTitle","defaultValue":""}                             |
| prefix-id        | Integer | false    | -1            | {"entityAttributeName":"prefix-id","useWorkflowContextKey":false,"workflowContextKey":"prefixId","defaultValue":"11015"}                       |
| suffix-id        | Integer | false    | -1            | {"entityAttributeName":"suffix-id","useWorkflowContextKey":false,"workflowContextKey":"suffixId","defaultValue":"11025"}                       |
| group-ids        | Long[]  | false    | null          | {"entityAttributeName":"group-ids","useWorkflowContextKey":false,"workflowContextKey":"groupIds","defaultValue":"[20123,52553]"}               |
| organisation-ids | Long[]  | false    | null          | {"entityAttributeName":"organisation-ids","useWorkflowContextKey":false,"workflowContextKey":"organisationIds","defaultValue":"[52430,52437]"} |
| role-ids         | Long[]  | false    | null          | {"entityAttributeName":"role-ids","useWorkflowContextKey":false,"workflowContextKey":"roleIds","defaultValue":"[20111,20109]"}                 |
| user-group-ids   | Long[]  | false    | null          | {"entityAttributeName":"user-group-ids","useWorkflowContextKey":false,"workflowContextKey":"userGroupIds","defaultValue":"[52424,52427]"}      |
| locale           | String  | false    | en-US         | {"entityAttributeName":"locale","useWorkflowContextKey":false,"workflowContextKey":"locale","defaultValue":"en-GB"}                            |
| dob              | String  | false    | 01/01/1970    | {"entityAttributeName":"dob","useWorkflowContextKey":false,"workflowContextKey":"dateOfBirth","defaultValue":"1980-01-01"}                     |

### Dependencies

This module is dependent on the Workflow Extensions Common module.

## User Group Roles Updater

This module allows user groups to be assigned to a user. The supported types are site, account entry and organisation.

In the case of an organisation configuration, if the group identifier is obtained via an organisation lookup then the
user will also be added to the organisation.

![User Group Roles Updater](images/user-group-roles-updater.png)

### Dependencies

This module is dependent on the Workflow Extensions Common module.

## User Group Creator

This module allows a user group to be created.

![User Group Roles Updater](images/user-group-creator.png)

### Entry Creation Attributes

The following table indicates which Entry Creation Attributes are required, their type and their default value when not
supplied. The default value here is not the same as the default valued specified in the Entity Creation Attribute JSON.
The default value specified in the Entity Creation Attribute JSON defines the default value if the value cannot be
obtained from the workflow context, the default value specified is what is used when no Entity Creation Attribute JSON
exists for the specific attribute.

| Name        | Type    | Required | Default Value   | Example                                                                                                                  |
|-------------|---------|----------|-----------------|--------------------------------------------------------------------------------------------------------------------------|
| name        | String  | true     | N/A             | {"entityAttributeName":"name","useWorkflowContextKey":true,"workflowContextKey":"userGroupName","defaultValue":""}       |             
| description | String  | false    | Empty String    | {"entityAttributeName":"description","useWorkflowContextKey":false,"workflowContextKey":"description","defaultValue":""} |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    

### Dependencies

This module is dependent on the Workflow Extensions Common module.

## User Group Updater

This module allows a user or group of users to be assigned to a user group.

![User Group Roles Updater](images/user-group-updater.png)

### Dependencies

This module is dependent on the Workflow Extensions Common module.