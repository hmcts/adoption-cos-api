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

Scenario('Verify final adoption order screen', async ({I, caseViewPage, manageOrdersPage, finalOrderPage}) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.verifyCaseDetails();
  await manageOrdersPage.selectCreateNewOrder();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await finalOrderPage.selectFinalOrderAndVerify();
  await finalOrderPage.addJudgeNameAndChildAdoptionName();
});

Scenario('Verify final adoption place and time of Birth', async ({I, caseViewPage, manageOrdersPage, finalOrderPage}) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.verifyCaseDetails();
  await manageOrdersPage.selectCreateNewOrder();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await finalOrderPage.selectFinalOrderAndVerify();
  await finalOrderPage.addJudgeNameAndChildAdoptionName();
  await finalOrderPage.verifyFAODateAndPlaceOfBirth();
  await finalOrderPage.verifyFinalAdoptionRegistrationDetails();

});

Scenario('Verify final adoption order recipients and CYA', async ({I, caseViewPage, manageOrdersPage, finalOrderPage}) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.verifyCaseDetails();
  await manageOrdersPage.selectCreateNewOrder();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await finalOrderPage.selectFinalOrderAndVerify();
  await finalOrderPage.addJudgeNameAndChildAdoptionName();
  await finalOrderPage.verifyFAODateAndPlaceOfBirth();
  await finalOrderPage.verifyFinalAdoptionRegistrationDetails();
  await finalOrderPage.verifyFinalAdoptionOrderRecipients();
  await finalOrderPage.verifyFinalAdoptionOrderRecipientsCYA();
  await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.manageOrders);

});
