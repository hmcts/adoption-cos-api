const { I } = inject();
const manageOrderDetails = require('../fixtures/manageOrderDetails.js');
const checkAndSendOrderDetails = require('../fixtures/checkAndSendOrderDetails.js');
const finalAdoptionOrderDetails = require("../fixtures/finalAdoptionOrderDetails.js");
const sendOrReplyToMessagesData = require("../fixtures/sendOrReplyToMessagesDetails");

module.exports = {

  fields: {
    pageTitle: '//h1[text()="Check and send orders"]',
    childName: '//h3[text()="Child\'s name: child child"]',
    ordersToReviewTitle: '//h2[text()="Orders for review"]',
    ordersToReviewSubTitle: '//span[text()="Select the order you want to review"]',
    continueButton: '//button[@type="submit"]',
    errorMessage: '[field_id=\'checkAndSendOrderDropdownList\'] .error-message',
    cancelButton: '//a[text()="Cancel"]',
    ordersDropDown: '#checkAndSendOrderDropdownList',
    orderToSelect: '//option[starts-with(@value, "1")]',
    alertMessage: '//div[@class="alert-message"]',
    previewOrderDocmosisLink: '//a[contains(text(),"A76_Final adoption order_draft.pdf")]',
    checkAndSendOrderErrorMessage: '#orderCheckAndSend .error-message',
    serveOrderRadioBtn: '#orderCheckAndSend-serveTheOrder',
    returnForAmendments: '#orderCheckAndSend-returnForAmendments',
    whomToSendMessageError: '#messageReceiverRoles .error-message',
    reasonForMessageIsRequiredError: '[field_id="messageReasonList"] .error-message',
    urgencyIsRequiredError: '[field_id="messageUrgencyList"] .error-message',
    attachDocumentError: '[field_id="sendMessageAttachDocument"] .error-message',
    messageRequiredError: '[field_id="messageText"] .error-message'
  },

  async verifyCheckAndSendOrdersPageDetails() {
    await I.wait(3);
    await I.retry(3).seeElement(this.fields.pageTitle);
    //await I.retry(3).seeElement(this.fields.childName);
    await I.retry(3).seeElement(this.fields.ordersToReviewTitle);
    await I.retry(3).seeElement(this.fields.ordersToReviewSubTitle);
    await I.click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).seeElement(this.fields.errorMessage);

    let delivery = locate('//select[@id="checkAndSendOrderDropdownList"]/option').at(2);
    orderType = await I.grabTextFrom(delivery);
    await I.wait(3);
    await I.retry(3).selectOption(this.fields.ordersDropDown, orderType);
    await I.click(this.fields.continueButton);
    await I.wait(5);
  },

  async verifyOrderForReview() {
    await I.wait(3);
    await I.retry(3).see('Review order');
    await I.retry(3).see(checkAndSendOrderDetails.documentsToReview);
  },

  async verifyOrderTypeCYA(){
    await I.retry(3).waitForText('Check your answers', 30);
    await I.retry(3).see('Check your answers');
    await I.retry().see(orderType)
  },

  async serverOrderCYA(){
    await I.retry(3).see(checkAndSendOrderDetails.selectOrderToreview);
    await I.retry(3).see(checkAndSendOrderDetails.serveOrderOrAmendment)
    await I.retry(3).see(checkAndSendOrderDetails.serverTheOrder);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  },


  async gateKeepingOrderRecipients(){
    await I.retry(3).see(checkAndSendOrderDetails.selectedRecipientsToServerOrder);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.applicantLocalAuthority);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.birthMother);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.birthFather);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.applicants);
    await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.childLocalAuthority);
   // await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.otherAdoptionAgency);
   // await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.otherPersonWithParentalResponsibility);
    // await I.retry(3).see(manageOrderDetails.caseManagementOrderDetails.cafCass);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  },

  async selectServerOrder() {
    await I.wait(3);
    await I.retry().see(checkAndSendOrderDetails.serveOrderOrAmendment)
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see(checkAndSendOrderDetails.serverOrderError, this.fields.checkAndSendOrderErrorMessage);
    await I.retry(3).click(this.fields.serveOrderRadioBtn);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
  },

  async FinalAdoptionOrderRecipients() {
    await I.retry(3).seeElement(this.fields.previewOrderDocmosisLink);
    await I.retry(3).see(checkAndSendOrderDetails.selectedRecipientsToServerOrder);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.firstApplicant);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.secondApplicant);
    //await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.birthFather);
    //await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.birthMother);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.childLocalAuthority);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.applicantLocalAuthority);
    await I.retry(3).see(finalAdoptionOrderDetails.checkYourAnswers.adoptionAgency);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);

  },

  async selectReturnForAmendments(){
    await I.retry(3).click(this.fields.returnForAmendments);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(8);
  },

  async sendAMessage(){
    await I.retry(3).see(checkAndSendOrderDetails.sendMessage);
    await I.retry(3).click(this.fields.continueButton);
    await I.wait(3);
    await I.retry(3).see(checkAndSendOrderDetails.whoDoYouWantSendMessageTo);
    await I.retry(3).see(checkAndSendOrderDetails.whoDoYouWantSendMessageToError, this.fields.whomToSendMessageError);
    await I.retry(3).see(checkAndSendOrderDetails.selectAReasonForMessageError, this.fields.reasonForMessageIsRequiredError);
    await I.retry(3).see(checkAndSendOrderDetails.urgencyIsRequired, this.fields.urgencyIsRequiredError);
    await I.retry(3).see(checkAndSendOrderDetails.doYouWantToAttachDocumentForThisCaseError, this.fields.attachDocumentError);
    await I.retry(3).see(checkAndSendOrderDetails.messageIsRequiredError, this.fields.messageRequiredError)
  },

  async verifyReturnForAmendmentsCYA(){
    await I.retry(3).waitForText('Check your answers', 30);
    await I.retry(3).see('Return for amendments');
    await I.retry(3).see('Judge');
    await I.retry(3).see('Refer for gatekeeping');
    await I.retry(3).see('High');
    await I.retry(3).see(sendOrReplyToMessagesData.message);
    await I.click('Save and continue');
    await I.wait(3);
  },

};
