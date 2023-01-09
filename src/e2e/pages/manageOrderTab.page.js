const config = require('../config');
const {I} = inject();

const finalAdoptionOrderDetails = require('../fixtures/finalAdoptionOrderDetails.js');
const manageOrderDetails = require("../fixtures/manageOrderDetails.js");
module.exports = {

  fields: {
    createNewOrder: '//*[text()="Create new order"]',
    ordersTab: '//div[text()="Orders"]',
  },

  async selectOrderTab() {
    await I.wait(3);
    await I.retry(3).click(this.fields.ordersTab);
    await I.wait(5);
  },

  async verifyOrderDetailsUnderOrdersTabs() {
    await I.retry(3).seeElement(this.fields.createNewOrder)
    await I.retry(3).see(manageOrderDetails.orderTab.dateOfTheOrder);
    await I.retry(3).see('12 Mar 2021');
    await I.retry(3).see(manageOrderDetails.orderTab.orderByJudgeName);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.orderedBy);
    await I.retry(3).see(manageOrderDetails.orderTab.orderType);
    await I.retry(3).see(manageOrderDetails.orderTab.status);
  },

  async gateKeepingOrdersRecipients() {
    await I.retry(3).see(manageOrderDetails.orderTab.recipients);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.applicantLocalAuthority);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.birthMother);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.birthFather);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.applicants);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.childLocalAuthority);
   // await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.otherAdoptionAgency);
   // await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.otherPersonWithParentalResponsibility);
   // await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.cafCass);
  },
  async faOrderRecipients() {
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.faoA76);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.faoA206);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.firstApplicant);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.secondApplicant);
    //await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.birthFather);
    //await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.birthMother);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.applicantLocalAuthority);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.childLocalAuthority);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.adoptionAgency);
  },


  async verifyGateKeepingOrderTypeAndStatus() {
    await I.retry(3).see(manageOrderDetails.orderTab.gateKeepingOrder);
    await I.retry(3).see(manageOrderDetails.orderTab.checkAndSend);
  },

  async verifyFAOrderStatus() {
    await I.retry(3).see("Final Adoption Order");
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.orderPDF)
    await I.retry(3).see(manageOrderDetails.orderTab.checkAndSend);
  },

  async verifyOrderServed() {
    await I.retry(3).see(manageOrderDetails.orderTab.statusServed);
  },


}
