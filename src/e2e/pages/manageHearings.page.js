const config = require('../config');
const { I } = inject();
const manageHearingFormData = require('../fixtures/manageHearings');
module.exports = {
  fields: {
    allocateJudgeTitle: '//h1[contains(text(),"Manage hearings")]',
    childNameHeader: '//h3[contains(text(),"Child\'s Name: child child")]',
    continueButton: 'button[type="submit"]',
    errorMessage: '//span[contains(text(),"Field is required")]',
    alertMessage: '//div[@class="alert-message"]',
    newHearing: '#manageHearingOptions-addNewHearing',
    vacateHearing: '#manageHearingOptions-vacateHearing',
    adjournHearing: '#manageHearingOptions-adjournHearing',
    hearingType: '#manageHearingDetails_typeOfHearing',
    hearingDay: '#hearingDateAndTime-day',
    hearingMonth: '#hearingDateAndTime-month',
    hearingYear: '#hearingDateAndTime-year',
    hearingHour: '#hearingDateAndTime-hour',
    hearingMinute: '#hearingDateAndTime-minute',
    hearingSeconds: '#hearingDateAndTime-second',
    lengthOfHearing: '#manageHearingDetails_lengthOfHearing',
    judge: '#manageHearingDetails_judge',
    court: '#manageHearingDetails_court',
    remoteMethod: '#manageHearingDetails_methodOfHearing-remote',
  },

  async verifyPageDetails() {
    await I.retry(3).seeElement(this.fields.allocateJudgeTitle);
    await I.retry(3).seeElement(this.fields.childNameHeader);
    await I.retry(3).seeElement(this.fields.continueButton);
    await I.retry(3).seeElement(this.fields.newHearing);
    await I.retry(3).seeElement(this.fields.vacateHearing);
    await I.retry(3).seeElement(this.fields.adjournHearing);
  },

  async verifyManageHearingsPageFunctionality(){
   await I.retry(3).click(this.fields.continueButton);
   await I.retry(6).seeElement(this.fields.errorMessage);
   await I.retry(5).click(this.fields.newHearing);
   await I.retry(5).click(this.fields.continueButton);
  },

  async addNewHearingOptions(){
    await I.fillField(this.fields.hearingType, manageHearingFormData.typeOfHearing);
    await I.fillField(this.fields.hearingDay, manageHearingFormData.dayOfHearing);
    await I.fillField(this.fields.hearingMonth, manageHearingFormData.monthOfHearing);
    await I.fillField(this.fields.hearingYear, manageHearingFormData.yearOfHearing);
    await I.fillField(this.fields.hearingHour, manageHearingFormData.hourOfHearing);
    await I.fillField(this.fields.hearingMinute, manageHearingFormData.minuteOfHearing);
    await I.fillField(this.fields.hearingSeconds, manageHearingFormData.secondOfHearing);
    await I.fillField(this.fields.lengthOfHearing, manageHearingFormData.lengthOfHearing);
    await I.fillField(this.fields.judge, manageHearingFormData.judgeOfHearing);
    await I.fillField(this.fields.court, manageHearingFormData.courtOfHearing);
    await I.retry(5).click(this.fields.remoteMethod);
    await I.retry(5).click(this.fields.continueButton);
  },
};
