const config = require('../config');


Feature('Smoke tests').retry(1);

Scenario('Verify applicant eligible to adopt', async ({loginPage, caseListPage }) => {
  console.log('Smoke test triggered now');
  await loginPage.loginToExUI(config.caseWorkerUserOne);
  await caseListPage.verifyCaseIsNotAccessible('1662-5602-0099-6610');
});
