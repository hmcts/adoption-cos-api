const { I } = inject();
const retryCount = 3;

module.exports = {
  fields: {
    pageHeaderText: 'Amend case details',
    adoptionType: 'TEST PLACEMENT ORDER',
    adoptionTypeElement: '#typeOfAdoption',
    dateSubmittedDay: '05',
    dateSubmittedMonth: '01',
    dateSubmittedYear: '2022',
    dateSubmittedDayElement: '#dateSubmitted-day',
    dateSubmittedMonthElement: '#dateSubmitted-month',
    dateSubmittedYearElement: '#dateSubmitted-year',
    dateChildMovedInDay: '01',
    dateChildMovedInMonth: '01',
    dateChildMovedInYear: '2021',
    dateChildMovedInDayElement: '#dateChildMovedIn-day',
    dateChildMovedInMonthElement: '#dateChildMovedIn-month',
    dateChildMovedInYearElement: 'dateChildMovedIn-year',
    dateSubmitted: '5 Jan 2022',
    dateChildMovedIn: '1 Jan 2021',
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
    await I.wait(5);
    await I.retry(retryCount).waitForText('Check your answers', 5);
    await I.retry(retryCount).waitForText(this.fields.adoptionType, 5);
    await I.retry(retryCount).waitForText(this.fields.dateSubmitted, 5);
    await I.retry(retryCount).waitForText(this.fields.dateChildMovedIn, 5);
    await I.retry(retryCount).click('Save and continue');
    await I.wait('5');
  },

};
