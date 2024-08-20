import AxeBuilder from '@axe-core/playwright';
import { test as base } from '@playwright/test';
import * as dotenv from 'dotenv';
import { expect } from '@playwright/test';
import { runAccessibilityScan } from '../helpers/accessibilityHelper';
import { setupUser, teardownUser } from '../hooks/createDeleteUser.hook';
import App from '../pages/app.page';

dotenv.config();

const test = base.extend<{ makeAxeBuilder: () => AxeBuilder }>({
  makeAxeBuilder: async ({ page }, use) => {
    await use(() => new AxeBuilder({ page }));
  },
});

test.describe('smoke test', () => {
  let userEmail: string;
  let userPassword: string;
  let userId: string;

  test.beforeEach(async () => {
    const userInfo = await setupUser();
    if (userInfo) {
      userEmail = userInfo.email;
      userPassword = userInfo.password;
      userId = userInfo.id;
    }
  });

  test.afterEach('Status check', async () => {
    await teardownUser(userId);
  });

  const smokeTestTags = { tag: ['@smoke', '@caseworker', '@accessibility'] };

  test(
    'smoke test: sign in',
    smokeTestTags,
    async ({ page, makeAxeBuilder }, testInfo) => {
      const app = new App(page);
      await app.signIn.signIn(userEmail, userPassword);
      await expect(app.basePage.continueButton).toBeVisible();
    }
  );
});
