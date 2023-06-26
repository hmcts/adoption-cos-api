const config = require('../config');

const laHelper = require('../helpers/la_portal_case');
const manageOrderDetails = require('../fixtures/seekFurtherInfoDetails.js');

let caseId;

Feature('Seek Further Information tests').retry(1);

async function setupScenario(I) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
    await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}

/**
* Scenario 1: Seek Further Information EVENT | Page 1: Who do you need to contact.
* To validate the navigation from Login to Event first page
* To validate the elements presence in scoped page
* To validate the navigation of Cancel link
*
* @param I - CodeceptJS Inject
* @param caseViewPage - Initial CaseView Page to start with
* @param seekFurtherInfoPage - Scenario scoped Page to do validation
*/
Scenario('Verify seek further Info Who do you need to contact details Cancel Link', async ({I, caseViewPage, seekFurtherInfoPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
  await seekFurtherInfoPage.verifyWhoDoYouNeedToContact();
  await seekFurtherInfoPage.verifyCancelLink();
});

/**
* Scenario 2: Seek Further Information EVENT | Page 1: Who do you need to contact.
* To check on Validation errors
* To make selection on Radio Group and Submit
*/
Scenario('Verify seek further Info Who do you need to contact Submit', async ({I, caseViewPage, seekFurtherInfoPage }) => {
   await setupScenario(I);
   await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
   await seekFurtherInfoPage.fulfillWhoDoYouNeedToContact();
});

/**
* Scenario 3: Seek Further Information EVENT | Page 2: What information do you need?.
* To validate the elements presence in scoped page
*/
Scenario('Verify seek further Info What information do you need Verify', async ({I, caseViewPage, seekFurtherInfoPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
  await seekFurtherInfoPage.fulfillWhoDoYouNeedToContact();
  await seekFurtherInfoPage.verifyWhatInformationDoYouNeed();
});

/**
* Scenario 4: Seek Further Information EVENT | Page 2: What information do you need?.
* To check on Validation errors
* To make selection on Checkbox Group
* To enter configured Text to Text box and Submit
*/
Scenario('Verify seek further Info What information do you need Submit', async ({I, caseViewPage, seekFurtherInfoPage }) => {
   await setupScenario(I);
   await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
   await seekFurtherInfoPage.fulfillWhoDoYouNeedToContact();
   await seekFurtherInfoPage.fulfillWhatInformationDoYouNeed();
});

/**
* Scenario 5: Seek Further Information EVENT | Page 3: When is the information needed by?.
* To validate the elements presence in scoped page
*/
Scenario('Verify seek further Info When is the information needed by Verify', async ({I, caseViewPage, seekFurtherInfoPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
  await seekFurtherInfoPage.fulfillWhoDoYouNeedToContact();
  await seekFurtherInfoPage.fulfillWhatInformationDoYouNeed();
  await seekFurtherInfoPage.verifyWhenIsTheInformationNeededBy();
});

/**
* Scenario 6: Seek Further Information EVENT | Page 3: When is the information needed by?.
* To check on Validation errors
* To enter configured Text to Text box and Submit
*/
Scenario('Verify seek further Info When is the information needed by Submit', async ({I, caseViewPage, seekFurtherInfoPage }) => {
   await setupScenario(I);
   await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
   await seekFurtherInfoPage.fulfillWhoDoYouNeedToContact();
   await seekFurtherInfoPage.fulfillWhatInformationDoYouNeed();
   await seekFurtherInfoPage.fulfillWhenIsTheInformationNeededBy();
});

/**
* Scenario 7: Seek Further Information EVENT | Page 4: CYA.
* To check on event user journey navigation
* To validate the elements presence in scoped page
*/
Scenario('Verify seek further Info CYA Verify', async ({I, caseViewPage, seekFurtherInfoPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
  await seekFurtherInfoPage.fulfillWhoDoYouNeedToContact();
  await seekFurtherInfoPage.fulfillWhatInformationDoYouNeed();
  await seekFurtherInfoPage.fulfillWhenIsTheInformationNeededBy();
  await seekFurtherInfoPage.navigationCheckCYA();
});

Screen 5 CYA
Scenario('Verify seek further Info Check your Answers of seek Further Info', async ({I, caseViewPage, seekFurtherInfoPage }) => {
   await setupScenario(I);
   await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
   await seekFurtherInfoPage.fulfillWhoDoYouNeedToContact();
   await seekFurtherInfoPage.fulfillWhatInformationDoYouNeed();
   await seekFurtherInfoPage.fulfillWhenIsTheInformationNeededBy();
   await seekFurtherInfoPage.verifyPreviewAndCheckTheLetter();
   await seekFurtherInfoPage.verifyCheckYourAnswersSeekFurtherInfo();
   await seekFurtherInfoPage.verifySuccessfulAlertMessage();
  });

