const config = require('../config');

const laHelper = require('../helpers/la_portal_case');
const manageOrderDetails = require('../fixtures/manageHearings.js');

let caseId;

Feature('Manage Hearings Tab tests').retry(0);

async function setupScenario(I) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
    await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}
Scenario('Verify details of manage hearing tab when a new hearing is added', async ({I, caseViewPage, manageHearingsPage,manageHearingsTabPage  }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageHearings);
  await manageHearingsPage.verifyPageDetails();
  await manageHearingsPage.verifyManageHearingsPageFunctionality();
  await manageHearingsPage.addNewHearingOptions();
  await manageHearingsPage.addRecepientDetails();
  await manageHearingsPage.verifyAddNewHearingCheckYourAnswers();
  await caseViewPage.navigateToTab('Hearings');
  await manageHearingsTabPage.verifyAddNewHearingTabDetails();
});
