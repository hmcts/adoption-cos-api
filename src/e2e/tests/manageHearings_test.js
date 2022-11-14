const config = require('../config');
const laHelper = require('../helpers/la_portal_case');
let caseId;

Feature('Manage Hearings tests').retry(1);

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
  await manageHearingsPage.verifyPageDetails();
  await manageHearingsPage.verifyManageHearingsPageFunctionality();
  await manageHearingsPage.addNewHearingOptions();
  await manageHearingsPage.addRecepientDetails();
  await manageHearingsPage.verifyAddNewHearingCheckYourAnswers();
  });

 Scenario('Manage Hearings of a case - Vacate Hearing with Agreement and Relisting', async ({I,loginPage, caseListPage, caseViewPage, manageHearingsPage }) => {
    await setupScenario(I);
    await caseViewPage.goToNewActions(config.administrationActions.manageHearings);
    await manageHearingsPage.verifyPageDetails();
    await manageHearingsPage.verifyManageHearingsPageFunctionality();
    await manageHearingsPage.addNewHearingOptions();
    await manageHearingsPage.addRecepientDetails();
    await manageHearingsPage.verifyAddNewHearingCheckYourAnswers();
    await caseViewPage.goToNewActions(config.administrationActions.manageHearings);
    await manageHearingsPage.selectVacateHearingOptionWithAgreementAndRelisting(manageHearingsPage.fields.vacateHearingReasonAgreement);
});
