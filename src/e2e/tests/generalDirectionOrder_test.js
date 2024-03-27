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

Scenario('verify General adoption Order details', async ({I, caseViewPage, manageOrdersPage, generalDirectionOrderPage }) => {
  // await setupScenario(I);
  // await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
  // await manageOrdersPage.verifyCaseDetails();
  // await manageOrdersPage.selectCreateNewOrder();
  // await manageOrdersPage.verifyTypeOfOrdersListed();
  // await generalDirectionOrderPage.selectGeneralDirectionOrderAndVerify();
  // await generalDirectionOrderPage.verifyGeneralDirectionOrderDetails();
  // await generalDirectionOrderPage.verifyProductionOrderDetails();
  // await generalDirectionOrderPage.selectRecipientsOfThisOrderDetails();
  // await generalDirectionOrderPage.verifyPreviewTheOrderDetails();


})
