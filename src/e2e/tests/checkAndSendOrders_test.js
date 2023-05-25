const config = require('../config');

const laHelper = require('../helpers/la_portal_case');
const manageOrderDetails = require('../fixtures/manageOrderDetails.js');

let caseId;

Feature('Manage order tests').retry(1);

async function setupScenario(I) {
  caseId = await laHelper.createCompleteCase();
  console.log('CCD Case number - '+ caseId);
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}
Scenario('Verify send message for check and send gate keeping orders', async ({I, caseViewPage, manageOrdersPage, checkAndSendOrdersPage, sendOrReplyToMessagesPage,messageTabPage }) => {

     await setupScenario(I);
     await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
     await manageOrdersPage.selectCreateNewOrder();
     await manageOrdersPage.verifyTypeOfOrdersListed();
     await manageOrdersPage.selectCaseManagementOrderAndVerify(caseId);
     await manageOrdersPage.addDateOrderMade();
     await manageOrdersPage.caseManagementOrderAddAdditionalParagraph();
     await manageOrdersPage.caseManagementOrderServeParties();
     await manageOrdersPage.caseManagementOrderDateOrderMadeCYAPage();
     await manageOrdersPage.caseManagementOrderServePartiesCYAPage();
     await I.retry(3).seeEventSubmissionConfirmation(caseId,config.administrationActions.manageOrders);

     await caseViewPage.goToNewActions(config.administrationActions.checkAndSendOrders);
     await checkAndSendOrdersPage.verifyCheckAndSendOrdersPageDetails();
     await checkAndSendOrdersPage.verifyOrderForReview();
     await checkAndSendOrdersPage.gateKeepingOrderRecipients();
     await checkAndSendOrdersPage.selectReturnForAmendments();
     await checkAndSendOrdersPage.sendAMessage();
     await sendOrReplyToMessagesPage.whoDoYouSendMessage();
     await checkAndSendOrdersPage.verifyReturnForAmendmentsCYA();
     await messageTabPage.selectMessageTab();
     await messageTabPage.verifyOpenMessageDetailsUnderMessageTab();
     await messageTabPage.verifyMessageStatusOpen();


});

Scenario('Verify final adoption order return for amendments  Check and Send Orders', async ({I, caseViewPage, manageOrdersPage, finalOrderPage, checkAndSendOrdersPage,sendOrReplyToMessagesPage,messageTabPage}) => {
     await setupScenario(I);
     await caseViewPage.goToNewActions(config.administrationActions.manageOrders);
     await manageOrdersPage.verifyCaseDetails();
     await manageOrdersPage.selectCreateNewOrder();
     await manageOrdersPage.verifyTypeOfOrdersListed();
     await finalOrderPage.selectFinalOrderAndVerify();
     await finalOrderPage.addJudgeNameChildAdoptionNameAndDateOrderMade()
     await finalOrderPage.verifyFAODateAndPlaceOfBirth();
     await finalOrderPage.verifyFinalAdoptionRegistrationDetails();
     await finalOrderPage.verifyFinalAdoptionOrderRecipients();
     await finalOrderPage.verifyOrderPreviewScreen();
     await finalOrderPage.finalAdoptionOrderManagementDateOrderMadeCYAPage();
     await finalOrderPage.verifyFinalAdoptionOrderRecipientsCYA();
     await I.retry(3).seeEventSubmissionConfirmation(caseId, config.administrationActions.manageOrders);


     await caseViewPage.goToNewActions(config.administrationActions.checkAndSendOrders);
     await checkAndSendOrdersPage.verifyCheckAndSendOrdersPageDetails();
     await checkAndSendOrdersPage.verifyOrderForReview();
     await checkAndSendOrdersPage.FinalAdoptionOrderRecipients();
     await checkAndSendOrdersPage.selectReturnForAmendments();
     await checkAndSendOrdersPage.sendAMessage();
     await sendOrReplyToMessagesPage.whoDoYouSendMessage();
     await checkAndSendOrdersPage.verifyReturnForAmendmentsCYA();
     await messageTabPage.selectMessageTab();
     await messageTabPage.verifyOpenMessageDetailsUnderMessageTab();
     await messageTabPage.verifyMessageStatusOpen();


});

