
import { test } from '@playwright/test';
import { getAccessToken } from '../helpers/idam-test-api-helper';

test('check access token', async ({ page }) => {
    await getAccessToken;
    console.log(getAccessToken);
    const caseID=await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseID);
    await this.signIn(config.caseWorkerUserOne);
    await caseListPage.searchForCasesWithHypernisedId(caseID);
    await caseListPage.seeCaseInSearchResult(caseID);
});