const config = require('../config');

const laHelper = require('../helpers/la_portal_case');
const manageOrderDetails = require('../fixtures/manageOrderDetails.js');
const finalAdoptionOrderDetails = require('../fixtures/finalAdoptionOrderDetails.js');

let caseId;

Feature('Manage order tests').retry(1);

async function setupScenario(I) {
  caseId = await laHelper.createCompleteCase();
  console.log('CCD Case number - ' + caseId);
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}

Scenario('Verify final adoption order details and CYA', async ({I, caseViewPage, manageOrdersPage, finalOrderPage}) => {
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
  await finalOrderPage.finalAdoptionOrderManagementDateOrderMadeCYAPage();
  await finalOrderPage.verifyFinalAdoptionOrderRecipientsCYA();
  await I.retry(3).seeEventSubmissionConfirmation(caseId, config.administrationActions.manageOrders);

});

