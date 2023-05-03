const config = require('../config');
const laHelper = require('../helpers/la_portal_case');
let caseId;

Feature('Case Details Other parties tab tests').retry(1);

async function setupScenario(I) {
  if (!caseId) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
  }
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}

Scenario('Verify headings are displayed in Check your answers page of amend other parties details', async ({I, caseViewPage, amendOtherPartiesDetailsPage }) => {
  // await setupScenario(I);
  // await caseViewPage.goToNewActions(config.administrationActions.amendOtherPartiesDetails);
  // await amendOtherPartiesDetailsPage.addChildLegalGuardianDetails();
  // await amendOtherPartiesDetailsPage.addChildLegalGuardianSolicitorDetails();
  // await amendOtherPartiesDetailsPage.addAdditionalAdoptionAgencyDetails();
  // await amendOtherPartiesDetailsPage.addBirthMotherSolicitorDetails();
  // await amendOtherPartiesDetailsPage.addBirthFatherSolicitorDetails();
  // await amendOtherPartiesDetailsPage.addOtherParentalRespSolicitorDetails();
  // await amendOtherPartiesDetailsPage.verifyHeadingsAreDisplayedInCheckYourAnswersPage();
});
