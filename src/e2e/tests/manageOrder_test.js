const config = require('../config');

const laHelper = require('../helpers/la_portal_case');

let caseId;

Feature('Manage order tests').retry(0);

async function setupScenario(I) {
  if (!caseId) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
  }
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}
Scenario('Verify Preamble and Reallocate Judge in Case Management Order details', async ({I, caseViewPage, manageOrdersPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.verifyCaseDetails();
  await manageOrdersPage.verifyErrorMessage();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await manageOrdersPage.selectCaseManagementOrderAndVerify(caseId);
  await manageOrdersPage.addPreambleAndReallocateJudgeInCaseManagementOrder();
  await manageOrdersPage.caseManagementOrderPreambleAndReallocateJudgeCYAPage();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.manageOrders);
});

Scenario('Verify Case management order first directions details', async ({I, caseViewPage, manageOrdersPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.verifyCaseDetails();
  await manageOrdersPage.verifyErrorMessage();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await manageOrdersPage.selectCaseManagementOrderAndVerify(caseId);
  await manageOrdersPage.caseManagementOrderFirstDirections();
  await manageOrdersPage.caseManagementOrderFirstDirectionsCYAPage();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.manageOrders);
});

Scenario('Verify Case management order Local authority File adoption agency report details', async ({I, caseViewPage, manageOrdersPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.verifyCaseDetails();
  await manageOrdersPage.verifyErrorMessage();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await manageOrdersPage.selectCaseManagementOrderAndVerify(caseId);
  await manageOrdersPage.caseManagementOrderLocalAuthority();
  await manageOrdersPage.caseManagementOrderLocalAuthorityCYAPage();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.manageOrders);
});

Scenario('Verify Case management order Attendance details', async ({I, caseViewPage, manageOrdersPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.verifyCaseDetails();
  await manageOrdersPage.verifyErrorMessage();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await manageOrdersPage.selectCaseManagementOrderAndVerify(caseId);
  await manageOrdersPage.caseManagementOrderAttendance();
  await manageOrdersPage.caseManagementOrderLocalAuthorityCYAPage();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.manageOrders);
});
