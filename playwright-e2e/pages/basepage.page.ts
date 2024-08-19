import { type Locator, type Page } from '@playwright/test';

export default class BasePage {
  readonly page: Page;
  readonly continueButton: Locator;
  readonly saveAndContinue: Locator;

  constructor(page: Page) {
    this.page = page;
    this.continueButton = page.getByRole('button', { name: 'Continue' });
    this.saveAndContinue = page.getByRole('button', { name: 'Save and continue' });
  }

  async clickContinue(): Promise<void> {
    await this.continueButton.click();
  }

  async clickSaveAndContinue(): Promise<void> {
    await this.saveAndContinue.click();
  }
}
