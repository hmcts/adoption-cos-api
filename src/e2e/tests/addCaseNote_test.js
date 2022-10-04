const config = require('../config');

const laHelper = require('../helpers/la_portal_case');
const loginPage = require('../pages/login.page');
const caseListPage = require('../pages/caseList.page');


Feature('Add Case Note').retry(1);

Scenario('Verify Add Case Note Event', async () => {
  console.log('Add Case Note Test');
  // const caseID=await laHelper.createCompleteCase();
  const caseID = "1664-8126-3174-1488";

  console.log('CCD Case number - '+ caseID);
  await loginPage.loginToExUI(config.caseWorkerUserOne);
  await caseListPage.searchForCasesWithHypernisedId(caseID);
  await caseListPage.seeCaseInSearchResult(caseID);
});
