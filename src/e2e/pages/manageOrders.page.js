const config = require('../config');
const { I } = inject();
const manageOrderDetails = require('../fixtures/manageOrderDetails.js');
const tranferCourtData = require("../fixtures/tranferCourt");
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
};

