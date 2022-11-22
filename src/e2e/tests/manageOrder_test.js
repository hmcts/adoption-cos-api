const config = require('../config');

const laHelper = require('../helpers/la_portal_case');
const manageOrderDetails = require('../fixtures/manageOrderDetails.js');

let caseId;

Feature('Manage order tests').retry(1);

async function setupScenario(I) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
    await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}
Scenario('Verify case management order Preamble and Reallocation judge details', async ({I, caseViewPage, manageOrdersPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.verifyCaseDetails();
  await manageOrdersPage.selectCreateNewOrder();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await manageOrdersPage.selectCaseManagementOrderAndVerify();
  await manageOrdersPage.addDateOrderMade();
  await manageOrdersPage.addPreambleAndReallocateJudgeInCaseManagementOrder();
  await manageOrdersPage.caseManagementOrderServeParties();
  await manageOrdersPage.caseManagementOrderPreambleReAllocatedCYAPage();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.manageOrders);
});
Scenario('Verify Case management order Local authority File adoption agency report details', async ({I, caseViewPage, manageOrdersPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.selectCreateNewOrder();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await manageOrdersPage.selectCaseManagementOrderAndVerify(caseId);
  await manageOrdersPage.addDateOrderMade();
  await manageOrdersPage.caseManagementOrderLocalAuthority();
  await manageOrdersPage.caseManagementOrderServeParties();
  await manageOrdersPage.caseManagementOrderLocalAuthorityCYAPage();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.manageOrders);
});
Scenario('Verify Case management order List for first hearing details', async ({I, caseViewPage, manageOrdersPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.selectCreateNewOrder();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await manageOrdersPage.selectCaseManagementOrderAndVerify(caseId);
  await manageOrdersPage.addDateOrderMade();
  await manageOrdersPage.caseManagementOrderListForFirstHearing();
  await manageOrdersPage.caseManagementOrderServeParties();
  await manageOrdersPage.caseManagementOrderListForFirstHearingCYAPage();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.manageOrders);
});
Scenario('Verify case management order Preamble and pre allocation judge details', async ({I, caseViewPage, manageOrdersPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.selectCreateNewOrder();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await manageOrdersPage.selectCaseManagementOrderAndVerify();
  await manageOrdersPage.addDateOrderMade();
  await manageOrdersPage.addPreambleAndPreviousAllocateJudgeInCaseManagementOrder();
  await manageOrdersPage.caseManagementOrderServeParties();
  await manageOrdersPage.caseManagementOrderPreambleAllocatedCYAPage();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.manageOrders);
  await I.selectTab(caseViewPage.fields.tabs.summary);
  await I.seeInTab('Name of the judge', manageOrderDetails.caseManagementOrderDetails.nameOfAllocatedJudge);
});
Scenario('Verify Case management order Attendance details', async ({I, caseViewPage, manageOrdersPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.selectCreateNewOrder();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await manageOrdersPage.selectCaseManagementOrderAndVerify(caseId);
  await manageOrdersPage.addDateOrderMade();
  await manageOrdersPage.caseManagementOrderAttendance();
  await manageOrdersPage.caseManagementOrderServeParties();
  await manageOrdersPage.caseManagementOrderAttendanceCYAPage();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.manageOrders);
});

Scenario('Verify Case management order Leave to oppose and Cost orders details', async ({I, caseViewPage, manageOrdersPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.selectCreateNewOrder();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await manageOrdersPage.selectCaseManagementOrderAndVerify(caseId);
  await manageOrdersPage.addDateOrderMade();
  await manageOrdersPage.caseManagementOrderLeaveToOpposeAndCostOrder();
  await manageOrdersPage.caseManagementOrderServeParties();

});

Scenario('Verify Case management order CAFCASS details', async ({I, caseViewPage, manageOrdersPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.selectCreateNewOrder();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await manageOrdersPage.selectCaseManagementOrderAndVerify(caseId);
  await manageOrdersPage.addDateOrderMade();
  await manageOrdersPage.caseManagementOrderCafcass();
  await manageOrdersPage.caseManagementOrderServeParties();

});

Scenario('Verify Case management order details add Serve Parties and CYA', async ({I, caseViewPage, manageOrdersPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.selectCreateNewOrder();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await manageOrdersPage.selectCaseManagementOrderAndVerify(caseId);
  await manageOrdersPage.addDateOrderMade();
  await manageOrdersPage.caseManagementOrderAddAdditionalParagraph();
  await manageOrdersPage.caseManagementOrderServeParties();
  await manageOrdersPage.caseManagementOrderDateOrderMadeCYAPage();
  await manageOrdersPage.caseManagementOrderServePartiesCYAPage();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.manageOrders);
});


