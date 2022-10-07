const config = require('../config');
const { I } = inject();
module.exports = {
  fields: {
    allocateJudgeTitle: '//h1[contains(text(),"Allocate judge")]',
    childNameHeader: '//h3[contains(text(),"Child\'s Name: child child")]',
    chooseAJudgeHeader: '//h2[contains(text(),"Choose a judge for this case")]',
    chooseJudgeHintText: '//p[contains(text(),"Choose whether the placement judge is presiding or a new judge is being allocated.")]',
    textBoxTitle: '//span[contains(text(),"Name of the judge")]',
    allocateJudgeTextBox: '#allocatedJudge',
    continueButton: 'button[type="submit"]',
    errorMessage: '//span[contains(text(),"Name of the judge is required")]',
    alertMessage: '//div[@class="alert-message"]',
  },

  async verifyPageDetails() {
    await I.retry(3).seeElement(this.fields.allocateJudgeTitle);
    await I.retry(3).seeElement(this.fields.allocateJudgeTitle);
    await I.retry(3).seeElement(this.fields.childNameHeader);
    await I.retry(3).seeElement(this.fields.chooseAJudgeHeader);
    await I.retry(3).seeElement(this.fields.chooseJudgeHintText);
    await I.retry(3).seeElement(this.fields.textBoxTitle);
    await I.retry(3).seeElement(this.fields.allocateJudgeTextBox);
    await I.retry(3).seeElement(this.fields.continueButton);
  },

  async verifyAllocateJudgePageFunctionality(){
   await I.retry(3).fillField(this.fields.allocateJudgeTextBox, "");
   await I.retry(3).click(this.fields.continueButton);
   await I.retry(3).seeElement(this.fields.errorMessage);
   await I.retry(3).fillField(this.fields.allocateJudgeTextBox, "Test Judge name");
   await I.retry(3).click(this.fields.continueButton);
   await I.retry(3).click(this.fields.continueButton);
   await I.retry(3).seeElement(this.fields.alertMessage);
  },
}
