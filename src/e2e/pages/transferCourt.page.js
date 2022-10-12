const tranferCourtData = require('../fixtures/tranferCourt');
const { I } = inject();
module.exports = {
  fields: {
    transferCourtEvent: {
      transferCourt: '#transferCourt',
    },
    transferCourtCYA: {
      changeLinkCYA: '.case-field-change a',
    },

  },

  async courtNameDetails() {
    await I.retry(3).waitForText('Enter court name',30);
    await I.retry(3).fillField(this.fields.transferCourtEvent.transferCourt, tranferCourtData.courtName);
    await I.retry(3).click('Continue');
    await I.retry(3).waitForText('Check the information below carefully.',30);
    await I.retry(3).see(tranferCourtData.courtName);
    await I.retry(3).click(this.fields.transferCourtCYA.changeLinkCYA);
    await I.retry(3).waitForText('Enter court name',30);
    await I.retry(3).fillField(this.fields.transferCourtEvent.transferCourt, tranferCourtData.changeCourtName);
    await I.retry(3).click('Continue');
    await I.retry(3).waitForText('Check the information below carefully.',30);
    await I.retry(3).see(tranferCourtData.changeCourtName);
    await I.click('Save and continue');
  },

};
