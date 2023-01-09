const config = require('../config');
const {I} = inject();

const finalAdoptionOrderDetails = require('../fixtures/finalAdoptionOrderDetails.js');
const manageOrderDetails = require("../fixtures/manageOrderDetails.js");
const sendOrReplyToMessagesData = require("../fixtures/sendOrReplyToMessagesDetails");
module.exports = {

  fields: {
    messageTab: '//div[text()="Messages"]',
    openMessages: '//*[text()="Open messages"]',
    closedMessages: '//*[text()="Closed Messages"]',
  },

  async selectMessageTab() {
    await I.wait(3);
    await I.retry(3).click(this.fields.messageTab);
    await I.wait(3);
  },

  async verifyOpenMessageText() {
    await I.retry(3).seeElement(this.fields.openMessages);
  },

  async verifyClosedMessageText() {
    await I.retry(3).seeElement(this.fields.closedMessages);
  },

  async verifyMessageDetailsUnderMessageTab() {
    await I.retry(3).see(manageOrderDetails.messagesTab.sentTo);
    await I.retry(3).see(manageOrderDetails.messagesTab.urgency);
    await I.retry(3).see(manageOrderDetails.messagesTab.reasonForMessage);
    await I.retry(3).see(manageOrderDetails.messagesTab.message);
    await I.retry(3).see(manageOrderDetails.messagesTab.status);
    await I.retry(3).see('Judge');
    await I.retry(3).see('List a hearing');
    await I.retry(3).see('High');
    //await I.retry(3).see(Date.now());
    await I.retry(3).see(sendOrReplyToMessagesData.message);

  },
  async verifyMessageStatusClosed() {
    await I.retry(3).see('Closed');
  },
  async verifyMessageStatusOpen() {
    await I.retry(3).see('Open');
  },

}
