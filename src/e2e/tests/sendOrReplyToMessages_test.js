const config = require('../config');
const laHelper = require('../helpers/la_portal_case');

let caseId;
Feature('Send or reply to messages Details').retry(1);
async function setupScenario(I) {
  if (!caseId) {
    caseId = await laHelper.createCompleteCase();
    console.log('CCD Case number - '+ caseId);
  }
  await I.navigateToCaseDetailsAs(config.caseWorkerUserOne, caseId);
}
Scenario('Verify send and reply message to judge Open Closed Message on message tab', async ({I, caseViewPage,sendOrReplyToMessagesPage, messageTabPage }) => {
  // await setupScenario(I);
  // await caseViewPage.goToNewActions(config.administrationActions.sendOrReplyToMessages);
  // await sendOrReplyToMessagesPage.sendMessageToJudge();
  // await sendOrReplyToMessagesPage.verifySendMessageToJudgeCYA();
  // await I.seeEventSubmissionConfirmation(caseId,config.administrationActions.sendOrReplyToMessages);
  // await caseViewPage.goToNewActions(config.administrationActions.sendOrReplyToMessages);
  // await sendOrReplyToMessagesPage.verifyReplayMessageToJudge();
  // await sendOrReplyToMessagesPage.verifyReplyMessageToJudgeCYA();
  // await I.seeEventSubmissionConfirmation(caseId,config.administrationActions.sendOrReplyToMessages);

  // await messageTabPage.selectMessageTab();
  // await messageTabPage.verifyOpenMessageText();
  // await messageTabPage.verifyOpenMessageDetailsUnderMessageTab();
  // await messageTabPage.verifyMessageStatusOpen();

  // await caseViewPage.goToNewActions(config.administrationActions.sendOrReplyToMessages);
  // await sendOrReplyToMessagesPage.replyToMessageNo();
  // await sendOrReplyToMessagesPage.verifyMessageStatusNoCYA();
  // await sendOrReplyToMessagesPage.verifyReplyMessageToJudgeCYA();

  // await messageTabPage.selectMessageTab();
  // await messageTabPage.verifyClosedMessageText();
  // await messageTabPage.verifyMessageStatusClosed();

});
