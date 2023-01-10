const config = require('../config');
const {I} = inject();

const finalAdoptionOrderDetails = require('../fixtures/finalAdoptionOrderDetails.js');
const manageOrderDetails = require("../fixtures/manageOrderDetails.js");
const sendOrReplyToMessagesData = require("../fixtures/sendOrReplyToMessagesDetails");
module.exports = {

  fields: {
    messageTab: '//div[text()="Messages"]',
    openMessages: '//*[text()="Open messages"]',
  },

  async selectMessageTab() {
    await I.wait(3);
    await I.retry(3).click(this.fields.messageTab);
    await I.wait(3);
  },

  async verifyMessageStatusOpen() {
    await I.retry(3).see('Open');
  },

  async verifyMessageDetailsUnderMessageTab() {
    await I.retry(3).see(manageOrderDetails.messagesTab.urgency);
    await I.retry(3).see(manageOrderDetails.messagesTab.message);
    await I.retry(3).see(manageOrderDetails.messagesTab.status);
    await I.retry(3).see('Judge');
    await I.retry(3).see('Refer for gatekeeping');
    await I.retry(3).see('High');
    await I.retry(3).see(sendOrReplyToMessagesData.message);

  },

}
