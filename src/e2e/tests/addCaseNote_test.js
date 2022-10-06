const config = require('../config');

const laHelper = require('../helpers/la_portal_case');
const loginPage = require('../pages/login.page');
const caseListPage = require('../pages/caseList.page');
const caseViewPage = require('../pages/caseView.page');
const addACaseNotePage = require('../pages/addACaseNote.page');



Feature('Add A Case Note').retry(1);

Scenario('Verify Add A Case Note Event', async () => {
  console.log('Add A Case Note Test');
  // const caseID=await laHelper.createCompleteCase();
  const caseID = "1665072458172031";

  console.log('CCD Case number - '+ caseID);
  await loginPage.loginToExUI(config.caseWorkerUserOne);
  await caseListPage.searchForCasesWithHypernisedId(caseID);
  await caseListPage.seeCaseInSearchResult(caseID);
  await caseListPage.clickOnCaseId(caseID);
  await caseViewPage.goToNewActions(config.administrationActions.addACaseNote);
  await addACaseNotePage.addACaseNote();
  await caseViewPage.goToTab('#mat-tab-label-1-6');

});
