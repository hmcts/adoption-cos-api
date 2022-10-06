const { I } = inject();
module.exports = {

  fields: {
    tabs: {
      summary: 'Summary',
      history: 'History',
    },
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

  async goToTab(actionSelector){
    await I.wait(3);
    await I.retry(3).click(actionSelector);
  }

};
