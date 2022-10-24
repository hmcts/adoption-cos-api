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

  async caseManagementOrderCYAPage(){
    await I.wait(3);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.preamble);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.nameOfReallocatedJudge);
    await I.retry(3).click(this.fields.continueButton);
  },
};
