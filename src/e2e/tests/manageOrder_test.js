const config = require('../config');

const laHelper = require('../helpers/la_portal_case');

let caseId;

Feature('Manage order tests').retry(1);

async function setupScenario(I) {
  if (!caseId) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
  }
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}
Scenario('Verify case management order details', async ({I, caseViewPage, manageOrdersPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.verifyCaseDetails();
  await manageOrdersPage.verifyErrorMessage();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await manageOrdersPage.selectCaseManagementOrderAndVerify(caseId);
  await manageOrdersPage.addPreambleAndReallocateJudgeInCaseManagementOrder();
  await manageOrdersPage.caseManagementOrderCYAPage();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.manageOrders);
});
