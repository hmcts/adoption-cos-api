import { faker } from '@faker-js/faker';
import { type Page } from '@playwright/test';
import BasePage from '../pages/basepage.page';
import SignIn from './signIn.page';


export default class App {
  readonly page: Page;
  readonly basePage: BasePage;
  readonly signIn: SignIn;

  constructor(page: Page) {
    this.page = page;
    this.basePage = new BasePage(page);
    this.signIn = new SignIn(page);

  }

}
