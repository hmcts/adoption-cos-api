const sendOrReplyToMessagesData = require('../fixtures/sendOrReplyToMessagesDetails');
const { I } = inject();
module.exports = {
  fields: {
    messageActionError: '#messageAction .error-message',
    messageActionSendMessage: '#messageAction-sendMessage',
    messageSendDetailsMessageReceiverRolesJudge: '#messageSendDetails_messageReceiverRoles-judge',
    messageReasonListDropDown: '#messageSendDetails_messageReasonList',
    messageUrgencyListDropDown: '#messageSendDetails_messageUrgencyList',
    messageTextBox: '#messageSendDetails_message',
    sendMessageAttachDocumentNo: '#sendMessageAttachDocument_No',
  },

  async sendMessageToJudge() {
    await I.retry(3).waitForText('Do you want to send or reply to a message?',30);
    await I.retry(3).click('Continue');
    await I.retry(3).waitForText('Do you want to send or reply to a message? is required',30);
    await I.retry(3).see('Do you want to send or reply to a message? is required',this.fields.messageActionError);
    await I.retry(3).click(this.fields.messageActionSendMessage);
    await I.retry(3).click('Continue');
    await I.retry(3).waitForText('Who do you want to send a message to?',30);
    await I.retry(3).click(this.fields.messageSendDetailsMessageReceiverRolesJudge);
    let reason = locate('//select[@id="messageSendDetails_messageReasonList"]/option').at(2);
    let reasonType = await I.grabTextFrom(reason);
    await I.wait(3);
    await I.retry(3).selectOption(this.fields.messageReasonListDropDown, reasonType);
    await I.wait(3);
    let urgency = locate('//select[@id="messageSendDetails_messageUrgencyList"]/option').at(2);
    let urgencyType = await I.grabTextFrom(urgency);
    await I.wait(3);
    await I.retry(3).selectOption(this.fields.messageUrgencyListDropDown, urgencyType);
    await I.wait(3);
    await I.retry(3).fillField(this.fields.messageTextBox,sendOrReplyToMessagesData.message);
    await I.retry(3).click(this.fields.sendMessageAttachDocumentNo);
    await I.retry(3).click('Continue');
    await I.wait(3);

  },

  async verifySendMessageToJudgeCYA(){
    await I.retry(3).waitForText('Check your answers', 30);
    await I.retry(3).see('Send a message');
    await I.retry(3).see('Judge');
    await I.retry(3).see('List a hearing');
    await I.retry(3).see('High');
    await I.retry(3).see(sendOrReplyToMessagesData.message);
    await I.click('Save and continue');
    await I.wait(3);
  },

};
