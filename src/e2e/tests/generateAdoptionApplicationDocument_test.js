const config = require('../config');
const laHelper = require('../helpers/la_portal_case');

let caseId;
Feature('Generate Combined Documents').retry(1);
async function setupScenario(I) {
  if (!caseId) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
  }
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}
Scenario('Verify Combined Document Generated', async ({I, caseViewPage }) => {
  await setupScenario(I);
  await caseViewPage.navigateToTab(config.tabs.documentsTab);
  await caseViewPage.verifyTableDataInsideTab(['Combined documents generated 1','Document'], 'adoption-laapplication')

});
