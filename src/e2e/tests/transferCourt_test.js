const config = require('../config');
const tranferCourtData = require('../fixtures/tranferCourt');
const laHelper = require('../helpers/la_portal_case');

let caseId;
Feature('Transfer court Details').retry(1);
async function setupScenario(I) {
  if (!caseId) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
  }
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}
Scenario('Verify caseworker able to Transfer court', async ({I, caseViewPage,transferCourtPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.transferCourt);
  await transferCourtPage.courtNameDetails();
  await I.seeEventSubmissionConfirmation(caseId,config.administrationActions.transferCourt);
  await I.selectTab(caseViewPage.fields.tabs.summary);
  await I.seeInTab('Allocated court', tranferCourtData.changeCourtName);
});
