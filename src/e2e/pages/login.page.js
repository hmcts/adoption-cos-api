const config = require('../config');
const { I } = inject();
const baseUrl = config.baseUrl;
module.exports = {
  fields: {
      username: '#username',
      password: '#password',
      submitButton: 'input[value="Sign in"]',
    },

  async loginToExUI(user) {
    console.log('login page signIn');
    await I.retry(3).goToPage(baseUrl);
    await I.retry(3).waitForSelector(this.fields.username);
    await I.retry(3).fillField(this.fields.username, user.email);
    await I.retry(3).fillField(this.fields.password, user.password);
    await I.retry(3).waitForSelector(this.submitButton);
    await I.retry(3).click(this.fields.submitButton);
    await I.wait(5);
  },

};
