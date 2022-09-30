const config = require('../config');

const laHelper = require('../helpers/la_portal_case');


Feature('Case Details Other parties tab tests').retry(1);

Scenario('verify respondent details in the other parties page in ExUI', async ({loginPage, caseListPage, caseDetailsPage, caseDetailsOtherPartiesPage }) => {
  console.log('APP_INSIGHTS_KEY - '+ process.env.APP_INSIGHTS_KEY);
  const caseID=await laHelper.createCompleteCase();
  console.log('CCD Case number - '+ caseID);
  const hyphanisedCaseId = caseID.replace(/(.{4})/g,"$1-").substring(0,19);
  console.log('hyphanisedCaseId '+ hyphanisedCaseId);
  console.log('CCD Case number - '+ hyphanisedCaseId);
  await loginPage.loginToExUI(config.caseWorkerUserOne);
  await caseListPage.searchForCasesWithId(hyphanisedCaseId);
  await caseListPage.seeCaseInSearchResult(caseID);
  await caseListPage.clickOnCaseId(caseID);
  await caseDetailsPage.clickOnOtherPartiesTab();
  await caseDetailsOtherPartiesPage.verifyBirthMotherAndFatherDetails();
});
