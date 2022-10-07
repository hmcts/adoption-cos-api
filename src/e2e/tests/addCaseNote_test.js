const config = require('../config');

const laHelper = require('../helpers/la_portal_case');
// const loginPage = require('../pages/login.page');
// const caseListPage = require('../pages/caseList.page');
// const caseViewPage = require('../pages/caseView.page');
// const addACaseNotePage = require('../pages/addACaseNote.page');

// {I, loginPage,caseListPage,caseViewPage,addACaseNotePage }
let caseId;
Feature('Add A Case Note').retry(1);

async function setupScenario(I) {
  if (!caseId) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
  }
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}

Scenario('Verify Add A Case Note Event', async ({ I,caseViewPage,addACaseNotePage }) => {
  console.log('Add A Case Note Test *************');
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.addACaseNote);
  await addACaseNotePage.addACaseNote();
  await caseViewPage.verifySuccessBanner(caseId, config.administrationActions.addACaseNote);
  await caseViewPage.navigateToTab(config.tabs.notesTab);
  const tableTitleNoteRowNamesArray = [addACaseNotePage.fields.tableTitleName,addACaseNotePage.fields.tableCaseNoteRowName];
  const tableTitleSubjectRowNamesArray = [addACaseNotePage.fields.tableTitleName,addACaseNotePage.fields.tableCaseNoteSubjectName];
  await caseViewPage.verifyTableDataInsideTab(tableTitleNoteRowNamesArray,addACaseNotePage.fields.noteText);
  await caseViewPage.verifyTableDataInsideTab(tableTitleSubjectRowNamesArray, addACaseNotePage.fields.subjectText)

});
