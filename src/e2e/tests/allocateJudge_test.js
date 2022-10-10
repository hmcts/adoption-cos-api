const config = require('../config');
const laHelper = require('../helpers/la_portal_case');
let caseId;

Feature('Allocate Judge tests').retry(0);

async function setupScenario(I) {
  if (!caseId) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
  }
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}

Scenario('Allocate Judge to a case', async ({I,loginPage, caseListPage, caseViewPage, allocateJudgePage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.allocateJudge);
  await allocateJudgePage.verifyPageDetails();
  await allocateJudgePage.verifyAllocateJudgePageFunctionality();
});
