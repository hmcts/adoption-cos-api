import { type Locator, type Page } from '@playwright/test';

export default class SignIn {
  readonly heading: Locator;
  readonly heading2: Locator;
  readonly email: Locator;
  readonly password: Locator;
  readonly signinButton: Locator;

  constructor(page: Page) {
    this.heading = page.getByRole('heading', { name: 'Sign in or create an account' });
    this.heading2 = page.getByRole('heading', { name: 'Sign in', exact: true });
    this.email = page.getByLabel('Email address');
    this.password = page.getByText('Password', { exact: true });
    this.signinButton = page.getByRole('button', { name: 'Sign in' });
    page.goto(
      'https://idam-web-public.aat.platform.hmcts.net/login?client_id=adoption-cos-api&response_type=code&redirect_uri=https://adoption-cos-api.aat.platform.hmcts.net/receiver'
    );
  }

  async signIn(email: string, password: string): Promise<void> {
    await this.email.fill(email);
    await this.password.fill(password);
    await this.signinButton.click();
  }
}
