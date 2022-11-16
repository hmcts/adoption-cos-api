const config = require('../config');
const { I } = inject();
const seekFurtherInfoDetails = require('../fixtures/seekFurtherInfoDetails.js');
module.exports = {
  fields: {
      //Common
      pageHeader1: 'h1:nth-child(1)',
      pageHeader2: 'h2:nth-child(1)',
      caseId: 'h3:nth-child(1)',
      ChildName: 'h3:nth-child(2)',
      continueButton: 'button[type="submit"]',
      previousButton: 'button[type="button"]',
      cancelLink: '//a[text()="Cancel"]',
      actionsDropdown: '#next-step',
      goButton: 'Go',

      //Screen 1 Who do you need to contact
      labelMessageWhoDoYouNeedToContact: '//span[contains(text(),"Who do you need to contact")]',
      whoDoYouNeedToContactErrorMessage: '#seekFurtherInformationList .error-message',
      whoDoYouNeedToContactList: '//input[@type="radio"]',

      //Screen 2 What information do you need?
      labelMessageWhatInformationDoYouNeed: '//span[contains(text(),"What information do you need?")]',
      whatInformationDoYouNeedListErrorMessage: '#furtherInformation .error-message',
      askForAdditionalDocumentListOption: '#furtherInformation-askForAdditionalDocument',
      askAQuestionListOption: '#furtherInformation-askAQuestion',
      askForAdditionalDocumentErrorMessage: '//span[contains(text(),"List the documents you need is required")]',
      askAQuestionErrorMessage: '//span[contains(text(),"List the questions you need is required")]',
      askForAdditionalDocumentText: '#askForAdditionalDocumentText',
      askAQuestionText: '#askAQuestionText',

      //Screen 3 When is the information needed by?
      labelMessageWhenIsTheInformationNeededBy: '//span[contains(text(),"When is the information needed by?")]',
      whenIsTheInformationNeededByDateErrorMessage: '#date .error-message',
      whenIsTheInformationNeededByDateText: {
        day: '#date-day',
        month: '#date-month',
        year: '#date-year',
        hour: '#date-hour',
        minute: '#date-minute',
        second: '#date-second',
      },

      //Screen 4 CYA
      labelMessageCYA: '//span[contains(text(),"Check the information below carefully.")]',
      labelMessageCYA: '//span[contains(text(),"Check the information below carefully.")]',

  },

  async verifyCommon() {
    await I.wait(3);
    await I.retry(3).waitForSelector(this.fields.pageHeader1);
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
    await I.retry(3).seeElement(this.fields.goButton);
  },

  async verifyWhoDoYouNeedToContact() {
    await verifyCommon();
    await I.retry(3).seeElement(this.fields.labelMessageWhoDoYouNeedToContact);
    await I.retry(3).seeElement(this.fields.whoDoYouNeedToContactList);
  },

  async verifyWhatInformationDoYouNeed() {
    await verifyCommon();
    await I.retry(3).seeElement(this.fields.labelMessageWhatInformationDoYouNeed);
    await I.retry(3).seeElement(this.fields.askForAdditionalDocumentListOption);
    await I.retry(3).seeElement(this.fields.askAQuestionListOption);
  },

  async verifyWhenIsTheInformationNeededBy() {
    await verifyCommon();
    await I.retry(3).seeElement(this.fields.labelMessageWhenIsTheInformationNeededBy);
    await I.retry(3).seeElement(this.fields.whenIsTheInformationNeededByDateText.day);
    await I.retry(3).seeElement(this.fields.whenIsTheInformationNeededByDateText.month);
    await I.retry(3).seeElement(this.fields.whenIsTheInformationNeededByDateText.year);
    await I.retry(3).seeElement(this.fields.whenIsTheInformationNeededByDateText.hour);
    await I.retry(3).seeElement(this.fields.whenIsTheInformationNeededByDateText.minute);
    await I.retry(3).seeElement(this.fields.whenIsTheInformationNeededByDateText.second);
  },

  async navigationCheckCYA() {
    await I.wait(3);
    await I.retry(3).waitForSelector(this.fields.pageHeader2);
    await I.retry(3).click(this.fields.previousButton);
    await I.wait(3);
    await I.retry(3).click(this.fields.previousButton);
    await I.wait(3);
    await I.retry(3).click(this.fields.previousButton);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await verifyCommon();
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).waitForSelector(this.fields.actionsDropdown);
    await I.retry(3).seeElement(this.fields.actionsDropdown);
    await I.retry(3).seeElement(this.fields.goButton);
  },

  async fulfillWhoDoYouNeedToContact(){
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.retry(3).see('Field is required', this.fields.whoDoYouNeedToContactErrorMessage);
    await I.retry(3).click(this.fields.whoDoYouNeedToContactList);//[(Math.random() * this.fields.whoDoYouNeedToContactList.length) | 0]
    await I.retry(3).click(this.fields.continueButton);
  },

  async fulfillWhatInformationDoYouNeed(){
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.retry(3).see('What information do you need? is required', this.fields.whatInformationDoYouNeedListErrorMessage);
    await I.retry(3).click(this.fields.askForAdditionalDocumentListOption);
    await I.retry(3).click(this.fields.askAQuestionListOption);
    await I.retry(3).click(this.fields.continueButton);
    await I.retry(3).see('List the documents you need is required', this.fields.askForAdditionalDocumentErrorMessage);
    await I.retry(3).see('List the questions you need is required', this.fields.askAQuestionErrorMessage);
    await I.fillField(this.fields.askForAdditionalDocumentText, seekFurtherInfoDetails.askForAdditionalDocumentText);
    await I.fillField(this.fields.askAQuestionText, seekFurtherInfoDetails.askAQuestionText);
    await I.retry(3).click(this.fields.continueButton);
  },

  async fulfillWhenIsTheInformationNeededBy(){
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.retry(3).see('When is the information needed by? is required', this.fields.whenIsTheInformationNeededByDateErrorMessage);
    await I.retry(3).fillField(this.fields.whenIsTheInformationNeededByDateText.day, seekFurtherInfoDetails.seekFurtherInfoErrorDate.day);
    await I.retry(3).fillField(this.fields.whenIsTheInformationNeededByDateText.month, seekFurtherInfoDetails.seekFurtherInfoErrorDate.month);
    await I.retry(3).fillField(this.fields.whenIsTheInformationNeededByDateText.year, seekFurtherInfoDetails.seekFurtherInfoErrorDate.year);
    await I.retry(3).fillField(this.fields.whenIsTheInformationNeededByDateText.hour, seekFurtherInfoDetails.seekFurtherInfoErrorDate.hour);
    await I.retry(3).fillField(this.fields.whenIsTheInformationNeededByDateText.minute, seekFurtherInfoDetails.seekFurtherInfoErrorDate.minute);
    await I.retry(3).fillField(this.fields.whenIsTheInformationNeededByDateText.second, seekFurtherInfoDetails.seekFurtherInfoErrorDate.second);
    await I.retry(3).click(this.fields.continueButton);
    await I.retry(3).see('The data entered is not valid for When is the information needed by?', this.fields.whenIsTheInformationNeededByDateErrorMessage);
    await I.retry(3).fillField(this.fields.whenIsTheInformationNeededByDateText.day, seekFurtherInfoDetails.seekFurtherInfoDate.day);
    await I.retry(3).fillField(this.fields.whenIsTheInformationNeededByDateText.month, seekFurtherInfoDetails.seekFurtherInfoDate.month);
    await I.retry(3).fillField(this.fields.whenIsTheInformationNeededByDateText.year, seekFurtherInfoDetails.seekFurtherInfoDate.year);
    await I.retry(3).fillField(this.fields.whenIsTheInformationNeededByDateText.hour, seekFurtherInfoDetails.seekFurtherInfoDate.hour);
    await I.retry(3).fillField(this.fields.whenIsTheInformationNeededByDateText.minute, seekFurtherInfoDetails.seekFurtherInfoDate.minute);
    await I.retry(3).fillField(this.fields.whenIsTheInformationNeededByDateText.second, seekFurtherInfoDetails.seekFurtherInfoDate.second);
    await I.retry(3).click(this.fields.continueButton);
  },
};
