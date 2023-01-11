const config = require('../config');
const { I } = inject();
const manageHearingFormData = require('../fixtures/manageHearings');
module.exports = {
    async verifyAddNewHearingTabDetails(){
      await I.wait(3);
      I.seeTextInTab(['New hearing 1', 'Accessibility requirements'], manageHearingFormData.newHearing.accessibilityRequired);
      I.seeTextInTab(['New hearing 1', 'Court'], manageHearingFormData.newHearing.courtOfHearing);
      I.seeTextInTab(['New hearing 1', 'Hearing date & time'], '15 Oct 2025, 11:15:55 PM');
      I.seeTextInTab(['New hearing 1', 'Hearing directions'], 'Hearing delay warning');
      I.seeTextInTab(['New hearing 1', 'Hearing directions'], 'Backup notice');
      I.seeTextInTab(['New hearing 1', 'Is an interpreter needed?'], manageHearingFormData.newHearing.interpreterRequired);
      I.seeTextInTab(['New hearing 1', 'Judge'], manageHearingFormData.newHearing.judgeOfHearing);
      I.seeTextInTab(['New hearing 1', 'Length of hearing'], manageHearingFormData.newHearing.lengthOfHearing);
      I.seeTextInTab(['New hearing 1', 'Method of hearing'], 'Remote (via video hearing)');
      I.seeTextInTab(['New hearing 1', 'Recipients'], 'Respondent (birth mother)');
      I.seeTextInTab(['New hearing 1', 'Recipients'], 'Respondent (birth father)');
      I.seeTextInTab(['New hearing 1', 'Type of hearing'], manageHearingFormData.newHearing.typeOfHearing);
      I.see("New hearing");
    },

    async verifyNewHearingForVacateHearingWithRelisting(){
      await I.wait(3);
      I.seeTextInTab(['New hearing 1', 'Type of hearing'], manageHearingFormData.vacateHearing.typeOfHearing);
      I.seeTextInTab(['New hearing 1', 'Hearing date & time'], '31 Dec 2035, 8:30:00 AM');
      I.seeTextInTab(['New hearing 1', 'Length of hearing'], manageHearingFormData.vacateHearing.lengthOfHearing);
      I.seeTextInTab(['New hearing 1', 'Judge'], manageHearingFormData.vacateHearing.judgeOfHearing);
      I.seeTextInTab(['New hearing 1', 'Court'], manageHearingFormData.vacateHearing.courtOfHearing);
      I.seeTextInTab(['New hearing 1', 'Is an interpreter needed?'], manageHearingFormData.vacateHearing.interpreterRequired);
      I.seeTextInTab(['New hearing 1', 'Method of hearing'], 'Remote (via video hearing)');
      I.seeTextInTab(['New hearing 1', 'Accessibility requirements'], manageHearingFormData.vacateHearing.accessibilityRequired);
      I.seeTextInTab(['New hearing 1', 'Hearing directions'], 'Hearing delay warning');
      I.seeTextInTab(['New hearing 1', 'Hearing directions'], 'Backup notice');
      I.seeTextInTab(['New hearing 1', 'Recipients'], 'Respondent (birth mother)');
      I.seeTextInTab(['New hearing 1', 'Recipients'], 'Respondent (birth father)');
      I.seeTextInTab(['New hearing 1', 'Recipients'], 'Applicant 1');
      I.seeTextInTab(['New hearing 1', 'Recipients'], 'Applicant 2');
      I.seeTextInTab(['New hearing 1', 'Recipients'], 'Child\'s local authority');
      I.seeTextInTab(['New hearing 1', 'Recipients'], 'Applicant\'s local authority');
      I.seeTextInTab(['New hearing 1', 'Recipients'], 'Adoption agency');
     // I.seeTextInTab(['New hearing 1', 'Recipients'], 'Other person with parental responsibility');
    },
  async verifyVacatedHearingForVacateHearingWithRelisting(){
      await I.wait(3);
      I.seeTextInTab(['Vacated hearings 1', 'Type of hearing'], manageHearingFormData.newHearing.typeOfHearing);
      I.seeTextInTab(['Vacated hearings 1', 'Hearing date & time'], '15 Oct 2025, 11:15:55 PM');
      I.seeTextInTab(['Vacated hearings 1', 'Length of hearing'], manageHearingFormData.newHearing.lengthOfHearing);
      I.seeTextInTab(['Vacated hearings 1', 'Judge'], manageHearingFormData.newHearing.judgeOfHearing);
      I.seeTextInTab(['Vacated hearings 1', 'Court'], manageHearingFormData.newHearing.courtOfHearing);
      I.seeTextInTab(['Vacated hearings 1', 'Is an interpreter needed?'], manageHearingFormData.newHearing.interpreterRequired);
      I.seeTextInTab(['Vacated hearings 1', 'Method of hearing'], 'Remote (via video hearing)');
      I.seeTextInTab(['Vacated hearings 1', 'Accessibility requirements'], manageHearingFormData.newHearing.accessibilityRequired);
      I.seeTextInTab(['Vacated hearings 1', 'Hearing directions'], 'Hearing delay warning');
      I.seeTextInTab(['Vacated hearings 1', 'Hearing directions'], 'Backup notice');
      I.seeTextInTab(['Vacated hearings 1', 'Recipients'], 'Respondent (birth mother)');
      I.seeTextInTab(['Vacated hearings 1', 'Recipients'], 'Respondent (birth father)');
      I.seeTextInTab(['Vacated hearings 1', 'Recipients'], 'Applicant 1');
      I.seeTextInTab(['Vacated hearings 1', 'Recipients'], 'Applicant 2');
      I.seeTextInTab(['Vacated hearings 1', 'Recipients'], 'Child\'s local authority');
      I.seeTextInTab(['Vacated hearings 1', 'Recipients'], 'Applicant\'s local authority');
      I.seeTextInTab(['Vacated hearings 1', 'Recipients'], 'Adoption agency');
     // I.seeTextInTab(['Vacated hearings 1', 'Recipients'], 'Other person with parental responsibility');
      I.seeTextInTab(['Vacated hearings 1', 'Reason for vacating hearing'], 'Agreement or consent order made');
    },

    async verifyVacatedHearingForAdjournHearingWithRelisting(){
          await I.wait(3);
          I.seeTextInTab(['Adjourned hearing 1', 'Type of hearing'], manageHearingFormData.newHearing.typeOfHearing);
          I.seeTextInTab(['Adjourned hearing 1', 'Hearing date & time'], '15 Oct 2025, 11:15:55 PM');
          I.seeTextInTab(['Adjourned hearing 1', 'Length of hearing'], manageHearingFormData.newHearing.lengthOfHearing);
          I.seeTextInTab(['Adjourned hearing 1', 'Judge'], manageHearingFormData.newHearing.judgeOfHearing);
          I.seeTextInTab(['Adjourned hearing 1', 'Court'], manageHearingFormData.newHearing.courtOfHearing);
          I.seeTextInTab(['Adjourned hearing 1', 'Is an interpreter needed?'], manageHearingFormData.newHearing.interpreterRequired);
          I.seeTextInTab(['Adjourned hearing 1', 'Method of hearing'], 'Remote (via video hearing)');
          I.seeTextInTab(['Adjourned hearing 1', 'Accessibility requirements'], manageHearingFormData.newHearing.accessibilityRequired);
          I.seeTextInTab(['Adjourned hearing 1', 'Hearing directions'], 'Hearing delay warning');
          I.seeTextInTab(['Adjourned hearing 1', 'Hearing directions'], 'Backup notice');
          I.seeTextInTab(['Adjourned hearing 1', 'Recipients'], 'Respondent (birth mother)');
          I.seeTextInTab(['Adjourned hearing 1', 'Recipients'], 'Respondent (birth father)');
          I.seeTextInTab(['Adjourned hearing 1', 'Recipients'], 'Applicant 1');
          I.seeTextInTab(['Adjourned hearing 1', 'Recipients'], 'Applicant 2');
          I.seeTextInTab(['Adjourned hearing 1', 'Recipients'], 'Child\'s local authority');
          I.seeTextInTab(['Adjourned hearing 1', 'Recipients'], 'Applicant\'s local authority');
          I.seeTextInTab(['Adjourned hearing 1', 'Recipients'], 'Adoption agency');
         // I.seeTextInTab(['Adjourned hearing 1', 'Recipients'], 'Other person with parental responsibility');
          I.seeTextInTab(['Adjourned hearing 1', 'Reason for adjournment'], 'Late filing of documents');
        },
};
