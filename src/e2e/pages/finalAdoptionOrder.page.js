const config = require('../config');
const {I} = inject();
const finalAdoptionOrderDetails = require('../fixtures/finalAdoptionOrderDetails.js');
module.exports = {
  fields: {
    manageOrderTypeErrorMessage: '#manageOrderType .error-message',
    continueButton: 'button[type="submit"]',
    preamble: '#preambleDetailsFinalAdoptionOrder',
    childFirstNameAfterAdoption: '#childrenFirstNameAfterAdoption',
    childLastNameAfterAdoption: '#childrenLastNameAfterAdoption',
    finalAdoptionOrder: '#manageOrderType-finalAdoptionOrder',
    orderedBy: '#orderedByFinalAdoptionOrder'

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
    await I.wait(3);
    await I.retry(3).fillField(this.fields.childFirstNameAfterAdoption, finalAdoptionOrderDetails.finalOrderDetails.childFirstNameAfterAdoption);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.childLastNameAfterAdoption, finalAdoptionOrderDetails.finalOrderDetails.childLastnameAfterAdoption);
    await I.wait(3);

  },
}
