const config = require('../config');

const laHelper = require('../helpers/la_portal_case');


Feature('Create case @functional @fullFunctional @smoke-tests').retry(1);

Scenario('Verify case created and able to search in ExUI', async ({I, caseListPage}) => {
  console.log('Search case test triggered now');
  await I.signIn(config.caseWorkerUserOne);
  await caseListPage.searchForCasesWithHypernisedId();
  await caseListPage.seeCaseInSearchResult();
  await caseListPage.seeExpectedTabsOnTheCase();
});
