const config = require('../config');
const {I} = inject();

const finalAdoptionOrderDetails = require('../fixtures/finalAdoptionOrderDetails.js');
const manageOrderDetails = require("../fixtures/manageOrderDetails.js");
const sendOrReplyToMessagesData = require("../fixtures/sendOrReplyToMessagesDetails");
const manageHearingFormData = require("../fixtures/manageHearings");
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

  async verifyOpenMessageDetailsUnderMessageTab() {
    await I.retry(3).see(manageOrderDetails.messagesTab.sentTo);
    await I.retry(3).see(manageOrderDetails.messagesTab.urgency);
    await I.retry(3).see(manageOrderDetails.messagesTab.reasonForMessage);
    await I.retry(3).see(manageOrderDetails.messagesTab.message);
    await I.retry(3).see(manageOrderDetails.messagesTab.status);
    await I.retry(3).see('Judge');
    await I.retry(3).see('List a hearing');
    await I.retry(3).see('High');
    await I.retry(3).see(sendOrReplyToMessagesData.message);
    await I.retry(3).seeTextInTab(['Open messages 1', 'From'], config.caseWorkerUserOne.email);
    await I.retry(3).seeTextInTab(['Open messages 1', 'Sent to'], 'Judge');
    await I.retry(3).seeTextInTab(['Open messages 1', 'Urgency'], 'High');
    await I.retry(3).seeTextInTab(['Open messages 1', 'Reason for message'], 'List a hearing');
    await I.retry(3).seeTextInTab(['Open messages 1', 'Message'], sendOrReplyToMessagesData.message);
    await I.retry(3).seeTextInTab(['Open messages 1', 'Status'], 'Open');
  },
  async verifyMessageStatusClosed() {
    await I.retry(3).see('Closed');
  },
  async verifyMessageStatusOpen() {
    await I.retry(3).see('Open');
  },

}
