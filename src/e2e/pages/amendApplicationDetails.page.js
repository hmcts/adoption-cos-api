const config = require('../config');
const { I } = inject();
module.exports = {
  fields: {
    isApplicantRepresentedBySolicitorYes: '#isApplicantRepresentedBySolicitor_Yes',
    solicitorFirm: '#solicitorFirm',
    solicitorRef: '#solicitorRef',
    solicitorFirm: '#solicitorFirm',
    email: '#email',
    phoneNumber: '#phoneNumber',

  },

  async applicantsSolicitorDetails() {
    await I.retry(3).click(this.fields.isApplicantRepresentedBySolicitorYes);

    await I.retry(3).goToPage(baseUrl);
    await I.retry(3).waitForSelector(this.fields.username);
    await I.retry(3).fillField(this.fields.username, user.email);
    await I.retry(3).fillField(this.fields.password, user.password);
    await I.retry(3).waitForSelector(this.submitButton);
    await I.wait(5);
  },



};
