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
  await manageHearingsPage.verifyPreviewDraft();
  await manageHearingsPage.verifyAddNewHearingCheckYourAnswers();
  await caseViewPage.navigateToTab('Hearings');
  await manageHearingsTabPage.verifyAddNewHearingTabDetails();
});

 Scenario('Hearings Tab - Vacate Hearing with Agreement and Relisting', async ({I,loginPage, caseListPage, caseViewPage, manageHearingsPage, manageHearingsTabPage }) => {
    await setupScenario(I);
    await caseViewPage.goToNewActions(config.administrationActions.manageHearings);
    await manageHearingsPage.genericAddNewHearing();
    await caseViewPage.goToNewActions(config.administrationActions.manageHearings);
    await manageHearingsPage.selectVacateHearingOptionWithAgreementAndRelisting();
    await manageHearingsPage.addNewHearingForVacateHearingOptions();
    await manageHearingsPage.addRecepientDetails();
   await manageHearingsPage.verifyPreviewDraft();
    await manageHearingsPage.verifyVacateHearingWithRelistingCheckYourAnswers();
    await caseViewPage.navigateToTab('Hearings');
    await manageHearingsTabPage.verifyNewHearingForVacateHearingWithRelisting();
    await manageHearingsTabPage.verifyVacatedHearingForVacateHearingWithRelisting();
});

Scenario('Hearings Tab - Vacate Hearing with NO Relisting', async ({I,loginPage, caseListPage, caseViewPage, manageHearingsPage, manageHearingsTabPage }) => {
    await setupScenario(I);
    await caseViewPage.goToNewActions(config.administrationActions.manageHearings);
    await manageHearingsPage.genericAddNewHearing();
    await caseViewPage.goToNewActions(config.administrationActions.manageHearings);
    await manageHearingsPage.selectVacateHearingOptionWithAgreementAndNoRelisting();
    await manageHearingsPage.verifyVacateHearingNoRelistingCheckYourAnswers();
    await caseViewPage.navigateToTab('Hearings');
    await manageHearingsTabPage.verifyVacatedHearingForVacateHearingWithRelisting();
});
