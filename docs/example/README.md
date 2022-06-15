## Use Case Example

![Workflow Example](images/workflow-screenshot.png)

[Download Workflow](onboarding.xml "download")

### Form Definition

There are two custom fields which are key to this example, Company Name and Annual Turnover. The former is used when
creating the folder for the document uploads and the latter is used by a conditional node (Groovy script) to determine
which of the Sales team will work on the prospect.

![Company Name field](images/company-name-form-field.png)
![Annual Turnover field](images/annual-turnover-form-field.png)

### Workflow Definition

This is the Groovy script which uses the Annual Turnover. The value of this variable was extracted from the form.

![Annual Turnover condition](images/annual-turnover-condition.png)]

The notification template uses all the fields extracted from the form to construct the notification and emails. As the
variables have been added to the workflowContext then they can be used like any other field available to the Freemarker
template.

![Notification template](images/notification-template.png)

### Checking the Process Uploads outcome

The Check Outcome condition node uses the DDM Form Action Outcome Evaluator to determine what action to take depending
on whether the uploads were processed successfully, i.e. approve the application or report the issue to an administrator.

### Prospect above 100,000

The following shows the example of the use case where the annual turnover is above 1000,000

![Form submission](images/hermes-form-submission.png)

![Notification](images/hermes-notification.png)

![Email](images/hermes-email.png)

The following shows how the Company Name was used to create the folders and the fact the DDM Form Upload Processor has
moved the appropriate document into the relevant folder.

![Uploads folder](images/uploads-folder.png)

![Upload](images/hermes-upload.png)

### Prospect below 100,000

The following shows the example of the use case where the annual turnover is below 1000,000

![Form submission](images/snacksly-form-submission.png)

![Notification](images/snacksly-notification.png)

![Email](images/snacksly-email.png)