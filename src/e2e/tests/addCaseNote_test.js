const config = require('../config');

const laHelper = require('../helpers/la_portal_case');
const loginPage = require('../pages/login.page');
const caseListPage = require('../pages/caseList.page');
const caseViewPage = require('../pages/caseView.page');
const addACaseNotePage = require('../pages/addACaseNote.page');

// {I, loginPage,caseListPage,caseViewPage,addACaseNotePage }

Feature('Add A Case Note').retry(1);

Scenario('Verify Add A Case Note Event', async () => {
  console.log('Add A Case Note Test *************');
  // const caseID = await createCompleteCase();
  const caseID = '1665072458172031';

  console.log('CCD Case number - '+ caseID);
  await loginPage.loginToExUI(config.caseWorkerUserOne);
  await caseListPage.searchForCasesWithHypernisedId(caseID);
  await caseListPage.seeCaseInSearchResult(caseID);
  await caseListPage.clickOnCaseId(caseID);
  await caseViewPage.goToNewActions(config.administrationActions.addACaseNote);
  await addACaseNotePage.addACaseNote();
  await caseViewPage.verifySuccessBanner(caseID, config.administrationActions.addACaseNote);
  await caseViewPage.navigateToTab(config.tabs.notesTab);
  const tableTitleNoteRowNamesArray = [addACaseNotePage.fields.tableTitleName,addACaseNotePage.fields.tableCaseNoteRowName];
  const tableTitleSubjectRowNamesArray = [addACaseNotePage.fields.tableTitleName,addACaseNotePage.fields.tableCaseNoteSubjectName];
  await caseViewPage.verifyTableDataInsideTab(tableTitleNoteRowNamesArray,addACaseNotePage.fields.noteText);
  await caseViewPage.verifyTableDataInsideTab(tableTitleSubjectRowNamesArray, addACaseNotePage.fields.subjectText)

});
