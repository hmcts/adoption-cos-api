const config = require('../config');
const laHelper = require('../helpers/la_portal_case');
const manageOrderDetails = require('../fixtures/seekFurtherInfoDetails.js');

let caseId;

Feature('Correspondence Tab tests').retry(1);

async function setupScenario(I) {
  if (!caseId) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
  }
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}

Scenario('Correspondence Tab verify details test', async ({I, caseViewPage, seekFurtherInfoPage, correspondenceTabPage }) => {
  // await setupScenario(I);
  // await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
  // await seekFurtherInfoPage.fulfillWhoDoYouNeedToContact();
  // await seekFurtherInfoPage.fulfillWhatInformationDoYouNeed();
  // await seekFurtherInfoPage.fulfillWhenIsTheInformationNeededBy();
  // await seekFurtherInfoPage.verifyPreviewAndCheckTheLetter();
  // await seekFurtherInfoPage.verifyCheckYourAnswersSeekFurtherInfo();
  // await seekFurtherInfoPage.verifySuccessfulAlertMessage();
  // await caseViewPage.navigateToTab('Correspondence');
  // await correspondenceTabPage.verifyCorrespondenceTabOptions();
  });
