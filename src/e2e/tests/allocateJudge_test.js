const config = require('../config');

const laHelper = require('../helpers/la_portal_case');


Feature('Allocate Judge tests').retry(0);

Scenario('Allocate Judge to a case', async ({loginPage, caseListPage, caseViewPage, allocateJudgePage }) => {
  console.log('Smoke test triggered now');
  const caseID=await laHelper.createCompleteCase();
  console.log('CCD Case number - '+ caseID);
  await loginPage.loginToExUI(config.caseWorkerUserOne);
  await caseListPage.searchForCasesWithHypernisedId(caseID);
  await caseListPage.seeCaseInSearchResult(caseID);
  await caseListPage.clickOnCaseId(caseID);
  await caseViewPage.goToNewActions(config.administrationActions.allocateJudge);
  await allocateJudgePage.verifyPageDetails();
  await allocateJudgePage.verifyAllocateJudgePageFunctionality();
});
