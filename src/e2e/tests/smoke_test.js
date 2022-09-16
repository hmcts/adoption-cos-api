const config = require('../config');

const laHelper = require('../helpers/la_portal_case');


Feature('Create case @smoke-tests').retry(1);

Scenario('Verify case created and able to search in ExUI', async ({loginPage, caseListPage }) => {
  console.log('APP_INSIGHTS_KEY - '+ process.env.APP_INSIGHTS_KEY);
  console.log('Smoke test triggered now');
  const caseID=await laHelper.createCompleteCase();
  console.log('CCD Case number - '+ caseID);
  const hyphanisedCaseId = caseID.replace(/(.{4})/g,"$1-").substring(0,19);
  console.log('hyphanisedCaseId '+ hyphanisedCaseId);
  console.log('CCD Case number - '+ hyphanisedCaseId);
  await loginPage.loginToExUI(config.caseWorkerUserOne);
  await caseListPage.searchForCasesWithId(hyphanisedCaseId);
  await caseListPage.seeCaseInSearchResult(caseID);
});
