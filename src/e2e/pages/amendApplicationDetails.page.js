const config = require('../config');
const { I } = inject();
const postcodeLookup = require('../fragments/postcodeLookup.js');
const amendApplicantDetails = require('../fixtures/amendApplicantDetails.js');
module.exports = {
  fields: {
    isApplicantRepresentedBySolicitorYes: '#isApplicantRepresentedBySolicitor_Yes',
    solicitorFirm: '#solicitorSolicitorFirm',
    solicitorRef: '#solicitorSolicitorRef',
    solicitorEmail: '#solicitorEmail',
    solicitorPhoneNumber: '#solicitorPhoneNumber',

  },

  async addApplicantsSolicitorDetails() {
    await I.retry(3).click(this.fields.isApplicantRepresentedBySolicitorYes);
    await I.retry(3).fillField(this.fields.solicitorFirm, amendApplicantDetails.solicitorName);
    await I.retry(3).fillField(this.fields.solicitorRef, amendApplicantDetails.solicitorReferenceNumber);
    await postcodeLookup.lookupPostcode(amendApplicantDetails.address);
    await I.retry(3).fillField(this.fields.solicitorEmail, amendApplicantDetails.email);
    await I.retry(3).fillField(this.fields.solicitorPhoneNumber, amendApplicantDetails.phoneNumber);
    await I.wait(5);
  },

  async applicantsSolicitorDetailsCYA() {
    await I.waitForText('Check the information below carefully.',30);
    await I.see(amendApplicantDetails.solicitorName);
    await I.see(amendApplicantDetails.solicitorName);
    await I.see(amendApplicantDetails.solicitorName);
    await I.see(amendApplicantDetails.solicitorName);

  },

};
