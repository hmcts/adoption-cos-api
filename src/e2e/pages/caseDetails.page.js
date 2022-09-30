const {I} = inject();
const config = require('../config');

module.exports = {

  fields: {
      caseSummary: '#mat-tab-label-0-1',
       otherPartiesTab: '//*[@role="tab"]/div[text() = "Other Parties"]',
  },

  clickOnSummaryPage() {
        console.log('clicking on summary tab');
        I.waitForElement(this.fields.caseSummary, 30);
        I.click(this.fields.caseSummary);
        I.waitForInvisible(this.fields.spinner, 30);
      },

      clickOnOtherPartiesTab(){
      console.log('clicking on Other parties tab');
        I.waitForElement(this.fields.otherPartiesTab, 30);
        I.click(this.fields.otherPartiesTab);
        I.waitForInvisible(this.fields.spinner, 30);
      }

  };
