const config = require('../config');

const laHelper = require('../helpers/la_portal_case');
const manageOrderDetails = require('../fixtures/manageOrderDetails.js');
const finalAdoptionOrderDetails = require('../fixtures/finalAdoptionOrderDetails.js');

let caseId;

Feature('Manage order tests').retry(0);

async function setupScenario(I) {
  caseId = await laHelper.createCompleteCase();
  console.log('CCD Case number - ' + caseId);
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}

Scenario('Verify final adoption order details check and send Orders and CYA', async ({I, caseViewPage, manageOrdersPage, finalOrderPage,checkAndSendOrdersPage, manageOrderTabPage}) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.verifyCaseDetails();
  await manageOrdersPage.selectCreateNewOrder();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await finalOrderPage.selectFinalOrderAndVerify();
  await finalOrderPage.addJudgeNameChildAdoptionNameAndDateOrderMade()
  await finalOrderPage.verifyFAODateAndPlaceOfBirth();
  await finalOrderPage.verifyFinalAdoptionRegistrationDetails();
  await finalOrderPage.verifyFinalAdoptionOrderRecipients();
  await finalOrderPage.verifyOrderPreviewScreen();
  await finalOrderPage.finalAdoptionOrderManagementDateOrderMadeCYAPage();
  await finalOrderPage.verifyFinalAdoptionOrderRecipientsCYA();
  await I.retry(3).seeEventSubmissionConfirmation(caseId, config.administrationActions.manageOrders);

  await manageOrderTabPage.selectOrderTab();
  await manageOrderTabPage.verifyOrderDetailsUnderOrdersTabs();
  await manageOrderTabPage.verifyFAOrderStatus()
  await manageOrderTabPage.faOrderRecipients();


  await caseViewPage.goToNewActions(config.administrationActions.checkAndSendOrders);
  await checkAndSendOrdersPage.verifyCheckAndSendOrdersPageDetails();
  await checkAndSendOrdersPage.verifyOrderForReview();
  await checkAndSendOrdersPage.FinalAdoptionOrderRecipients();
  await checkAndSendOrdersPage.selectServerOrder();
  await checkAndSendOrdersPage.verifyOrderTypeCYA();
  await checkAndSendOrdersPage.serverOrderCYA();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.checkAndSendOrders);

  await manageOrderTabPage.selectOrderTab();
  await manageOrderTabPage.verifyOrderDetailsUnderOrdersTabs();
  await manageOrderTabPage.verifyOrderServed();
  await manageOrderTabPage.faOrderRecipients();


});

