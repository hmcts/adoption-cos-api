const {I} = inject();
const config = require('../config');
let hyphanisedCaseId;

module.exports = {

  fields: {
    jurisdiction: '#wb-jurisdiction',
    caseType: '#wb-case-type',
    caseState: '#wb-case-state',
    evidenceHandled: '#evidenceHandled_Yes',
    evidenceNotHandled: '#evidenceHandled_No',
    caseNumber: '#hyphenatedCaseRef',
    childrenFirstName: '#childrenFirstName',
    childrenLastName: '#childrenLastName',
    search: 'Apply',
    caseList: 'Case list',
    spinner: 'xuilib-loading-spinner',
    firstCaseInCaseList: 'tr:nth-child(1) > td:nth-child(1) > a > ccd-field-read > div > ccd-field-read-label > div > ccd-read-text-field > span',
    historyTab: '//div[contains(text(), "History")]',
    summaryTab: '//div[contains(text(), "Summary")]',
    applicantsTab: '//div[contains(text(), "Applicants")]',
    opTab: '//div[contains(text(), "Other parties")]',
    docsTab: '//div[contains(text(), "Documents")]',
    paymentsTab: '//div[contains(text(), "Payment")]',
  },

  navigate() {
    I.click(this.fields.caseList);
  },

  changeStateFilter(desiredState) {
    this.setInitialSearchFields(desiredState);
    I.click(this.fields.search);
  },

  searchForCasesWithHandledEvidences(caseId, state = 'Any') {
    this.setInitialSearchFields(state);
    I.waitForElement(this.fields.evidenceHandled, 30);
    I.fillField(this.fields.caseNumber, caseId);
    I.click(this.fields.evidenceHandled);
    I.click(this.fields.search);
  },

  searchForCasesWithId(caseId, state = 'Any') {
    this.setInitialSearchFields(state);
    I.wait(5);
    I.fillField(this.fields.caseNumber, caseId);
    I.grabCurrentUrl();
    I.click(this.fields.search);
    I.wait(5);
  },

  async searchForCasesWithHypernisedId(state = 'Any') {
    this.setInitialSearchFields(state);
    hyphanisedCaseId = await I.grabTextFrom(this.fields.firstCaseInCaseList);
    I.wait(5);
    I.fillField(this.fields.caseNumber, hyphanisedCaseId);
    I.grabCurrentUrl();
    I.click(this.fields.search);
    I.wait(5);
  },

  async seeCaseInSearchResult() {
    await I.waitForElement(`//a[contains(@aria-label,'${hyphanisedCaseId}')]`);
    await I.click(`//a[contains(@aria-label,'${hyphanisedCaseId}')]`);
    await I.see(`#${hyphanisedCaseId}`);
  },

  async seeExpectedTabsOnTheCase() {
    eleVisible = [this.fields.historyTab, this.fields.summaryTab, this.fields.applicantsTab, this.fields.opTab,
                  this.fields.docsTab, this.fields.paymentsTab]
    eleVisible.forEach(async function(ele) {
      await I.seeElement(ele);
    });
  },
  
  searchForCasesWithUnhandledEvidences() {
    I.click(this.fields.evidenceNotHandled);
    I.click(this.fields.search);
  },

  searchForCasesWithChildFirstNameAndWithId(caseId,childrenFirstName, state = 'Any') {
    this.setInitialSearchFields(state);
    // wait for our filters to load
    I.waitForVisible(this.fields.caseId, 30);
    I.fillField(this.fields.caseId, caseId);
    I.waitForVisible(this.fields.childrenFirstName, 30);
    I.fillField(this.fields.childrenFirstName, childrenFirstName);
    I.click(this.fields.search);
  },

  searchForCasesWithChildLastNameAndWithId(caseId,childrenLastName, state = 'Any') {
    this.setInitialSearchFields(state);
    // wait for our filters to load
    I.waitForVisible(this.fields.caseId, 30);
    I.fillField(this.fields.caseId, caseId);
    I.waitForVisible(this.fields.childrenLastName, 30);
    I.fillField(this.fields.childrenLastName, childrenLastName);
    I.click(this.fields.search);
  },

  setInitialSearchFields(state = 'Any') {
    // wait for initial filters to load
    I.waitForVisible(this.fields.jurisdiction, 30);
    I.selectOption(this.fields.jurisdiction, config.definition.jurisdictionFullDesc);
    I.selectOption(this.fields.caseType, config.definition.caseTypeFullDesc);
    I.selectOption(this.fields.caseState, state);
  },

  locateCase(caseId) {
    return `a[href$='${caseId}']`;
  },

  locateCaseProperty(caseId, columnNumber) {
    const caseRow = this.locateCase(caseId);
    const caseProperty = locate(`//td[${columnNumber}]`);
    return caseProperty.inside(caseRow);
  },

  async verifyCaseIsShareable(caseId) {
    I.navigateToCaseList();
    await I.retryUntilExists(() => this.searchForCasesWithId(caseId), this.locateCase(caseId), false);
    I.seeElement(`#select-${caseId}:not(:disabled)`);
  },

  verifyCaseIsNotAccessible(caseId) {
    I.navigateToCaseList();
    I.grabCurrentUrl();
    this.searchForCasesWithId(caseId);
    I.waitForInvisible(this.fields.spinner, 30);
    I.grabCurrentUrl();
    I.see('No cases found. Try using different filters.');
  },

};
