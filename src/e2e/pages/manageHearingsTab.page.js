const config = require('../config');
const { I } = inject();
const manageHearingFormData = require('../fixtures/manageHearings');
module.exports = {
    async verifyAddNewHearingTabDetails(){
      await I.wait(3);
      I.seeTextInTab(['New hearing 1', 'Accessibility requirements'], manageHearingFormData.accessibilityRequired);
      I.seeTextInTab(['New hearing 1', 'Court'], manageHearingFormData.courtOfHearing);
      I.seeTextInTab(['New hearing 1', 'Hearing date & time'], '15 Oct 2025, 11:15:55 PM');
      I.seeTextInTab(['New hearing 1', 'Hearing directions'], 'Hearing delay warning');
      I.seeTextInTab(['New hearing 1', 'Hearing directions'], 'Backup notice');
      I.seeTextInTab(['New hearing 1', 'Is an interpreter needed?'], manageHearingFormData.interpreterRequired);
      I.seeTextInTab(['New hearing 1', 'Judge'], manageHearingFormData.judgeOfHearing);
      I.seeTextInTab(['New hearing 1', 'Length of hearing'], manageHearingFormData.lengthOfHearing);
      I.seeTextInTab(['New hearing 1', 'Method of hearing'], 'Remote (via video hearing)');
      I.seeTextInTab(['New hearing 1', 'Recipients'], 'Respondent(birth mother)');
      I.seeTextInTab(['New hearing 1', 'Recipients'], 'Respondent(birth father)');
      I.seeTextInTab(['New hearing 1', 'Type of hearing'], manageHearingFormData.typeOfHearing);
      I.see("New hearing");
    },
};
