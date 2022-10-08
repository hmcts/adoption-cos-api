const { I } = inject();
const retryCount = 3;

const browserHelper = require('../helpers/browser_helper');

module.exports = {
  fields: {
    pageHeaderText: 'Amend case details',
    adoptionType: 'TEST PLACEMENT ORDER',
    adoptionTypeElement: '',
    dateSubmittedDay: '05',
    dateSubmittedMonth: '01',
    dateSubmittedYear: '2022',
    dateSubmittedDayElement: '',
    dateSubmittedMonthElement: '',
    dateSubmittedYearElement: '',
    dateChildMovedInDay: '01',
    dateChildMovedInMonth: '01',
    dateChildMovedInYear: '2021',
    dateChildMovedInDayElement: '',
    dateChildMovedInMonthElement: '',
    dateChildMovedInYearElement: '',
    dateSubmitted: '05 Jan 2022',
    dateChildMovedIn: '01 Jan 2022',
  },

  async updateCaseDetails(){
    await I.retry(retryCount).waitForText(this.fields.pageHeaderText, 30);
    await I.retry(retryCount).fillField(this.fields.adoptionTypeElement, this.fields.adoptionType);
    await I.retry(retryCount).fillField(this.fields.dateSubmittedDayElement, this.fields.dateSubmittedDay);
    await I.wait(2);
    await I.retry(retryCount).fillField(this.fields.dateSubmittedMonthElement, this.fields.dateSubmittedMonth);
    await I.wait(2);
    await I.retry(retryCount).fillField(this.fields.dateSubmittedYearElement, this.fields.dateSubmittedYear);
    await I.wait(2);
    await I.retry(retryCount).fillField(this.fields.dateChildMovedInDayElement, this.fields.dateChildMovedInDay);
    await I.wait(2);
    await I.retry(retryCount).fillField(this.fields.dateChildMovedInMonthElement, this.fields.dateChildMovedInMonth);
    await I.wait(2);
    await I.retry(retryCount).fillField(this.fields.dateChildMovedInYearElement, this.fields.dateChildMovedInYear);
    await I.wait(2);
    await I.retry(retryCount).click('Continue');
    await I.wait(retryCount);
    await I.retry(retryCount).waitForText('Check your answers', 30);
    await I.retry(retryCount).waitForText(this.fields.dateSubmitted, 30);
    await I.retry(retryCount).waitForText(this.fields.dateChildMovedIn, 30);
    await I.retry(retryCount).click('Save and continue');
    await I.wait('10');
  },

};
