const config = require('../config');

const laHelper = require('../helpers/la_portal_case');

let caseId;

Feature('Payment Tab tests').retry(0);

async function setupScenario(I) {
  if (!caseId) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
  }
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}
Scenario('Verify Payment details of a case', async ({I, caseViewPage, paymentDetailsPage }) => {
  await setupScenario(I);
  await caseViewPage.navigateToTab('Payment');
  await paymentDetailsPage.verifyPaymentDetails();
});
