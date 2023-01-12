const config = require('../config');
const laHelper = require('../helpers/la_portal_case');
let caseId;

Feature('Manage documents tests').retry(0);
async function setupScenario(I) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
    await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}

/**
* Scenario 1: Manage documents EVENT | Page 1: Manage documents.
* To check on Validation errors
* To check on Cancel
*/
Scenario('Verify manage documents', async ({I, caseViewPage, manageDocumentsPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageDocuments);
  await manageDocumentsPage.verifyManageDocuments();
  await manageDocumentsPage.verifyCancelLink();
});

/**
* Scenario 2: Manage documents EVENT | Page 2: Who submitted the document?.
* Page 1:
* To choose and upload a document
* To enter the Document description
* To make selection on Document category Radio Group and Submit
* Page 2:
* To check on Validation errors
*/
Scenario('Verify who submitted the document', async ({I, caseViewPage, manageDocumentsPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageDocuments);
  await manageDocumentsPage.fulfillManageDocuments();
  await manageDocumentsPage.verifyWhoSubmittedTheDocument();
});

/**
* Scenario 3: Manage documents EVENT | Page 3: CYA
* Page 2:
* To make selection on Parties applicable Radio Group and Submit
* Page 3:
* To validate page data and submit
*/
Scenario('Verify CYA and Submit', async ({I, caseViewPage, manageDocumentsPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.manageDocuments);
  await manageDocumentsPage.fulfillManageDocuments();
  await manageDocumentsPage.fulfillWhoSubmittedTheDocument();
  await manageDocumentsPage.verifyAndFulfillCYA();
  // Currently the dynamic radio button selection fails to persist when navigation too and fro from CYA to Manage Documents
  //  await manageDocumentsPage.navigationCheckCYA();
  await manageDocumentsPage.verifySuccessfulAlertMessage();
});
