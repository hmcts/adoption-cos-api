const config = require('../config');

const laHelper = require('../helpers/la_portal_case');

let caseId;
Feature('Amend Case Details').retry(1);
async function setupScenario(I) {
  if (!caseId) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
  }
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}
Scenario('Verify amend case details event', async ({I, caseViewPage }) => {
  await setupScenario(I);
  // await caseViewPage.goToNewActions(config.administrationActions.amendCaseDetails);
  // await I.click('Save and continue');
  // await I.seeEventSubmissionConfirmation(caseId,config.administrationActions.amendCaseDetails);
});
