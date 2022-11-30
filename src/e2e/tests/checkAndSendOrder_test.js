const config = require('../config');
const laHelper = require('../helpers/la_portal_case');
let caseId;

Feature('Check and Send orders tests').retry(0);

async function setupScenario(I) {
  caseId = await laHelper.createCompleteCase();
  console.log('CCD Case number - ' + caseId);
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
  await I.wait(2);
}

Scenario('Order Creation - send and check orders', async ({I, caseViewPage, manageOrdersPage,checkAndSendOrdersPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.selectCreateNewOrder();
  await manageOrdersPage.selectCaseManagementOrderAndVerify();
  await manageOrdersPage.addDateOrderMade();
  await manageOrdersPage.addPreambleAndReallocateJudgeInCaseManagementOrder();
  await manageOrdersPage.caseManagementOrderServeParties();
  await manageOrdersPage.caseManagementOrderPreambleReAllocatedCYAPage();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.manageOrders);
  await caseViewPage.goToNewActions(config.administrationActions.checkAndSendOrders);
  await checkAndSendOrdersPage.selectOrderToReview();
  await checkAndSendOrdersPage.verifyOrderForReview();
  await checkAndSendOrdersPage.gateKeepingOrderRecipients();
  await checkAndSendOrdersPage.selectServerOrder();
  await checkAndSendOrdersPage.verifyOrderTypeCYA();
  await checkAndSendOrdersPage.serverOrderCYA();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.checkAndSendOrders);
});



