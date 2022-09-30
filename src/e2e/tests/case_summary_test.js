const config = require('../config');

const laHelper = require('../helpers/la_portal_case');

Feature('case Details Summary tab tests').retry(0);

Scenario('verify details of sibling and other placement orders are displayed in the summary page', async ({loginPage, caseListPage, caseDetailsSummaryPage, caseDetailsPage}) => {
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
  await caseDetailsPage.clickOnSummaryPage();
  await caseDetailsSummaryPage.verifyPlacementTableOptions();
  await caseDetailsSummaryPage.verifyLinkedCasesTableOptions();
  await caseDetailsSummaryPage.verifySiblingCourtCaseTableOptions();
});
