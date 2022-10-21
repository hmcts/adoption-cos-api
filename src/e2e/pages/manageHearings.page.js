const config = require('../config');
const { I } = inject();
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
 /*  To be implemented as part 2 journey
   await I.retry(5).click(this.fields.continueButton);
   await I.retry(5).seeElement(this.fields.alertMessage);*/
  },
}
