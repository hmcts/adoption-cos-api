const {I} = inject();
const config = require('../config');
const postcodeLookup = require('../fragments/postcodeLookup.js');
const amendOtherPartiesDetails = require('../fixtures/amendOtherPartiesDetails.js');

module.exports = {

  fields: {
     FindAddressButton: 'Find address',
     legalGuardianYes: '#isChildRepresentedByGuardian_Yes',
     legalGuardianName: '#localGuardianName',
     legalGuardianPhoneNumber: '#localGuardianPhoneNumber',
     legalGuardianEmail: '#localGuardianEmail',
     legalGuardianAddressBox: '#localGuardianGuardianAddress_localGuardianGuardianAddress_postcodeInput',
     legalGuardianAddressList: '#localGuardianGuardianAddress_localGuardianGuardianAddress_addressList',

     legalGuardianSolicitorYes: '#isChildRepresentedBySolicitor_Yes',
     childSolicitor: '#childSolicitorSolicitorFirm',
     childSolicitorReferenceNumber: '#childSolicitorSolicitorRef',
     childSolicitorPhoneNumber: '#childSolicitorPhoneNumber',
     childSolicitorEmail: '#childSolicitorEmail',
     childLACountry: '#childSocialWorkerCountry',
     ApplicantLACountry: '#applicantSocialWorkerCountry',
     childSolicitorPostCodeBox: '#childSolicitorSolicitorAddress_childSolicitorSolicitorAddress_postcodeInput',
     childSolicitorAddressList: '#childSolicitorSolicitorAddress_childSolicitorSolicitorAddress_addressList',
     childSolicitorFindAddress: '//*[@id="childSolicitorSolicitorAddress_childSolicitorSolicitorAddress_postcodeLookup"]//button[contains(text(),"Find address")]',

    adoptionAgencyCountry: '#adopAgencyCountry',
    otherAdoptionAgencyYes: '#hasAnotherAdopAgencyOrLAinXui_Yes',
    otherAdoptionAgencyName: '#otherAdoptionAgencyOrLaName',
    otherAdoptionAgencyContact: '#otherAdoptionAgencyOrLaContactName',
    otherAdoptionAgencyPostCodeTextBox: '#otherAdoptionAgencyAddress_otherAdoptionAgencyAddress_postcodeInput',
    otherAdoptionAgencyFindAddress: '//*[@id="otherAdoptionAgencyAddress_otherAdoptionAgencyAddress_postcodeLookup"]//button[contains(text(),"Find address")]',
    otherAdoptionAgencyAddressList: '#otherAdoptionAgencyAddress_otherAdoptionAgencyAddress_addressList',
    otherAdoptionAgencyPhone: '#otherAdoptionAgencyOrLaPhoneNumber',
    otherAdoptionAgencyEmail: '#otherAdoptionAgencyOrLaContactEmail',

   birthMotherCountry: '#birthMotherAddressCountry',
   birthMotherDateOfAddress: '#birthMotherLastAddressDate-day',
   birthMotherMonthOfAddress: '#birthMotherLastAddressDate-month',
   birthMotherYearOfAddress: '#birthMotherLastAddressDate-year',
   birthMotherSolicitorYes: '#isBirthMotherRepresentedBySolicitor_Yes',
   birthMotherSolicitorName: '//input[@id="motherSolicitorSolicitorFirm"]',
   birthMotherSolicitorRef: '#motherSolicitorSolicitorRef',
   birthMotherPostcodeInputBox: '#motherSolicitorSolicitorAddress_motherSolicitorSolicitorAddress_postcodeInput',
   birthMOtherFindAddressButton: '//*[@id="motherSolicitorSolicitorAddress_motherSolicitorSolicitorAddress_postcodeLookup"]//button[contains(text(),"Find address")]',
   birthMotherListOfAddresses: '#motherSolicitorSolicitorAddress_motherSolicitorSolicitorAddress_addressList',
   birthMotherSolicitorPhone: '#motherSolicitorPhoneNumber',
   birthMotherSolicitorEmail: '#motherSolicitorEmail',

  birthFatherCountry: '#birthFatherAddressCountry',
  birthFatherDateOfAddress: '#birthFatherLastAddressDate-day',
  birthFatherMonthOfAddress: '#birthFatherLastAddressDate-month',
  birthFatherYearOfAddress: '#birthFatherLastAddressDate-year',
  birthFatherSolicitorYes: '#isBirthFatherRepresentedBySolicitor_Yes',
  birthFatherSolicitorName: '//input[@id="fatherSolicitorSolicitorFirm"]',
  birthFatherSolicitorRef: '#fatherSolicitorSolicitorRef',
  birthFatherPostcodeInputBox: '#fatherSolicitorSolicitorAddress_fatherSolicitorSolicitorAddress_postcodeInput',
  birthFatherFindAddressButton: '//*[@id="fatherSolicitorSolicitorAddress_fatherSolicitorSolicitorAddress_postcodeLookup"]//button[contains(text(),"Find address")]',
  birthFatherListOfAddresses: '#fatherSolicitorSolicitorAddress_fatherSolicitorSolicitorAddress_addressList',
  birthFatherSolicitorPhone: '#fatherSolicitorPhoneNumber',
  birthFatherSolicitorEmail: '#fatherSolicitorEmail',

  otherParentalRespYes: '//input[@id="isThereAnyOtherPersonWithParentalResponsibility_Yes"]',
  otherParentalRelationToChild: '//input[@id="otherParentRelationShipWithChild"]',
  otherParentalRespDateOfAddress: '#otherParentLastAddressDate-day',
  otherParentalRespMonthOfAddress: '#otherParentLastAddressDate-month',
  otherParentalRespYearOfAddress: '#otherParentLastAddressDate-year',
  otherParentalRespPostcodeInputBox: '#otherParentSolicitorSolicitorAddress_otherParentSolicitorSolicitorAddress_postcodeInput',
  otherParentalRespFindAddressButton: '//*[@id="otherParentSolicitorSolicitorAddress_otherParentSolicitorSolicitorAddress_postcodeLookup"]//button[contains(text(),"Find address")]',
  otherParentalRespListOfAddresses: '#otherParentSolicitorSolicitorAddress_otherParentSolicitorSolicitorAddress_addressList',
  otherParentalRespSolicitorYes: '//input[@id="isOtherParentRepresentedBySolicitor_Yes"]',
  otherParentalRespSolicitorName: '#otherParentSolicitorSolicitorFirm',
  otherParentalRespSolicitorRef: '#otherParentSolicitorSolicitorRef',
  otherParentalRespSolicitorPhone: '#otherParentSolicitorPhoneNumber',
  otherParentalRespSolicitorEmail: '#otherParentSolicitorEmail',
  amendOtherPartiesLink: '//dl[@id="labelSummary-otherParties"]//a',
  continueButton: 'button[type="submit"]',
  alertMessage: '//div[@class="alert-message"]',

  amendOtherPartyDetailsHeading: '//h1[contains(text(),"Amend other parties details")]',
  childDetailsHeading: '//h3[contains(text(),"Child Details")]',
  legalGuardianDetailsHeading: '//h3[contains(text(),"Legal guardian (CAFCASS)")]',
  legalGuardianSolicitorDetailsHeading: '//h3[contains(text(),"Solicitor")]',
  AgencyLADetailsHeading: '//h3[contains(text(),"Agencies/Local authorities details")]',
  adoptionAgencyDetailsHeading: '//h3[contains(text(),"Adoption agency")]',
  otherAdoptionAgencyDetailsHeading: '//h3[contains(text(),"Other adoption agency")]',
  childLAHeading: '//h3[contains(text(),"Child\'s local authority")]',
  applicantLAHeading: '//h3[contains(text(),"Applicant\'s local authority")]',
  respondentDetailsHeading: '//h3[contains(text(),"Respondent details")]',
  birthMotherDetailsHeading: '//h3[contains(text(),"Birth mother")]',
  birthMotherSolicitorDetailsHeading: '//dl[@id="birthMotherSolicitorLab1"]//h3[contains(text(),"Solicitor")]',
  birthFatherDetailsHeading: '//h3[contains(text(),"Birth Father")]',
  birthFatherSolicitorDetailsHeading: '//dl[@id="birthFatherSolicitorLab1"]//h3[contains(text(),"Solicitor")]',
  otherPersonParentalResponsibilityHeading: '//h3[contains(text(),"Other person with parental responsibility")]',
  otherPersonParentalResponsibilitySolicitorHeading: '//dl[@id="otherParentSolicitorlabel1"]//h3[contains(text(),"Solicitor")]',
  },

 async addChildLegalGuardianDetails() {
     await I.retry(3).click(this.fields.legalGuardianYes);
     await I.wait(3);
     await I.retry(3).fillField(this.fields.legalGuardianName,amendOtherPartiesDetails.childLegalGuardianDetails.childGuardianName);
     await I.retry(3).fillField(this.fields.legalGuardianAddressBox, amendOtherPartiesDetails.childLegalGuardianDetails.childGuardianPostcode);
     await I.retry(3).click(this.fields.FindAddressButton);
     await I.wait(3);
     await I.retry(3).waitForText('addresses found');
     await I.retry(3).waitForElement(locate(this.fields.legalGuardianAddressList).find('option').withText(amendOtherPartiesDetails.childLegalGuardianDetails.childGuardianAddressLookupOption));
     await I.retry(3).selectOption(this.fields.legalGuardianAddressList, amendOtherPartiesDetails.childLegalGuardianDetails.childGuardianAddressLookupOption);
     await I.retry(3).fillField(this.fields.legalGuardianPhoneNumber, amendOtherPartiesDetails.childLegalGuardianDetails.childGuardianPhoneNumber);
     await I.retry(3).fillField(this.fields.legalGuardianEmail, amendOtherPartiesDetails.childLegalGuardianDetails.childGuardianEmail);
 },
 async addChildLegalGuardianSolicitorDetails() {
     await I.retry(3).click(this.fields.legalGuardianSolicitorYes);
     await I.retry(3).fillField(this.fields.childSolicitor, amendOtherPartiesDetails.childLegalGuardianSolicitorDetails.solicitorName);
     await I.retry(3).fillField(this.fields.childSolicitorReferenceNumber, amendOtherPartiesDetails.childLegalGuardianSolicitorDetails.solicitorReferenceNumber);
     await I.retry(3).fillField(this.fields.childSolicitorPostCodeBox, amendOtherPartiesDetails.childLegalGuardianSolicitorDetails.childSolicitorPostCode);
     await I.retry(3).click(this.fields.childSolicitorFindAddress);
     await I.wait(3);
     await I.retry(3).waitForText('addresses found');
     await I.retry(3).waitForElement(locate(this.fields.childSolicitorAddressList).find('option').withText(amendOtherPartiesDetails.childLegalGuardianSolicitorDetails.childSolicitorPostcodeLookupOption));
     await I.retry(3).selectOption(this.fields.childSolicitorAddressList, amendOtherPartiesDetails.childLegalGuardianSolicitorDetails.childSolicitorPostcodeLookupOption);
     await I.retry(3).fillField(this.fields.childSolicitorEmail, amendOtherPartiesDetails.childLegalGuardianSolicitorDetails.solicitorEmail);
     await I.retry(3).fillField(this.fields.childSolicitorPhoneNumber, amendOtherPartiesDetails.childLegalGuardianSolicitorDetails.solicitorsolicitorPhoneNumber);
     await I.wait(5);
 },
 async addAdditionalAdoptionAgencyDetails() {
    await I.retry(3).click(this.fields.otherAdoptionAgencyYes);
    await I.retry(3).fillField(this.fields.adoptionAgencyCountry,amendOtherPartiesDetails.otherAdoptionAgencyDetails.country);
    await I.retry(3).fillField(this.fields.otherAdoptionAgencyName, amendOtherPartiesDetails.otherAdoptionAgencyDetails.agencyName);
    await I.retry(3).fillField(this.fields.otherAdoptionAgencyContact, amendOtherPartiesDetails.otherAdoptionAgencyDetails.agencyContact);
    await I.retry(3).fillField(this.fields.otherAdoptionAgencyPostCodeTextBox, amendOtherPartiesDetails.childLegalGuardianSolicitorDetails.childSolicitorPostCode);
    await I.retry(3).click(this.fields.otherAdoptionAgencyFindAddress);
    await I.wait(3);
    await I.retry(3).waitForText('addresses found');
    await I.retry(3).waitForElement(locate(this.fields.otherAdoptionAgencyAddressList).find('option').withText(amendOtherPartiesDetails.childLegalGuardianSolicitorDetails.childSolicitorPostcodeLookupOption));
    await I.retry(3).selectOption(this.fields.otherAdoptionAgencyAddressList, amendOtherPartiesDetails.childLegalGuardianSolicitorDetails.childSolicitorPostcodeLookupOption);
    await I.retry(3).fillField(this.fields.otherAdoptionAgencyEmail, amendOtherPartiesDetails.otherAdoptionAgencyDetails.agencyEmail);
    await I.retry(3).fillField(this.fields.otherAdoptionAgencyPhone, amendOtherPartiesDetails.otherAdoptionAgencyDetails.agencyPhoneNumber);
    await I.retry(3).fillField(this.fields.childLACountry, amendOtherPartiesDetails.otherAdoptionAgencyDetails.country);
    await I.retry(3).fillField(this.fields.ApplicantLACountry, amendOtherPartiesDetails.otherAdoptionAgencyDetails.country);
    await I.wait(5);

  },
 async addBirthMotherSolicitorDetails() {
     await I.retry(3).fillField(this.fields.birthMotherCountry, amendOtherPartiesDetails.otherAdoptionAgencyDetails.country);
     await I.retry(3).fillField(this.fields.birthMotherDateOfAddress, amendOtherPartiesDetails.birthMotherSolicitorDetails.dateLastKnown);
     await I.retry(3).fillField(this.fields.birthMotherMonthOfAddress, amendOtherPartiesDetails.birthMotherSolicitorDetails.monthLastKnown);
     await I.retry(3).fillField(this.fields.birthMotherYearOfAddress, amendOtherPartiesDetails.birthMotherSolicitorDetails.yearLastKnown);
     await I.retry(3).click(this.fields.birthMotherSolicitorYes);
     await I.retry(3).fillField(this.fields.birthMotherSolicitorName, amendOtherPartiesDetails.birthMotherSolicitorDetails.solicitorName);
     await I.retry(3).fillField(this.fields.birthMotherSolicitorRef, amendOtherPartiesDetails.birthMotherSolicitorDetails.solicitorReferenceNumber);
     await I.retry(3).fillField(this.fields.birthMotherPostcodeInputBox, amendOtherPartiesDetails.birthMotherSolicitorDetails.motherSolicitorPostCode);
     await I.retry(3).click(this.fields.birthMOtherFindAddressButton);
     await I.wait(3);
     await I.retry(3).waitForText('addresses found');
     await I.retry(3).waitForElement(locate(this.fields.birthMotherListOfAddresses).find('option').withText(amendOtherPartiesDetails.birthMotherSolicitorDetails.motherSolicitorPostcodeLookupOption));
     await I.retry(3).selectOption(this.fields.birthMotherListOfAddresses, amendOtherPartiesDetails.birthMotherSolicitorDetails.motherSolicitorPostcodeLookupOption);
     await I.retry(3).fillField(this.fields.birthMotherSolicitorPhone, amendOtherPartiesDetails.birthMotherSolicitorDetails.solicitorPhoneNumber);
     await I.retry(3).fillField(this.fields.birthMotherSolicitorEmail, amendOtherPartiesDetails.birthMotherSolicitorDetails.solicitorEmail);
     await I.wait(5);
   },

  async addBirthFatherSolicitorDetails() {
   await I.retry(3).fillField(this.fields.birthFatherCountry, amendOtherPartiesDetails.otherAdoptionAgencyDetails.country);
   await I.retry(3).fillField(this.fields.birthFatherDateOfAddress, amendOtherPartiesDetails.birthFatherSolicitorDetails.dateLastKnown);
   await I.retry(3).fillField(this.fields.birthFatherMonthOfAddress, amendOtherPartiesDetails.birthFatherSolicitorDetails.monthLastKnown);
   await I.retry(3).fillField(this.fields.birthFatherYearOfAddress, amendOtherPartiesDetails.birthFatherSolicitorDetails.yearLastKnown);
   await I.retry(3).click(this.fields.birthFatherSolicitorYes);
   await I.wait(5);
   await I.retry(5).fillField(this.fields.birthFatherSolicitorName, amendOtherPartiesDetails.birthFatherSolicitorDetails.solicitorName);
   await I.retry(3).fillField(this.fields.birthFatherSolicitorRef, amendOtherPartiesDetails.birthFatherSolicitorDetails.solicitorReferenceNumber);
   await I.retry(3).fillField(this.fields.birthFatherPostcodeInputBox, amendOtherPartiesDetails.birthFatherSolicitorDetails.fatherSolicitorPostCode);
   await I.retry(3).click(this.fields.birthFatherFindAddressButton);
   await I.wait(3);
   await I.retry(3).waitForText('addresses found');
   await I.retry(3).waitForElement(locate(this.fields.birthFatherListOfAddresses).find('option').withText(amendOtherPartiesDetails.birthFatherSolicitorDetails.fatherSolicitorPostcodeLookupOption));
   await I.retry(3).selectOption(this.fields.birthFatherListOfAddresses, amendOtherPartiesDetails.birthFatherSolicitorDetails.fatherSolicitorPostcodeLookupOption);
   await I.retry(3).fillField(this.fields.birthFatherSolicitorPhone, amendOtherPartiesDetails.birthFatherSolicitorDetails.solicitorPhoneNumber);
   await I.retry(3).fillField(this.fields.birthFatherSolicitorEmail, amendOtherPartiesDetails.birthFatherSolicitorDetails.solicitorEmail);
   await I.wait(5);
 },

 async addOtherParentalRespSolicitorDetails() {
   await I.retry(3).click(this.fields.otherParentalRespYes);
   await I.retry(3).fillField(this.fields.otherParentalRelationToChild, amendOtherPartiesDetails.OtherParentalRespPersonSolicitorDetails.relationToChild);
   await I.retry(3).fillField(this.fields.otherParentalRespDateOfAddress, amendOtherPartiesDetails.OtherParentalRespPersonSolicitorDetails.dateLastKnown);
   await I.retry(3).fillField(this.fields.otherParentalRespMonthOfAddress, amendOtherPartiesDetails.OtherParentalRespPersonSolicitorDetails.monthLastKnown);
   await I.retry(3).fillField(this.fields.otherParentalRespYearOfAddress, amendOtherPartiesDetails.OtherParentalRespPersonSolicitorDetails.yearLastKnown);
   await I.wait(3);
   await I.retry(3).click(this.fields.otherParentalRespSolicitorYes);
   await I.retry(3).fillField(this.fields.otherParentalRespSolicitorName, amendOtherPartiesDetails.OtherParentalRespPersonSolicitorDetails.solicitorName);
   await I.retry(3).fillField(this.fields.otherParentalRespSolicitorRef, amendOtherPartiesDetails.OtherParentalRespPersonSolicitorDetails.solicitorReferenceNumber);
   await I.retry(3).fillField(this.fields.otherParentalRespPostcodeInputBox, amendOtherPartiesDetails.OtherParentalRespPersonSolicitorDetails.otherParentalRespSolicitorPostCode);
   await I.retry(3).click(this.fields.otherParentalRespFindAddressButton);
   await I.wait(3);
   await I.retry(3).waitForText('addresses found');
   await I.retry(3).waitForElement(locate(this.fields.otherParentalRespListOfAddresses).find('option').withText(amendOtherPartiesDetails.OtherParentalRespPersonSolicitorDetails.otherParentalRespSolicitorPostcodeLookupOption));
   await I.retry(3).selectOption(this.fields.otherParentalRespListOfAddresses, amendOtherPartiesDetails.OtherParentalRespPersonSolicitorDetails.otherParentalRespSolicitorPostcodeLookupOption);
   await I.retry(3).fillField(this.fields.otherParentalRespSolicitorPhone, amendOtherPartiesDetails.OtherParentalRespPersonSolicitorDetails.solicitorPhoneNumber);
   await I.retry(3).fillField(this.fields.otherParentalRespSolicitorEmail, amendOtherPartiesDetails.OtherParentalRespPersonSolicitorDetails.solicitorEmail);
   await I.wait(5);
   await I.retry(3).click(this.fields.continueButton);
   await I.wait(5);
 },
  async verifyHeadingsAreDisplayedInCheckYourAnswersPage(){
    await I.wait(5);
    await I.retry(5).seeElement(this.fields.amendOtherPartyDetailsHeading);
    await I.retry(5).seeElement(this.fields.childDetailsHeading);
    await I.retry(5).seeElement(this.fields.legalGuardianDetailsHeading);
    await I.retry(5).seeElement(this.fields.legalGuardianSolicitorDetailsHeading);
    await I.retry(5).seeElement(this.fields.AgencyLADetailsHeading);
    await I.retry(5).seeElement(this.fields.adoptionAgencyDetailsHeading);
    await I.retry(5).seeElement(this.fields.otherAdoptionAgencyDetailsHeading);
    await I.retry(5).seeElement(this.fields.childLAHeading);
    await I.retry(5).seeElement(this.fields.applicantLAHeading);
    await I.retry(5).seeElement(this.fields.respondentDetailsHeading);
    await I.retry(5).seeElement(this.fields.birthMotherDetailsHeading);
    await I.retry(5).seeElement(this.fields.birthMotherSolicitorDetailsHeading);
    await I.retry(5).seeElement(this.fields.birthFatherDetailsHeading);
    await I.retry(5).seeElement(this.fields.birthFatherSolicitorDetailsHeading);
    await I.retry(5).seeElement(this.fields.otherPersonParentalResponsibilityHeading);
    await I.retry(5).seeElement(this.fields.otherPersonParentalResponsibilitySolicitorHeading);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(5);
    await I.retry(5).seeElement(this.fields.alertMessage);
},
 };
