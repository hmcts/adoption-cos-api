const config = require('../config');
const { I } = inject();
const manageOrderDetails = require('../fixtures/manageOrderDetails.js');
module.exports = {
  fields: {
    caseId: 'h3:nth-child(1)',
    ChildName: 'h3:nth-child(2)',
    continueButton: 'button[type="submit"]',
    errorMessage: '//span[starts-with(@class,"error-message")]',
    createOrder:'#manageOrderActivity-createOrder',
    caseManagementOrder: '#manageOrderType-caseManagementOrder',
    generalDirectionsOrder: '#manageOrderType-generalDirectionsOrder',
    finalAdoptionOrder: '#manageOrderType-finalAdoptionOrder',
    manageOrdersUpdate: '//div[contains(text(), "has been updated with event: Manage orders")]',
    preambleDetails: '#preambleDetails',
    reallocateJudge: '#allocationJudge-reallocateJudge',
    nameOfJudge: '#nameOfJudge',
    listForFirstHearing: '#hearingNotices-listForFirstHearing',
    hearingDetails:{
      dateAndTimeFirstHearingErrorMessage: '#dateAndTimeFirstHearing .error-message',
      dateAndTimeFirstHearingDay: '#dateAndTimeFirstHearing-day',
      dateAndTimeFirstHearingMonth: '#dateAndTimeFirstHearing-month',
      dateAndTimeFirstHearingYear: '#dateAndTimeFirstHearing-year',
    },
    lengthOfHearingFirstHearing: '#lengthOfHearingFirstHearing',
    lengthOfHearingFirstHearingErrorMessage: 'ccd-field-write[field_id=\'lengthOfHearingFirstHearing\'] .error-message',
    localAuthority: {
      selectLocalAuthority: '#selectedLocalAuthority-fileAdoptionAgencyReport',
      dateAndTimeForOption1Day: '#dateAndTimeForOption1-day',
      dateAndTimeForOption1Month: '#dateAndTimeForOption1-month',
      dateAndTimeForOption1Year: '#dateAndTimeForOption1-year',
      dateAndTimeForOption2Day: '#timeForOption2-day',
      dateAndTimeForOption2Month: '#timeForOption2-month',
      dateAndTimeForOption2Year: '#timeForOption2-year',
      dateAndTimeForOption1Error:'#dateAndTimeForOption1 .error-message',
      dateAndTimeForOption2Error:'#timeForOption2 .error-message',

    },

  },

  async verifyCaseDetails() {
    await I.wait(3);
    await I.retry(3).waitForSelector(this.fields.caseId);
    await I.retry(3).seeElement(this.fields.caseId);
    await I.retry(3).seeElement(this.fields.ChildName);
  },

  async verifyErrorMessage(){
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.retry(3).seeElement(this.fields.errorMessage);
    await I.retry(3).click(this.fields.createOrder);
    await I.retry(3).click(this.fields.continueButton);
  },


  async verifyTypeOfOrdersListed(){
    await I.wait(3);
    await I.retry(3).seeElement(this.fields.caseManagementOrder);
    await I.retry(3).seeElement(this.fields.generalDirectionsOrder);
    await I.retry(3).seeElement(this.fields.finalAdoptionOrder);
  },

  async selectCaseManagementOrderAndVerify(caseId){
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.retry(3).seeElement(this.fields.errorMessage);
    await I.retry(3).click(this.fields.caseManagementOrder);
    await I.retry(3).click(this.fields.continueButton);
  },
  async addPreambleAndReallocateJudgeInCaseManagementOrder(){
    await I.wait(3);
    await I.retry(3).fillField(this.fields.preambleDetails, manageOrderDetails.caseManagementOrderDetails.preamble);
    await I.wait(3);
    await I.retry(3).click(this.fields.reallocateJudge);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.preamble);
    await I.retry(3).fillField(this.fields.nameOfJudge, manageOrderDetails.caseManagementOrderDetails.nameOfReallocatedJudge);
    await I.retry(3).click(this.fields.continueButton);
  },
  async caseManagementOrderFirstDirections(){
    await I.wait(3);
    await I.retry(3).click(this.fields.listForFirstHearing);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.errorMessage,this.fields.hearingDetails.dateAndTimeFirstHearingErrorMessage);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.errorMessage,this.fields.lengthOfHearingFirstHearingErrorMessage);
    await I.retry(3).fillField(this.fields.hearingDetails.dateAndTimeFirstHearingDay, manageOrderDetails.caseManagementOrderDetails.hearingDate.day);
    await I.retry(3).fillField(this.fields.hearingDetails.dateAndTimeFirstHearingMonth, manageOrderDetails.caseManagementOrderDetails.hearingDate.month);
    await I.retry(3).fillField(this.fields.hearingDetails.dateAndTimeFirstHearingYear, manageOrderDetails.caseManagementOrderDetails.hearingDate.year);
    await I.retry(3).fillField(this.fields.lengthOfHearingFirstHearing, manageOrderDetails.caseManagementOrderDetails.lengthOfHearingFirstHearing);
    await I.retry(3).click(this.fields.continueButton);
  },

  async caseManagementOrderPreambleAndReallocateJudgeCYAPage(){
    await I.wait(3);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.preamble);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.nameOfReallocatedJudge);
    await I.retry(3).click(this.fields.continueButton);
  },

  async caseManagementOrderFirstDirectionsCYAPage(){
    await I.wait(3);
    await I.retry(3).see('1 Jan 2050, 12:00:00 AM');
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.lengthOfHearingFirstHearing);
    await I.retry(3).click(this.fields.continueButton);
  },

  async caseManagementOrderLocalAuthority(){
    await I.wait(3);
    await I.retry(3).click(this.fields.localAuthority.selectLocalAuthority);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.errorMessage,this.fields.localAuthority.dateAndTimeForOption1Error);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.errorMessage,this.fields.localAuthority.dateAndTimeForOption2Error);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.localAuthority.dateAndTimeForOption1Day, manageOrderDetails.caseManagementOrderDetails.localAuthorityOption1.day);
    await I.retry(3).fillField(this.fields.localAuthority.dateAndTimeForOption1Month, manageOrderDetails.caseManagementOrderDetails.localAuthorityOption1.month);
    await I.retry(3).fillField(this.fields.localAuthority.dateAndTimeForOption1Year, manageOrderDetails.caseManagementOrderDetails.localAuthorityOption1.year);
    await I.retry(3).fillField(this.fields.localAuthority.dateAndTimeForOption2Day, manageOrderDetails.caseManagementOrderDetails.localAuthorityOption2.day);
    await I.retry(3).fillField(this.fields.localAuthority.dateAndTimeForOption2Month, manageOrderDetails.caseManagementOrderDetails.localAuthorityOption2.month);
    await I.retry(3).fillField(this.fields.localAuthority.dateAndTimeForOption2Year, manageOrderDetails.caseManagementOrderDetails.localAuthorityOption2.year);
    await I.retry(3).click(this.fields.continueButton);
  },

  async caseManagementOrderLocalAuthorityCYAPage(){
    await I.wait(3);
    await I.retry(3).see('1 Jan 2029, 12:00:00 AM');
    await I.retry(3).see('1 Jan 2030, 12:00:00 AM');
    await I.retry(3).click(this.fields.continueButton);
  },
};
