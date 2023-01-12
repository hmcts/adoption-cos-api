const config = require('../config');
const { I } = inject();
const manageDocumentsDetails = require('../fixtures/manageDocumentsDetails.js');

module.exports = {
  fields: {
      //Common
      caseId: 'h3:nth-child(1)',
      ChildName: 'h3:nth-child(2)',
      continueButton: 'button[type="submit"]',
      previousButton: 'button[type="button"]',
      cancelLink: '//a[text()="Cancel"]',
      actionsDropdown: '#next-step',

      //Screen 1 Manage documents
      documentPageHeader: 'h2:nth-child(1)',
      documentLabel: '//span[text()="Document"]',
      documentHint: '//span[text()="The selected file must be smaller than 1GB"]',
      documentFileUploadField: '#adoptionUploadDocument_documentLink',
      documentErrorMessagePage: '//a[text()="Select or fill the required Document field"]',
      documentDescLabel: '//span[text()="Document description"]',
      documentDescHint: '//span[text()="Describe what the document is, such as death certificate for the birth father."]',
      documentDescField: '#adoptionUploadDocument_documentComment',
      documentDescErrorMessagePage: '//a[text()="Document description is required"]',
      documentDescErrorMessageField: '//span[text()="Document description is required"]',
      documentCategoryLabel: '//span[text()="Document"]',
      documentCategoryHint: '//span[text()="If you want to upload more than one, you need to go through the steps again from the documents tab."]',
      documentCategoryListField: '//input[@type="radio"]',
      documentCategoryErrorMessagePage: '//a[text()="What document are you uploading? is required"]',
      documentCategoryErrorMessageField: '//span[text()="What document are you uploading? is required"]',

      //Screen 2 Who submitted the document?.
      submittedPartyPageHeader: '//h1[text()="Manage documents"]',
      submittedPartyLabel: '//h2[text()="Who submitted the document?"]',
      submittedPartyHint: '//span[text()="Select an option below"]',
      submittedPartyListField: '//input[@type="radio"]',
      submittedPartyErrorMessagePage: '//a[text()="Field is required"]',
      submittedPartyErrorMessageField: '//span[text()="Field is required"]',
      otherPartyNameLabel: '//span[text()="Name"]',
      otherPartyNameHint: '//span[text()="Add the name of the person who submitted the document."]',
      otherPartyRoleLabel: '//span[text()="Role"]',
      otherPartyRoleHint: '//span[text()="What is their role? For example, first applicant or child\'s social worker."]',
      otherPartyNameErrorMessagePage: '//a[text()="Name is required"]',
      otherPartyNameErrorMessageField: '//span[text()="Name is required"]',
      otherPartyRoleErrorMessagePage: '//a[text()="Role is required"]',
      otherPartyRoleErrorMessageField: '//span[text()="Role is required"]',

      //Screen 3 CYA
      cyaPageHeader: '//h2[text()="Check your answers"]',
//      cyaPageHeaderChangeLink: '//a/span[text()="Change"]',
      alertMessage: '//div[@class="alert-message"]',

  },

  async verifyCommon() {
    await I.wait(3);
    await I.retry(3).waitForSelector(this.fields.caseId);
    await I.retry(3).seeElement(this.fields.caseId);
    await I.retry(3).seeElement(this.fields.ChildName);
    await I.retry(3).seeElement(this.fields.continueButton);
    await I.retry(3).seeElement(this.fields.previousButton);
    await I.retry(3).seeElement(this.fields.cancelLink);
  },

  async verifyCancelLink() {
    await I.retry(3).click(this.fields.cancelLink);
    await I.wait(3);
    await I.retry(3).waitForSelector(this.fields.actionsDropdown);
    await I.retry(3).seeElement(this.fields.actionsDropdown);
  },

  async verifyManageDocuments(){
    await this.verifyCommon();
    await I.retry(3).seeElement(this.fields.documentPageHeader);
    await I.retry(3).seeElement(this.fields.documentLabel);
    await I.retry(3).seeElement(this.fields.documentHint);
    await I.retry(3).seeElement(this.fields.documentFileUploadField);
    await I.retry(3).seeElement(this.fields.documentDescLabel);
    await I.retry(3).seeElement(this.fields.documentDescHint);
    await I.retry(3).seeElement(this.fields.documentDescField);
    await I.retry(3).seeElement(this.fields.documentCategoryLabel);
    await I.retry(3).seeElement(this.fields.documentCategoryHint);
    await I.retry(3).seeElement(this.fields.documentCategoryListField);
    await I.retry(3).click(this.fields.continueButton);
    await I.retry(3).see('Select or fill the required Document field', this.fields.documentErrorMessagePage);
    await I.retry(3).see('Document description is required', this.fields.documentDescErrorMessagePage);
    await I.retry(3).see('Document description is required', this.fields.documentDescErrorMessageField);
    await I.retry(3).see('What document are you uploading? is required', this.fields.documentCategoryErrorMessagePage);
    await I.retry(3).see('What document are you uploading? is required', this.fields.documentCategoryErrorMessageField);
  },

  async fulfillManageDocuments(){
    await this.verifyCommon();
    await I.retry(3).attachFile(this.fields.documentFileUploadField, manageDocumentsDetails.documentFileUploadPathFilename);
    await I.retry(3).fillField(this.fields.documentDescField, manageDocumentsDetails.documentDescriptionText);
    await I.retry(3).checkOption(this.fields.documentCategoryListField);
    await I.retry(3).click(this.fields.continueButton);
  },

  async verifyWhoSubmittedTheDocument(){
    await this.verifyCommon();
    await I.retry(3).seeElement(this.fields.submittedPartyPageHeader);
    await I.retry(3).seeElement(this.fields.submittedPartyLabel);
    await I.retry(3).seeElement(this.fields.submittedPartyHint);
    await I.retry(3).seeElement(this.fields.submittedPartyListField);
    await I.retry(3).click(this.fields.continueButton);
    await I.retry(3);
    await I.retry(3).see('Field is required', this.fields.submittedPartyErrorMessagePage);
    await I.retry(3).see('Field is required', this.fields.submittedPartyErrorMessageField);
//    await I.retry(3).checkOption(this.fields.submittedPartyListField, 'Other party');
//    await I.retry(3);
//    await I.retry(3).seeElement(this.fields.otherPartyNameLabel);
//    await I.retry(3).seeElement(this.fields.otherPartyNameHint);
//    await I.retry(3).seeElement(this.fields.otherPartyRoleLabel);
//    await I.retry(3).seeElement(this.fields.otherPartyRoleHint);
//    await I.retry(3).click(this.fields.continueButton);
//    await I.retry(3);
//    await I.retry(3).see('Name is required', this.fields.otherPartyNameErrorMessagePage);
//    await I.retry(3).see('Name is required', this.fields.otherPartyNameErrorMessageField);
//    await I.retry(3).see('Role is required', this.fields.otherPartyRoleErrorMessagePage);
//    await I.retry(3).see('Role is required', this.fields.otherPartyRoleErrorMessageField);
  },

  async fulfillWhoSubmittedTheDocument(){
    await this.verifyCommon();
    await I.retry(3).checkOption(this.fields.submittedPartyListField);
    await I.retry(3).click(this.fields.continueButton);
  },

  async verifyAndFulfillCYA() {
    await this.verifyCommon();
    await I.retry(3).seeElement(this.fields.cyaPageHeader);
//    await I.retry(3).seeElement(this.fields.cyaPageHeaderChangeLink);
    await I.retry(3).see('Document');
    await I.retry(3).see(manageDocumentsDetails.documentFileUploadFilename);
    await I.retry(3).see('Document description');
    await I.retry(3).see(manageDocumentsDetails.documentDescriptionText);
    await I.retry(3).see('What document are you uploading?');
    await I.retry(3).see(manageDocumentsDetails.documentCategoryListFieldOption);
    await I.retry(3).see('First applicant :');
  },

  async navigationCheckCYA() {
    await this.verifyCommon();
    await I.retry(3).click(this.fields.previousButton);
    await I.wait(3);
    await I.retry(3).click(this.fields.previousButton);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await this.verifyCommon();
  },

  async verifySuccessfulAlertMessage(){
    await this.verifyCommon();
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(5).seeElement(this.fields.alertMessage);
  },
};
