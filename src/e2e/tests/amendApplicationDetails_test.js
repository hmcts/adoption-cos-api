const config = require('../config');

const laHelper = require('../helpers/la_portal_case');

let caseId;
Feature('Amend Application Details').retry(1);
async function setupScenario(I) {
  if (!caseId) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
  }
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}
Scenario('Verify amend application details event', async ({I, caseViewPage,amendApplicationDetailsPage }) => {
     await setupScenario(I);
     await caseViewPage.goToNewActions(config.administrationActions.amendApplicantDetails);
     await amendApplicationDetailsPage.addApplicantsSolicitorDetails();
     await I.click('Continue');
     await amendApplicationDetailsPage.applicantsSolicitorDetailsCYA();
     await I.click('Save and continue');
     await I.seeEventSubmissionConfirmation(caseId,config.administrationActions.amendApplicantDetails);

});
