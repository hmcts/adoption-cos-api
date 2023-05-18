const config = require('../config');
const {I} = inject();
const finalAdoptionOrderDetails = require('../fixtures/finalAdoptionOrderDetails.js');
const manageOrderDetails = require("../fixtures/manageOrderDetails");
module.exports = {
  fields: {
    manageOrderTypeErrorMessage: '#manageOrderType .error-message',
    continueButton: 'button[type="submit"]',
    preamble: '#preambleDetailsFinalAdoptionOrder',
    childFirstNameAfterAdoption: '#childrenFirstNameAfterAdoption',
    childLastNameAfterAdoption: '#childrenLastNameAfterAdoption',
    finalAdoptionOrder: '#manageOrderType-finalAdoptionOrder',
    orderedBy: '#orderedByFinalAdoptionOrder',
    previewOrderLink: '//a[contains(text(),"A76_Final adoption order_draft.pdf")]',
    previewOrderLinkA206: '//a[contains(text(),"A206_Final adoption order_draft.pdf")]',
    placeAndDateOfBirth: {
      dateOfBirthProvedYes: '#dateOfBirthProved_Yes',
      placeOfBirthProvidedYes: '#placeOfBirthProved_Yes',
      placeOfBirthProvidedNo: '#placeOfBirthProved_No',
      birthCertificate: '#typeOfCertificate-birthCertificate',
      adoptionCertificate: '#typeOfCertificate-adoptionCertificate',
      countryOfBirthUK: '#countryOfBirthForPlaceOfBirthYes-unitedKingdom',
      countryOfBirthOutsideUK: '#countryOfBirthForPlaceOfBirthYes-outsideTheUK',
      countryOfBirth: '#otherCountryOfOriginForPlaceOfBirthYes',
      birthLocationUK: '#countryOfBirthForPlaceOfBirthNo-unitedKingdom',
      birthLocationOutsideUK: '#countryOfBirthForPlaceOfBirthNo-outsideTheUK',
      birthLocation: '#otherCountryOfOriginForPlaceOfBirthNo',
      timeOfBirthKnown: '#timeOfBirthKnown_Yes',
      timeOfBirthNo: '#timeOfBirthKnown_No',
      timeOfBirth: '#timeOfBirth',
      birthOrAdoptionRegNumber: '#birthAdoptionRegistrationNumber',
      birthOrAdoptionRegDateDay: '#adoptionRegistrationDate-day',
      birthOrAdoptionRegDateMonth: '#adoptionRegistrationDate-month',
      birthOrAdoptionRegDateYear: '#adoptionRegistrationDate-year',
      registrationDistrict: '#registrationDistrict',
      registrationSubDistrict: '#registrationSubDistrict',
      registrationCounty: '#registrationCounty',
      birthProvedError: '#placeOfBirthProved .error-message',
      certificateError: '#typeOfCertificate .error-message',
      timeOfBirthError: '#timeOfBirthKnown .error-message',
      countryOfBirthError: '#countryOfBirthForPlaceOfBirthYes .error-message',
      adoptionRegNumberError: 'ccd-field-write[field_id=\'birthAdoptionRegistrationNumber\'] .error-message',
      adoptionRegDistrictError: 'ccd-field-write[field_id=\'registrationDistrict\'] .error-message',
      adoptionRegSubDistrictError: 'ccd-field-write[field_id=\'registrationSubDistrict\'] .error-message',
      adoptionRegCountyError: 'ccd-field-write[field_id=\'registrationCounty\'] .error-message',
      adoptionRegDateError: '#adoptionRegistrationDate .error-message',
    },
    finalOrderRecipients: {
      FAORecipientsError: '#errors',
      FAORecipientFirstApplicant: '#recipientsListA76-firstApplicant',
      FAORecipientSecondApplicant: '#recipientsListA76-secondApplicant',
      FAORecipientBirthMother: '#recipientsListA206-respondentBirthMother',
      FAORecipientBirthFather: '#recipientsListA206-respondentBirthFather',
      FAORecipientCafCass: '#recipientsListA206-legalGuardianCAFCASS',
      FAORecipientChildLocalAuthority: '#recipientsListA206-childsLocalAuthority',
      FAORecipientApplicantLocalAuthority: '#recipientsListA206-applicantsLocalAuthority',
      FAORecipientAdoptionAgency: '#recipientsListA206-adoptionAgency',
      FAORecipientOtherAdoptionAgency: '#recipientsListA206-otherAdoptionAgency',
      FAORecipientOtherWithResponsibility: '#recipientsListA206-otherPersonWithParentalResponsibility',
      placementOfTheChild: '//label[@for="placementOfTheChildList"]/following-sibling::div[1]/input'
    },
    dateOrderMadeError: '#dateOrderMadeFinalAdoptionOrder .error-message',
    dateOrderMade: {
      day: '#dateOrderMadeFinalAdoptionOrder-day',
      month: '#dateOrderMadeFinalAdoptionOrder-month',
      year: '#dateOrderMadeFinalAdoptionOrder-year'
    }

  },

  async selectFinalOrderAndVerify() {
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see('Select type of order is required', this.fields.manageOrderTypeErrorMessage);
    await I.retry(3).click(this.fields.finalAdoptionOrder);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  },

  async addJudgeNameAndChildAdoptionName() {
    await I.retry(3).fillField(this.fields.orderedBy, finalAdoptionOrderDetails.finalOrderDetails.nameOfJudgeIssuingOrder);
    await I.wait(3);
    await I.retry(3).click(this.fields.finalOrderRecipients.placementOfTheChild)
    await I.retry(3).fillField(this.fields.childFirstNameAfterAdoption, finalAdoptionOrderDetails.finalOrderDetails.childFirstNameAfterAdoption);
    await I.retry(3).fillField(this.fields.childLastNameAfterAdoption, finalAdoptionOrderDetails.finalOrderDetails.childLastnameAfterAdoption);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  },

  async verifyFAODateAndPlaceOfBirth() {
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.FAODateAndPlaceOFBirth);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.errorMessage, this.fields.placeAndDateOfBirth.birthProvedError);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.dateOfBirthProvedYes);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.placeOfBirthProvidedNo);
    await I.wait(3);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.birthLocationUK);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.birthLocationOutsideUK);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.birthLocation, finalAdoptionOrderDetails.finalOrderDetails.locationOfBirth);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.placeOfBirthProvidedYes);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.errorMessage, this.fields.placeAndDateOfBirth.certificateError);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.errorMessage, this.fields.placeAndDateOfBirth.countryOfBirthError);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.birthCertificate);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.adoptionCertificate);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.countryOfBirthUK);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.countryOfBirthOutsideUK);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.countryOfBirth, finalAdoptionOrderDetails.finalOrderDetails.countryOfBirth);
    await I.wait(3);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.errorMessage, this.fields.placeAndDateOfBirth.timeOfBirthError);
    await I.retry().click(this.fields.placeAndDateOfBirth.timeOfBirthNo);
    await I.retry().click(this.fields.placeAndDateOfBirth.timeOfBirthKnown);
    await I.wait(1);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.timeOfBirth, finalAdoptionOrderDetails.finalOrderDetails.timeOfBirth);
    await I.wait(3);

  },

  async verifyFinalAdoptionRegistrationDetails() {
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.birthOrAdoptionRegNumber, finalAdoptionOrderDetails.finalOrderDetails.adoptionRegNumber);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.birthOrAdoptionRegDateDay, finalAdoptionOrderDetails.finalOrderDetails.birthAdoptionRegistrationDate.day);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.birthOrAdoptionRegDateMonth, finalAdoptionOrderDetails.finalOrderDetails.birthAdoptionRegistrationDate.month);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.birthOrAdoptionRegDateYear, finalAdoptionOrderDetails.finalOrderDetails.birthAdoptionRegistrationDate.year);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.registrationDistrict, finalAdoptionOrderDetails.finalOrderDetails.adoptionRegDistrict);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.registrationSubDistrict, finalAdoptionOrderDetails.finalOrderDetails.adoptionSubDistrict);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.registrationCounty, finalAdoptionOrderDetails.finalOrderDetails.adoptionRegCounty);
    await I.wait(3);
  },

  async verifyFinalAdoptionOrderRecipients() {
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.finalAdoptionOrderRecipients);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.selectWhoToServeOrderTo);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.finalOrderRecipientsError, this.fields.finalOrderRecipients.FAORecipientsError);
    await I.retry(3).click(this.fields.finalOrderRecipients.FAORecipientFirstApplicant);
    await I.retry(3).click(this.fields.finalOrderRecipients.FAORecipientSecondApplicant);
    await I.retry(3).click(this.fields.finalOrderRecipients.FAORecipientBirthMother);
    await I.retry(3).click(this.fields.finalOrderRecipients.FAORecipientBirthFather);
    await I.retry(3).click(this.fields.finalOrderRecipients.FAORecipientChildLocalAuthority);
    await I.retry(3).click(this.fields.finalOrderRecipients.FAORecipientApplicantLocalAuthority);
    await I.retry(3).click(this.fields.finalOrderRecipients.FAORecipientAdoptionAgency);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  },

  async verifyFinalAdoptionOrderRecipientsCYA() {
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.checkYAnswers);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.childFirstNameAfterAdoption);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.childLastnameAfterAdoption);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.timeOfBirth);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.adoptionRegNumber);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.adoptionRegDistrict);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.adoptionSubDistrict);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.adoptionRegCounty);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.selectWhoToServeOrderTo);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.firstApplicant);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.secondApplicant);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.birthFather);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.birthMother);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.childLocalAuthority);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.applicantLocalAuthority);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.adoptionAgency);
    await I.retry(3).seeElement(this.fields.previewOrderLink);
    await I.retry(3).seeElement(this.fields.previewOrderLinkA206)
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  },

  async addJudgeNameChildAdoptionNameAndDateOrderMade() {
    await I.wait(3);
    await I.retry(3).see('Date order made');
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.orderedBy, finalAdoptionOrderDetails.finalOrderDetails.nameOfJudgeIssuingOrder);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.errorMessage, this.fields.dateOrderMadeError);
    await I.retry(3).fillField(this.fields.dateOrderMade.day, finalAdoptionOrderDetails.dom.day);
    await I.retry(3).fillField(this.fields.dateOrderMade.month, finalAdoptionOrderDetails.dom.month);
    await I.retry(3).fillField(this.fields.dateOrderMade.year, finalAdoptionOrderDetails.dom.year);
    await I.retry(3).click(this.fields.finalOrderRecipients.placementOfTheChild)
    await I.retry(3).fillField(this.fields.childFirstNameAfterAdoption, finalAdoptionOrderDetails.finalOrderDetails.childFirstNameAfterAdoption);
    await I.retry(3).fillField(this.fields.childLastNameAfterAdoption, finalAdoptionOrderDetails.finalOrderDetails.childLastnameAfterAdoption);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  },

  async finalAdoptionOrderManagementDateOrderMadeCYAPage() {
    await I.wait(3);
    await I.retry(3).see('12 Mar 2021');
  },

  async verifyOrderPreviewScreen(){
    await I.wait(3);
    await I.retry(3).waitForText('Preview the draft order', 30);
    await I.retry(3).see('Preview the draft order');
    await I.retry(3).see('Preview and check the order in draft. You can make changes on the next page');
    await I.retry().seeElement(this.fields.previewOrderLink);
    await I.retry().seeElement(this.fields.previewOrderLinkA206);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  }

}
