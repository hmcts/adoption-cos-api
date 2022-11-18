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
    placeAndDateOfBirth: {
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

    }

  },

  async selectFinalOrderAndVerify() {
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see('Select type of order is required', this.fields.manageOrderTypeErrorMessage);
    await I.retry(3).click(this.fields.finalAdoptionOrder);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  },

  async addJudgeNameAndChildAdoptionName() {
    await I.retry(3).fillField(this.fields.orderedBy, finalAdoptionOrderDetails.finalOrderDetails.nameOfJudgeIssuingOrder);
    await I.wait(10);
    await I.retry(3).click(this.fields.finalOrderRecipients.placementOfTheChild)
    await I.wait(3);
    await I.retry(3).fillField(this.fields.childFirstNameAfterAdoption, finalAdoptionOrderDetails.finalOrderDetails.childFirstNameAfterAdoption);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.childLastNameAfterAdoption, finalAdoptionOrderDetails.finalOrderDetails.childLastnameAfterAdoption);
    await I.wait(3);
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
    await I.wait(3);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.placeOfBirthProvidedNo);
    await I.wait(3);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.birthLocationUK);
    await I.wait(3);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.birthLocationOutsideUK);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.birthLocation, finalAdoptionOrderDetails.finalOrderDetails.locationOfBirth);
    await I.wait(3);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.placeOfBirthProvidedYes);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.errorMessage, this.fields.placeAndDateOfBirth.certificateError);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.errorMessage, this.fields.placeAndDateOfBirth.countryOfBirthError);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.birthCertificate);
    await I.wait(3);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.adoptionCertificate);
    await I.wait(3);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.countryOfBirthUK);
    await I.wait(3);
    await I.retry(3).click(this.fields.placeAndDateOfBirth.countryOfBirthOutsideUK);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.countryOfBirth, finalAdoptionOrderDetails.finalOrderDetails.countryOfBirth);
    await I.wait(3);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.errorMessage, this.fields.placeAndDateOfBirth.timeOfBirthError);
    await I.wait(3);
    await I.retry().click(this.fields.placeAndDateOfBirth.timeOfBirthNo);
    await I.wait(1);
    await I.retry().click(this.fields.placeAndDateOfBirth.timeOfBirthKnown);
    await I.wait(1);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.timeOfBirth, finalAdoptionOrderDetails.finalOrderDetails.timeOfBirth);
    await I.wait(3);

  },

  async verifyFinalAdoptionRegistrationDetails() {
    await I.retry(3).click(this.fields.continueButton);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.errorMessage, this.fields.placeAndDateOfBirth.adoptionRegNumberError);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.birthOrAdoptionRegNumber, finalAdoptionOrderDetails.finalOrderDetails.adoptionRegNumber);
    await I.wait(3);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.errorMessage, this.fields.placeAndDateOfBirth.adoptionRegDateError);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.birthOrAdoptionRegDateDay, finalAdoptionOrderDetails.finalOrderDetails.birthAdoptionRegistrationDate.day);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.birthOrAdoptionRegDateMonth, finalAdoptionOrderDetails.finalOrderDetails.birthAdoptionRegistrationDate.month);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.birthOrAdoptionRegDateYear, finalAdoptionOrderDetails.finalOrderDetails.birthAdoptionRegistrationDate.year);
    await I.wait(3);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.errorMessage, this.fields.placeAndDateOfBirth.adoptionRegDistrictError);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.registrationDistrict, finalAdoptionOrderDetails.finalOrderDetails.adoptionRegDistrict);
    await I.wait(3);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.errorMessage, this.fields.placeAndDateOfBirth.adoptionRegSubDistrictError);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.registrationSubDistrict, finalAdoptionOrderDetails.finalOrderDetails.adoptionSubDistrict);
    await I.wait(3);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.errorMessage, this.fields.placeAndDateOfBirth.adoptionRegCountyError);
    await I.retry(3).fillField(this.fields.placeAndDateOfBirth.registrationCounty, finalAdoptionOrderDetails.finalOrderDetails.adoptionRegCounty);
    await I.wait(3);
  },

  async verifyFinalAdoptionOrderRecipients() {
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(10);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.finalAdoptionOrderRecipients);
    await I.wait(3);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.selectWhoToServeOrderTo);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.finalOrderRecipientsError, this.fields.finalOrderRecipients.FAORecipientsError);
    await I.wait(3);
    await I.wait(3);
    await I.retry(3).click(this.fields.finalOrderRecipients.FAORecipientFirstApplicant);
    await I.wait(3);
    await I.retry(3).click(this.fields.finalOrderRecipients.FAORecipientSecondApplicant);
    await I.wait(3);
    await I.retry(3).click(this.fields.finalOrderRecipients.FAORecipientBirthMother);
    await I.wait(3);
    await I.retry(3).click(this.fields.finalOrderRecipients.FAORecipientBirthFather);
    await I.wait(3);
    await I.retry(3).click(this.fields.finalOrderRecipients.FAORecipientChildLocalAuthority);
    await I.wait(3);
    await I.retry(3).click(this.fields.finalOrderRecipients.FAORecipientApplicantLocalAuthority);
    await I.wait(3);
    await I.retry(3).click(this.fields.finalOrderRecipients.FAORecipientAdoptionAgency);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    // await I.retry(3).click(this.fields.continueButton);
    // await I.wait(3);
  },

  async verifyFinalAdoptionOrderRecipientsCYA() {
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.checkYAnswers);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.childFirstNameAfterAdoption);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.childLastnameAfterAdoption);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.timeOfBirth);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.adoptionRegNumber);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.adoptionRegDistrict);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.adoptionSubDistrict);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.finalOrderDetails.adoptionRegCounty);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.selectWhoToServeOrderTo);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.firstApplicant);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.secondApplicant);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.birthFather);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.birthMother);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.childLocalAuthority);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.applicantLocalAuthority);
    await I.wait(1);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.adoptionAgency);
    await I.wait(3);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  }

}
