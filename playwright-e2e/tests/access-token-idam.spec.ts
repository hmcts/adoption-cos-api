
import { test } from '@playwright/test';
import { getAccessToken } from '../helpers/idam-test-api-helper';
import { laPortalCase } from '../helpers/la_portal_case';
import { createCase } from '../../src/e2e/helpers/create_cui_case';

test('check access token', async ({ page }) => {
    await getAccessToken;
    console.log(getAccessToken);
    await createCase();
    
});