const config = require('../config');
const {I} = inject();
const manageHearingFormData = require('../fixtures/manageHearings');
module.exports = {
  fields: {
    allocateJudgeTitle: '//h1[contains(text(),"Manage hearings")]',
    childNameHeader: '//h3[contains(text(),"Child\'s name: child child")]',
    continueButton: 'button[type="submit"]',
    errorMessage: '//span[contains(text(),"Field is required")]',
    alertMessage: '//div[@class="alert-message"]',
    newHearing: '#manageHearingOptions-addNewHearing',
    vacateHearing: '#manageHearingOptions-vacateHearing',
    adjournHearing: '#manageHearingOptions-adjournHearing',
    hearingType: '#typeOfHearing',
    hearingDay: '#hearingDateAndTime-day',
    hearingMonth: '#hearingDateAndTime-month',
    hearingYear: '#hearingDateAndTime-year',
    hearingHour: '#hearingDateAndTime-hour',
    hearingMinute: '#hearingDateAndTime-minute',
    hearingSeconds: '#hearingDateAndTime-second',
    lengthOfHearing: '#lengthOfHearing',
    judge: '#judge',
    court: '#court',
    remoteMethod: '#methodOfHearing-remote',
    interpreterRequirements: '#isInterpreterNeeded',
    accessibilityRequirements: '#accessibilityRequirements',
    hearingDelay: '#hearingDirections-hearingDelayWaring',
    backupNotice: '#hearingDirections-backupNotice',
    firstApplicant: '#recipientsInTheCase-firstApplicant',
    secondApplicant: '#recipientsInTheCase-secondApplicant',
    recipientsBirthMother: '#recipientsInTheCase-respondentBirthMother',
    recipientsBirthFather: '#recipientsInTheCase-respondentBirthFather',
    legalGuardian: '#recipientsInTheCase-legalGuardianCafcass',
    childsLocalAuthority: '#recipientsInTheCase-childsLocalAuthority',
    applicantsLocalAuthority: '#recipientsInTheCase-applicantsLocalAuthority',
    adoptionAgency: '#recipientsInTheCase-adoptionAgency',
    otherAdoptionAgency: '#recipientsInTheCase-otherAdoptionAgency',
    otherParentWithParentalResponsibility: '#recipientsInTheCase-otherPersonWithParentalResponsibility',
    vacateHearingToSelect: '//input[@type="radio"]',
    adjournHearingSpecialMeasures: '#reasonForAdjournHearing-specialMeasuresRequired',
    adjournHearingInterpreter: '#reasonForAdjournHearing-interpreterRequired',
    reasonForAdjournAdminError: '#reasonForAdjournHearing-adminError',
    reasonForAdjournInternational: '#reasonForAdjournHearing-international',
    vacateHearingReasonAgreement: '#reasonForVacatingHearing-agreementConsentOrderMade',
    vacateHearingReasonWithDrawn: '#reasonForVacatingHearing-caseWithdrawn',
    vacateHearingReasonDismissed: '#reasonForVacatingHearing-caseDismissed',
    reListingYes: '#isTheHearingNeedsRelisting_Yes',
    reListingNo: '#isTheHearingNeedsRelisting_No',
    recipientsTitle: '//span[contains(text(),"Recipients")]',
    specialMeasuresRequired: '#reasonForAdjournHearing-specialMeasuresRequired',
    previewDraftLink:'//ccd-read-document-field/a[contains(text(), \' Birth_mother_hearing_notice.pdf\')]'
  },

  async verifyPageDetails() {
    await I.wait(3);
    await I.retry(3).seeElement(this.fields.allocateJudgeTitle);
    //await I.retry(3).seeElement(this.fields.childNameHeader);
    await I.retry(3).seeElement(this.fields.continueButton);
    await I.retry(3).seeElement(this.fields.newHearing);
    await I.retry(3).seeElement(this.fields.vacateHearing);
    await I.retry(3).seeElement(this.fields.adjournHearing);
  },

  async verifyManageHearingsPageFunctionality(){
    await I.retry(3).click(this.fields.continueButton);
    await I.retry(6).seeElement(this.fields.errorMessage);
    await I.retry(5).click(this.fields.newHearing);
    await I.retry(5).click(this.fields.continueButton);
  },

  async addNewHearingOptions(){
     await I.wait(3);
     await I.retry(3).waitForText('Add new hearing', 30);
     await I.fillField(this.fields.hearingType, manageHearingFormData.newHearing.typeOfHearing);
     await I.fillField(this.fields.hearingDay, manageHearingFormData.newHearing.dayOfHearing);
     await I.fillField(this.fields.hearingMonth, manageHearingFormData.newHearing.monthOfHearing);
     await I.fillField(this.fields.hearingYear, manageHearingFormData.newHearing.yearOfHearing);
     await I.fillField(this.fields.hearingHour, manageHearingFormData.newHearing.hourOfHearing);
     await I.fillField(this.fields.hearingMinute, manageHearingFormData.newHearing.minuteOfHearing);
     await I.fillField(this.fields.hearingSeconds, manageHearingFormData.newHearing.secondOfHearing);
     await I.fillField(this.fields.lengthOfHearing, manageHearingFormData.newHearing.lengthOfHearing);
     await I.fillField(this.fields.judge, manageHearingFormData.newHearing.judgeOfHearing);
     await I.fillField(this.fields.court, manageHearingFormData.newHearing.courtOfHearing);
     await I.fillField(this.fields.interpreterRequirements, manageHearingFormData.newHearing.interpreterRequired);
     await I.retry(5).click(this.fields.remoteMethod);
     await I.fillField(this.fields.accessibilityRequirements, manageHearingFormData.newHearing.accessibilityRequired);
     await I.retry(5).click(this.fields.hearingDelay);
     await I.retry(5).click(this.fields.backupNotice);
     await I.retry(5).click(this.fields.continueButton);
      await I.wait(3);
  },

  async addRecepientDetails(){
     await I.wait(5);
    await I.retry(3).waitForText('Recipients', 30);
    await I.retry(3).seeElement(this.fields.childNameHeader);
    await I.retry(3).seeElement(this.fields.recipientsTitle);
    // await I.retry(3).see('Only select people who are party to this case and who need a copy of this order.');
     await I.retry(5).click(this.fields.firstApplicant);
     await I.retry(5).click(this.fields.secondApplicant);
     await I.retry(5).click(this.fields.recipientsBirthMother);
     await I.retry(5).click(this.fields.recipientsBirthFather);
     await I.retry(5).click(this.fields.legalGuardian);
     await I.retry(5).click(this.fields.childsLocalAuthority);
     await I.retry(5).click(this.fields.applicantsLocalAuthority);
     await I.retry(5).click(this.fields.adoptionAgency);
     await I.retry(5).click(this.fields.otherAdoptionAgency);
     await I.retry(5).click(this.fields.otherParentWithParentalResponsibility);
     await I.retry(5).click(this.fields.continueButton);
     await I.wait(3);
     await I.see("Legal guardian (Cafcass) not applicable for the case");
     await I.see("Other adoption agency not applicable for the case");
     await I.wait(3);
    await I.retry(5).click(this.fields.otherParentWithParentalResponsibility);
     await I.retry(5).click(this.fields.legalGuardian);
     await I.retry(5).click(this.fields.otherAdoptionAgency);
     await I.wait(3);
     await I.retry(5).click(this.fields.continueButton);
     await I.wait(5);
   },


  async verifyAddNewHearingCheckYourAnswers() {
    await I.wait(3);
    await I.see(manageHearingFormData.newHearing.typeOfHearing);
    await I.see('15 Oct 2025, 11:15:55 PM');
    await I.see(manageHearingFormData.newHearing.lengthOfHearing);
    await I.see(manageHearingFormData.newHearing.judgeOfHearing);
    await I.see(manageHearingFormData.newHearing.courtOfHearing);
    await I.see(manageHearingFormData.newHearing.interpreterRequired);
    await I.see('Remote (via video hearing)');
    await I.see(manageHearingFormData.newHearing.accessibilityRequired);
    await I.see('Hearing delay warning');
    await I.see('Backup notice');
    await I.see(manageHearingFormData.checkYourAnswers.recipientApplicant1);
    await I.see(manageHearingFormData.checkYourAnswers.recipientApplicant2);
    await I.see(manageHearingFormData.checkYourAnswers.recipientBirthMother);
    await I.see(manageHearingFormData.checkYourAnswers.recipientBirthFather);
    await I.see(manageHearingFormData.checkYourAnswers.recipientChildLA);
    await I.see(manageHearingFormData.checkYourAnswers.recipientApplicantLA);
    await I.see(manageHearingFormData.checkYourAnswers.recipientAdopAgency);
   // await I.see(manageHearingFormData.checkYourAnswers.recipientOtherParentalResponsibility);
    await I.see(manageHearingFormData.checkYourAnswers.addNewHearingOption);
    await I.retry(5).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(5).seeElement(this.fields.alertMessage);
  },

 async selectVacateHearingOptionWithAgreementAndRelisting(){
    await I.retry(5).click(this.fields.vacateHearing);
    await I.retry(5).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(5).click(this.fields.vacateHearingToSelect);
    await I.retry(5).click(this.fields.continueButton);
    await I.retry(5).seeElement(this.fields.vacateHearingReasonAgreement);
    await I.retry(5).seeElement(this.fields.vacateHearingReasonWithDrawn);
    await I.retry(5).seeElement(this.fields.vacateHearingReasonDismissed);
    await I.retry(5).click(this.fields.vacateHearingReasonAgreement);
    await I.retry(5).click(this.fields.continueButton);
    await I.retry(5).seeElement(this.fields.reListingYes);
    await I.retry(5).seeElement(this.fields.reListingNo);
    await I.retry(5).click(this.fields.reListingYes);
    await I.retry(5).click(this.fields.continueButton);
  },

 async addNewHearingForVacateHearingOptions(){
    await I.fillField(this.fields.hearingType, manageHearingFormData.vacateHearing.typeOfHearingVacate);
    await I.fillField(this.fields.hearingDay, manageHearingFormData.vacateHearing.dayOfHearing);
    await I.fillField(this.fields.hearingMonth, manageHearingFormData.vacateHearing.monthOfHearing);
    await I.fillField(this.fields.hearingYear, manageHearingFormData.vacateHearing.yearOfHearing);
    await I.fillField(this.fields.hearingHour, manageHearingFormData.vacateHearing.hourOfHearing);
    await I.fillField(this.fields.hearingMinute, manageHearingFormData.vacateHearing.minuteOfHearing);
    await I.fillField(this.fields.hearingSeconds, manageHearingFormData.vacateHearing.secondOfHearing);
    await I.fillField(this.fields.lengthOfHearing, manageHearingFormData.vacateHearing.lengthOfHearing);
    await I.fillField(this.fields.judge, manageHearingFormData.vacateHearing.judgeOfHearing);
    await I.fillField(this.fields.court, manageHearingFormData.vacateHearing.courtOfHearing);
    await I.fillField(this.fields.interpreterRequirements, manageHearingFormData.vacateHearing.interpreterRequired);
    await I.retry(5).click(this.fields.remoteMethod);
    await I.fillField(this.fields.accessibilityRequirements, manageHearingFormData.vacateHearing.accessibilityRequired);
    await I.retry(5).click(this.fields.hearingDelay);
    await I.retry(5).click(this.fields.backupNotice);
    await I.retry(5).click(this.fields.continueButton);
    await I.wait(3);
  },

  async verifyVacateHearingWithRelistingCheckYourAnswers() {
    await I.wait(5);
    await I.see(manageHearingFormData.checkYourAnswers.vacateHearingOption);
    // await I.see(manageHearingFormData.checkYourAnswers.vacateHearingName);
    await I.see(manageHearingFormData.checkYourAnswers.vacateHearingReason);
    await I.see(manageHearingFormData.vacateHearing.typeOfHearingVacate);
    await I.see('31 Dec 2035, 8:30:00 AM');
    await I.see(manageHearingFormData.vacateHearing.lengthOfHearing);
    await I.see(manageHearingFormData.vacateHearing.judgeOfHearing);
    await I.see(manageHearingFormData.vacateHearing.courtOfHearing);
    await I.see(manageHearingFormData.vacateHearing.interpreterRequired);
    await I.see('Remote (via video hearing)');
    await I.see(manageHearingFormData.vacateHearing.accessibilityRequired);
    await I.see('Hearing delay warning');
    await I.see('Backup notice');
    await I.see(manageHearingFormData.checkYourAnswers.recipientApplicant1);
    await I.see(manageHearingFormData.checkYourAnswers.recipientApplicant2);
    await I.see(manageHearingFormData.checkYourAnswers.recipientChildLA);
    await I.see(manageHearingFormData.checkYourAnswers.recipientApplicantLA);
    await I.see(manageHearingFormData.checkYourAnswers.recipientAdopAgency);
    //await I.see(manageHearingFormData.checkYourAnswers.recipientOtherParentalResponsibility);
    await I.retry(3).seeElement(this.fields.previewDraftLink);
    await I.wait(3);
    await I.retry(5).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(5).seeElement(this.fields.alertMessage);
  },

  async genericAddNewHearing() {

    await this.verifyPageDetails();
    await this.verifyManageHearingsPageFunctionality();
    await this.addNewHearingOptions();
    await this.addRecepientDetails();
    await this.verifyPreviewDraft();
    await this.verifyAddNewHearingCheckYourAnswers();

  },

  async selectVacateHearingOptionWithAgreementAndNoRelisting() {
    await I.retry(5).click(this.fields.vacateHearing);
    await I.retry(5).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(5).click(this.fields.vacateHearingToSelect);
    await I.retry(5).click(this.fields.continueButton);
    await I.retry(5).seeElement(this.fields.vacateHearingReasonAgreement);
    await I.retry(5).seeElement(this.fields.vacateHearingReasonWithDrawn);
    await I.retry(5).seeElement(this.fields.vacateHearingReasonDismissed);
    await I.retry(5).click(this.fields.vacateHearingReasonAgreement);
    await I.retry(5).click(this.fields.continueButton);
    await I.retry(5).seeElement(this.fields.reListingYes);
    await I.retry(5).seeElement(this.fields.reListingNo);
    await I.retry(5).click(this.fields.reListingNo);
    await I.retry(5).click(this.fields.continueButton);
  },

  async verifyVacateHearingNoRelistingCheckYourAnswers() {
    await I.wait(3);
    await I.see(manageHearingFormData.checkYourAnswers.vacateHearingOption);
    //  await I.see(manageHearingFormData.checkYourAnswers.vacateHearingName);
    await I.see(manageHearingFormData.checkYourAnswers.vacateHearingReason);
    await I.see(manageHearingFormData.checkYourAnswers.relistingNo);
    await I.retry(5).click(this.fields.continueButton);
    await I.retry(5).seeElement(this.fields.alertMessage);
  },

  async selectAdjournHearingNoRelisting() {
    await I.retry(5).click(this.fields.adjournHearing);
    await I.retry(5).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(5).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see('Select a hearing you want to adjourn is required');
    await I.retry(5).click(this.fields.vacateHearingToSelect);
    await I.retry(5).click(this.fields.continueButton);
    await I.retry(5).seeElement(this.fields.adjournHearingSpecialMeasures);
    await I.retry(5).seeElement(this.fields.adjournHearingInterpreter);
    await I.retry(5).seeElement(this.fields.reasonForAdjournAdminError);
    await I.retry(5).seeElement(this.fields.reasonForAdjournInternational);
    await I.retry(5).click(this.fields.continueButton);
    await I.retry(3).see('Reason for adjournment is required');
    await I.retry(5).click(this.fields.adjournHearingSpecialMeasures);
    await I.retry(5).click(this.fields.continueButton);
    await I.retry(5).seeElement(this.fields.reListingYes);
    await I.retry(5).seeElement(this.fields.reListingNo);
    await I.retry(5).click(this.fields.continueButton);
    await I.retry(3).see('Does the hearing need to be relisted? is required');
    await I.retry(5).click(this.fields.reListingNo);
    await I.retry(5).click(this.fields.continueButton);
  },

    async verifyAdjournHearingNoRelistingCheckYourAnswers(){
        await I.wait(3);
        await I.see(manageHearingFormData.checkYourAnswers.adjournHearingOption);
     //   await I.see(manageHearingFormData.checkYourAnswers.vacateHearingName);
        await I.see(manageHearingFormData.checkYourAnswers.adjournHearingReason);
        await I.see(manageHearingFormData.checkYourAnswers.relistingNo);
        await I.retry(5).click(this.fields.continueButton);
        await I.wait(3);
        await I.retry(5).seeElement(this.fields.alertMessage);
      },


  async addRecepientDetailsWithOutFirstAndSecondApplicant() {
    await I.retry(3).seeElement(this.fields.childNameHeader);
    await I.retry(3).seeElement(this.fields.recipientsTitle);
    await I.retry(3).see('Only select people who are party to this case and who need a copy of this order.');
    await I.retry(5).click(this.fields.recipientsBirthMother);
    await I.retry(5).click(this.fields.recipientsBirthFather);
    await I.retry(5).click(this.fields.legalGuardian);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see('Check your answers')

  },

  async verifyPreviewDraft(){
    await I.wait(3);
    await I.retry(3).waitForText('Preview the hearing notice', 30);
    await I.retry(3).see('Birth_mother_hearing_notice.pdf');
    await I.retry(3).see('Birth_father_hearing_notice.pdf');
    await I.retry(3).see('Applicants_hearing_notice.pdf');
    await I.retry(5).click(this.fields.continueButton);
    await I.wait(3);
  },


  async selectAdjournHearingYesRelisting(){
        await I.wait(3);
        await I.retry(5).click(this.fields.adjournHearing);
        await I.retry(5).click(this.fields.continueButton);
        await I.wait(3);
        await I.retry(5).click(this.fields.continueButton);
        await I.wait(3);
        await I.retry(3).see('Select a hearing you want to adjourn is required');
        await I.wait(3);
        await I.retry(5).click(this.fields.vacateHearingToSelect);
        await I.retry(5).click(this.fields.continueButton);
        await I.retry(3).waitForText('Reason for adjournment', 30);
        await I.retry(5).click(this.fields.continueButton);
        await I.retry(3).see('Reason for adjournment is required');
        await I.retry(5).click(this.fields.specialMeasuresRequired);
        await I.retry(5).click(this.fields.continueButton);
        await I.retry(5).seeElement(this.fields.reListingYes);
        await I.retry(5).seeElement(this.fields.reListingNo);
        await I.retry(5).click(this.fields.continueButton);
        await I.retry(3).see('Does the hearing need to be relisted? is required');
        await I.retry(5).click(this.fields.reListingYes);
        await I.retry(5).click(this.fields.continueButton);
      },

  async verifyAdjournHearingWithRelistingCheckYourAnswers(){
      await I.wait(3);
      await I.see(manageHearingFormData.checkYourAnswers.adjournHearingOption);
    //  await I.see(manageHearingFormData.checkYourAnswers.vacateHearingName);
      await I.see(manageHearingFormData.checkYourAnswers.adjournHearingReason);
      await I.see(manageHearingFormData.vacateHearing.typeOfHearingVacate);
      await I.see('31 Dec 2035, 8:30:00 AM');
      await I.see(manageHearingFormData.vacateHearing.lengthOfHearing);
      await I.see(manageHearingFormData.vacateHearing.judgeOfHearing);
      await I.see(manageHearingFormData.vacateHearing.courtOfHearing);
      await I.see(manageHearingFormData.vacateHearing.interpreterRequired);
      await I.see('Remote (via video hearing)');
      await I.see(manageHearingFormData.vacateHearing.accessibilityRequired);
      await I.see('Hearing delay warning');
      await I.see('Backup notice');
      await I.see(manageHearingFormData.checkYourAnswers.recipientApplicant1);
      await I.see(manageHearingFormData.checkYourAnswers.recipientApplicant2);
      await I.see(manageHearingFormData.checkYourAnswers.recipientChildLA);
      await I.see(manageHearingFormData.checkYourAnswers.recipientApplicantLA);
      await I.see(manageHearingFormData.checkYourAnswers.recipientAdopAgency);
     // await I.see(manageHearingFormData.checkYourAnswers.recipientOtherParentalResponsibility);
      await I.wait(3);
      await I.retry(5).click(this.fields.continueButton);
      await I.wait(3);
      await I.retry(5).seeElement(this.fields.alertMessage);
    },

};
