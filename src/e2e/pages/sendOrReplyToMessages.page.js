const sendOrReplyToMessagesData = require('../fixtures/sendOrReplyToMessagesDetails');
const {I} = inject();
let reasonType;
module.exports = {
  fields: {
    messageActionError: '#messageAction .error-message',
    messageActionSendMessage: '#messageAction-sendMessage',
    messageSendDetailsMessageReceiverRolesJudge: '#messageReceiverRoles-judge',
    messageReasonListDropDown: '#messageReasonList',
    messageUrgencyListDropDown: '#messageUrgencyList',
    sendMessageAttachDocumentNo: '#sendMessageAttachDocument_No',
    messageTextBox: '#messageText',
    messageActionReplyMessage: '#messageAction-replyMessage',
    replyMsgDynamicListDropDown: '#replyMsgDynamicList',
    replyMessageYes: '#replyMessage_Yes',

  },

  async sendMessageToJudge() {
    await I.retry(3).waitForText('Do you want to send or reply to a message?', 30);
    await I.retry(3).click('Continue');
    await I.retry(3).waitForText('Do you want to send or reply to a message? is required', 30);
    await I.retry(3).see('Do you want to send or reply to a message? is required', this.fields.messageActionError);
    await I.retry(3).click(this.fields.messageActionSendMessage);
    await I.retry(3).click('Continue');
    await this.whoDoYouSendMessage();

  },

  async verifyReplayMessageToJudge() {
    await I.retry(3).waitForText('Do you want to send or reply to a message?', 30);
    await I.retry(3).click(this.fields.messageActionReplyMessage);
    let urgency = locate('//select[@id="replyMsgDynamicList"]/option').at(2);
    let urgencyType = await I.grabTextFrom(urgency);
    await I.wait(3);
    await I.retry(3).selectOption(this.fields.replyMsgDynamicListDropDown, urgencyType);
    await I.retry(3).click('Continue');
    await I.wait(3);
    await I.retry(3).waitForText('Reply to a message', 30);
    await I.retry(3).click(this.fields.replyMessageYes);
    await I.retry(3).click('Continue');
    await I.wait(3);
    await this.whoDoYouSendMessage();

  },

  async whoDoYouSendMessage() {
    await I.retry(3).waitForText('Who do you want to send a message to?', 30);
    await I.retry(3).click(this.fields.messageSendDetailsMessageReceiverRolesJudge);
    let reason = locate('//select[@id="messageReasonList"]/option').at(2);
    reasonType = await I.grabTextFrom(reason);
    await I.wait(3);
    await I.retry(3).selectOption(this.fields.messageReasonListDropDown, reasonType);
    await I.wait(3);
    let urgency = locate('//select[@id="messageUrgencyList"]/option').at(2);
    let urgencyType = await I.grabTextFrom(urgency);
    await I.wait(3);
    await I.retry(3).selectOption(this.fields.messageUrgencyListDropDown, urgencyType);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.messageTextBox, sendOrReplyToMessagesData.message);
    await I.retry(3).click(this.fields.sendMessageAttachDocumentNo);
    await I.retry(3).click('Continue');
    await I.wait(3);
  },

  async verifySendMessageToJudgeCYA() {
    await I.retry(3).waitForText('Check your answers', 30);
    await I.retry(3).see('Send a message');
    await I.retry(3).see('Judge');
    await I.retry(3).see('List a hearing');
    await I.retry(3).see('High');
    await I.retry(3).see(sendOrReplyToMessagesData.message);
    await I.click('Save and continue');
    await I.wait(3);
  },
  async verifyReplyMessageToJudgeCYA() {
    await I.retry(3).waitForText('Check your answers', 30);
    await I.retry(3).see('Reply to a message');
    await I.retry(3).see('Judge');
    await I.retry(3).see('List a hearing');
    await I.retry(3).see('High');
    await I.retry(3).see(sendOrReplyToMessagesData.message);
    await I.click('Save and continue');
    await I.wait(3);
  },

};
