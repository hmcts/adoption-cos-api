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
Scenario('Verify send message to judge', async ({I, caseViewPage,sendOrReplyToMessagesPage }) => {
  await setupScenario(I);
  await caseViewPage.goToNewActions(config.administrationActions.sendOrReplyToMessages);
  await sendOrReplyToMessagesPage.sendMessageToJudge();
  await sendOrReplyToMessagesPage.verifySendMessageToJudgeCYA();
  await I.seeEventSubmissionConfirmation(caseId,config.administrationActions.sendOrReplyToMessages);

});
