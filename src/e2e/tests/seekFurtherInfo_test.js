const config = require('../config');

const laHelper = require('../helpers/la_portal_case');
const manageOrderDetails = require('../fixtures/seekFurtherInfoDetails.js');

let caseId;

Feature('Seek Further Information tests').retry(0);

async function setupScenario(I) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
    await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}

//Common
Scenario('Verify seek further Info Who do you need to contact details Cancel Link', async ({I, caseViewPage, seekFurtherInfoPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
  await seekFurtherInfoPage.verifyWhoDoYouNeedToContact();
  await seekFurtherInfoPage.verifyCancelLink();
});

//Screen 1 Who do you need to contact
Scenario('Verify seek further Info Who do you need to contact Submit', async ({I, caseViewPage, seekFurtherInfoPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
  await seekFurtherInfoPage.fulfillWhoDoYouNeedToContact();
});

//Screen 2 What information do you need?
Scenario('Verify seek further Info What information do you need Verify', async ({I, caseViewPage, seekFurtherInfoPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
  await seekFurtherInfoPage.fulfillWhoDoYouNeedToContact();
  await seekFurtherInfoPage.verifyWhatInformationDoYouNeed();
});

Scenario('Verify seek further Info What information do you need Submit', async ({I, caseViewPage, seekFurtherInfoPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
  await seekFurtherInfoPage.fulfillWhoDoYouNeedToContact();
  await seekFurtherInfoPage.fulfillWhatInformationDoYouNeed();
});

//Screen 3 When is the information needed by?
Scenario('Verify seek further Info When is the information needed by Verify', async ({I, caseViewPage, seekFurtherInfoPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
  await seekFurtherInfoPage.fulfillWhoDoYouNeedToContact();
  await seekFurtherInfoPage.fulfillWhatInformationDoYouNeed();
  await seekFurtherInfoPage.verifyWhenIsTheInformationNeededBy();
});

Scenario('Verify seek further Info When is the information needed by Submit', async ({I, caseViewPage, seekFurtherInfoPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
  await seekFurtherInfoPage.fulfillWhoDoYouNeedToContact();
  await seekFurtherInfoPage.fulfillWhatInformationDoYouNeed();
  await seekFurtherInfoPage.fulfillWhenIsTheInformationNeededBy();
});

//Screen 4 CYA
Scenario('Verify seek further Info CYA Verify', async ({I, caseViewPage, seekFurtherInfoPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.seekFurtherInfo);
  await seekFurtherInfoPage.fulfillWhoDoYouNeedToContact();
  await seekFurtherInfoPage.fulfillWhatInformationDoYouNeed();
  await seekFurtherInfoPage.fulfillWhenIsTheInformationNeededBy();
  await seekFurtherInfoPage.navigationCheckCYA();
});
