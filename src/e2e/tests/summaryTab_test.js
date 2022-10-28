const config = require('../config');

const laHelper = require('../helpers/la_portal_case');

let caseId;

Feature('case Details Summary tab tests').retry(0);

async function setupScenario(I) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
    await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}

Scenario('Verify details of sibling and other placement orders are displayed in the summary page', async ({I, caseDetailsSummaryPage, caseViewPage}) => {
  await setupScenario(I);
  await caseViewPage.navigateToTab(caseViewPage.fields.tabs.summary);
  await caseDetailsSummaryPage.verifyPlacementTableOptions();
  await caseDetailsSummaryPage.verifyLinkedCasesTableOptions();
  await caseDetailsSummaryPage.verifySiblingCourtCaseTableOptions();
});
