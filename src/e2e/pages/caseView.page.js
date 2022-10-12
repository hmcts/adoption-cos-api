const { I } = inject();
module.exports = {

  fields: {
    actionsDropdown: '#next-step',
    goButton: 'Go',
  },

  async goToNewActions(actionSelected) {
    const currentUrl = await I.grabCurrentUrl();
    await I.retryUntilExists(async () => {
      if(await I.waitForSelector(this.fields.actionsDropdown, 30) != null) {
        await I.scrollToElement(this.fields.actionsDropdown);
        I.selectOption(this.fields.actionsDropdown, actionSelected);
        I.click(this.fields.goButton);
      } else {
        const newUrl = await I.grabCurrentUrl();
        if(newUrl === currentUrl || !newUrl.includes('http')){
          console.log('Page refresh');
          I.refreshPage();
        }
      }
    }, 'ccd-case-event-trigger', false);
  },

  async navigateToTab(tabName){
    await I.retry(retryCount).selectTab(tabName);
    await I.wait(3);
  },

  async verifySuccessBanner(caseID, actionName){
    await I.retry(retryCount).seeEventSubmissionConfirmation(caseID,actionName);
  },

  async verifyTableDataInsideTab(tableTitleRowNameArray, rowValue){
    await I.retry(retryCount).seeInTab(tableTitleRowNameArray, rowValue);
  },

};
