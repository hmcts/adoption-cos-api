const config = require('../config');
const { I } = inject();
const manageHearingFormData = require('../fixtures/manageHearings');
module.exports = {
  fields: {
    allocateJudgeTitle: '//h1[contains(text(),"Manage hearings")]',
    childNameHeader: '//h3[contains(text(),"Child\'s Name: child child")]',
    continueButton: 'button[type="submit"]',
    errorMessage: '//span[contains(text(),"Field is required")]',
    alertMessage: '//div[@class="alert-message"]',
    newHearing: '#manageHearingOptions-addNewHearing',
    vacateHearing: '#manageHearingOptions-vacateHearing',
    adjournHearing: '#manageHearingOptions-adjournHearing',
    hearingType: '#manageHearingDetails_typeOfHearing',
    hearingDay: '#hearingDateAndTime-day',
    hearingMonth: '#hearingDateAndTime-month',
    hearingYear: '#hearingDateAndTime-year',
    hearingHour: '#hearingDateAndTime-hour',
    hearingMinute: '#hearingDateAndTime-minute',
    hearingSeconds: '#hearingDateAndTime-second',
    lengthOfHearing: '#manageHearingDetails_lengthOfHearing',
    judge: '#manageHearingDetails_judge',
    court: '#manageHearingDetails_court',
    remoteMethod: '#manageHearingDetails_methodOfHearing-remote',
    interpreterRequirements: '#manageHearingDetails_isInterpreterNeeded',
    accessibilityRequirements: '#manageHearingDetails_accessibilityRequirements',
    hearingDelay: '#manageHearingDetails_hearingDirections-hearingDelayWaring',
    backupNotice: '#manageHearingDetails_hearingDirections-backupNotice',
    firstApplicant: '#recipientsInTheCase-applicant1',
    secondApplicant: '#recipientsInTheCase-applicant2',
    recipientsBirthMother: '#recipientsInTheCase-respondentBirthMother',
    recipientsBirthFather: '#recipientsInTheCase-respondentBirthFather',
    legalGuardian: '#recipientsInTheCase-legalGuardian',
    childsLocalAuthority: '#recipientsInTheCase-childLocalAuthority',
    applicantsLocalAuthority: '#recipientsInTheCase-applicantLocalAuthority',
    adoptionAgency: '#recipientsInTheCase-adoptionAgency',
    otherAdoptionAgency: '#recipientsInTheCase-otherAdoptionAgency',
    otherParentWithParentalResponsibility: '#recipientsInTheCase-otherPersonWithParentalResponsibility',
    vacateHearingToSelect: '//input[@type="radio"]',
    vacateHearingReasonAgreement: '#reasonForVacatingHearing-agreementConsentOrderMade',
    vacateHearingReasonWithDrawn: '#reasonForVacatingHearing-caseWithdrawn',
    vacateHearingReasonDismissed: '#reasonForVacatingHearing-caseDismissed',
    reListingYes: '#isTheHearingNeedsRelisting_Yes',
    reListingNo: '#isTheHearingNeedsRelisting_No',
    recipientsTitle: '//span[contains(text(),"Hearing notice recipients")]',
  },

  async verifyPageDetails() {
    await I.retry(3).seeElement(this.fields.allocateJudgeTitle);
    await I.retry(3).seeElement(this.fields.childNameHeader);
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
    I.waitForVisible(this.fields.hearingType, 30);
    await I.fillField(this.fields.hearingType, manageHearingFormData.typeOfHearing);
    await I.fillField(this.fields.hearingDay, manageHearingFormData.dayOfHearing);
    await I.fillField(this.fields.hearingMonth, manageHearingFormData.monthOfHearing);
    await I.fillField(this.fields.hearingYear, manageHearingFormData.yearOfHearing);
    await I.fillField(this.fields.hearingHour, manageHearingFormData.hourOfHearing);
    await I.fillField(this.fields.hearingMinute, manageHearingFormData.minuteOfHearing);
    await I.fillField(this.fields.hearingSeconds, manageHearingFormData.secondOfHearing);
    await I.fillField(this.fields.lengthOfHearing, manageHearingFormData.lengthOfHearing);
    await I.fillField(this.fields.judge, manageHearingFormData.judgeOfHearing);
    await I.fillField(this.fields.court, manageHearingFormData.courtOfHearing);
    await I.fillField(this.fields.interpreterRequirements, manageHearingFormData.interpreterRequired);
    await I.retry(5).click(this.fields.remoteMethod);
    await I.fillField(this.fields.accessibilityRequirements, manageHearingFormData.accessibilityRequired);
    await I.retry(5).click(this.fields.hearingDelay);
    await I.retry(5).click(this.fields.backupNotice);
    await I.retry(5).click(this.fields.continueButton);
    await I.wait(5);
  },

  async addRecepientDetails(){
     await I.retry(3).see("Child's Name: child child");
     await I.retry(3).see('Recipients');
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
     I.see("Legal guardian (CAFCASS) is not applicable");
     I.see("Other adoption agency is not applicable");
     await I.retry(5).click(this.fields.legalGuardian);
     await I.retry(5).click(this.fields.otherAdoptionAgency);
     await I.retry(5).click(this.fields.continueButton);
   },

  async verifyAddNewHearingCheckYourAnswers(){
    await I.wait(3);
    I.seeTextInPage(['Enter hearing details', 'Type of hearing'], manageHearingFormData.typeOfHearing);
    I.seeTextInPage(['Enter hearing details', 'Hearing date & time'], '15 Oct 2025, 11:15:55 PM');
    I.seeTextInPage(['Enter hearing details', 'Length of hearing'], manageHearingFormData.lengthOfHearing);
    I.seeTextInPage(['Enter hearing details', 'Judge'], manageHearingFormData.judgeOfHearing);
    I.seeTextInPage(['Enter hearing details', 'Court'], manageHearingFormData.courtOfHearing);
    I.seeTextInPage(['Enter hearing details', 'Is an interpreter needed?'], manageHearingFormData.interpreterRequired);
    I.seeTextInPage(['Enter hearing details', 'Method of hearing'], 'Remote (via video hearing)');
    I.seeTextInPage(['Enter hearing details', 'Accessibility requirements'], manageHearingFormData.accessibilityRequired);
    I.seeTextInPage(['Enter hearing details', 'Hearing directions'], 'Hearing delay warning');
    I.seeTextInPage(['Enter hearing details', 'Hearing directions'], 'Backup notice');
    I.see('Applicant 1');
    I.see('Applicant 2');
    I.see("Respondent(birth mother)");
    I.see("Respondent(birth father)");
    I.see("Child's local authority");
    I.see("Applicant's local authority");
    I.see("Adoption agency");
    I.see("Other person with parental responsibility");
    I.see("Add a new hearing");
    await I.retry(5).click(this.fields.continueButton);
    await I.retry(5).seeElement(this.fields.alertMessage);
  },

  async selectVacateHearingOptionWithAgreementAndRelisting(){
   I.waitForVisible(this.fields.vacateHearing, 30);
   await I.retry(5).click(this.fields.vacateHearing);
   await I.retry(5).click(this.fields.continueButton);
   await I.wait(3);
   I.waitForVisible(this.fields.vacateHearingToSelect, 30);
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
   /*check your answers page and options from re listing to be covered in next sprint*/
   await I.retry(5).click(this.fields.continueButton);
   await I.retry(5).seeElement(this.fields.alertMessage);
  }
};
