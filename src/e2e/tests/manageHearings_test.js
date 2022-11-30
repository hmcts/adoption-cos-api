const config = require('../config');
const laHelper = require('../helpers/la_portal_case');
let caseId;

Feature('Manage Hearings tests').retry(0);

async function setupScenario(I) {
  if (!caseId) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
  }
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}

Scenario('Manage Hearings of a case - Add new Hearing', async ({I,loginPage, caseListPage, caseViewPage, manageHearingsPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageHearings);
  await manageHearingsPage.genericAddNewHearing();
  });

 Scenario('Manage Hearings of a case - Vacate Hearing with Agreement and Relisting', async ({I,loginPage, caseListPage, caseViewPage, manageHearingsPage }) => {
    await setupScenario(I);
    await caseViewPage.goToNewActions(config.administrationActions.manageHearings);
    await manageHearingsPage.genericAddNewHearing();
    await caseViewPage.goToNewActions(config.administrationActions.manageHearings);
    await manageHearingsPage.selectVacateHearingOptionWithAgreementAndRelisting();
    await manageHearingsPage.addNewHearingForVacateHearingOptions();
    await manageHearingsPage.addRecepientDetails();
    await manageHearingsPage.verifyPreviewDraft()
    await manageHearingsPage.verifyVacateHearingWithRelistingCheckYourAnswers();
});

Scenario('Manage Hearings of a case - Vacate Hearing with NO Relisting', async ({I,loginPage, caseListPage, caseViewPage, manageHearingsPage }) => {
    await setupScenario(I);
    await caseViewPage.goToNewActions(config.administrationActions.manageHearings);
    await manageHearingsPage.genericAddNewHearing();
    await caseViewPage.goToNewActions(config.administrationActions.manageHearings);
    await manageHearingsPage.selectVacateHearingOptionWithAgreementAndNoRelisting();
    await manageHearingsPage.verifyVacateHearingNoRelistingCheckYourAnswers();
});

Scenario('Manage Hearings of a case - Adjourn Hearing with NO Relisting', async ({I,loginPage, caseListPage, caseViewPage, manageHearingsPage }) => {
    await setupScenario(I);
    await caseViewPage.goToNewActions(config.administrationActions.manageHearings);
    await manageHearingsPage.genericAddNewHearing();
    await caseViewPage.goToNewActions(config.administrationActions.manageHearings);
    await manageHearingsPage.selectAdjournHearingNoRelisting();
    await manageHearingsPage.verifyAdjournHearingNoRelistingCheckYourAnswers();
});
