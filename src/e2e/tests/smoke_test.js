const config = require('../config');

const laHelper = require('../helpers/la_portal_case');


Feature('Create case @smoke-tests').retry(1);

Scenario('Verify case created and able to search in ExUI', async ({I,loginPage, caseListPage }) => {
  console.log('Smoke test triggered now');
  const caseID=await laHelper.createCompleteCase();
  console.log('CCD Case number - '+ caseID);
  await I.signIn(config.caseWorkerUserOne);
  await caseListPage.searchForCasesWithHypernisedId(caseID);
  await caseListPage.seeCaseInSearchResult(caseID);
});
