const config = require('../config');

const laHelper = require('../helpers/la_portal_case');


Feature('Create case @smoke').retry(1);

Scenario('Verify case created and able to search in ExUI', async ({I,loginPage, caseListPage }) => {

  console.log('Smoke test triggered now');
  const caseID=await laHelper.createCompleteCase();
  const hyphanisedCaseId = caseID.replace(/(.{4})/g,"$1-").substring(0,19);
  console.log('hyphanisedCaseId '+ hyphanisedCaseId);
  console.log('CCD Case number - '+ hyphanisedCaseId);
  await loginPage.loginToExUI(config.caseWorkerUserOne);
  await caseListPage.searchForCasesWithId(hyphanisedCaseId);
  await caseListPage.seeCaseInSearchResult(caseID);
});
