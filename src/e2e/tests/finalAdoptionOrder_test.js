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

Scenario('Verify final adoption order screen', async ({I, caseViewPage, manageOrdersPage, finalOrderPage}) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  await manageOrdersPage.verifyCaseDetails();
  await manageOrdersPage.selectCreateNewOrder();
  await manageOrdersPage.verifyTypeOfOrdersListed();
  await finalOrderPage.selectFinalOrderAndVerify();
  await finalOrderPage.addJudgeNameAndChildAdoptionName();
});
