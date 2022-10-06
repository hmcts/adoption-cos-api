const {I} = inject();
const config = require('../config');

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

  searchForCasesWithHypernisedId(caseId, state = 'Any') {
    const hyphanisedCaseId = caseId.replace(/(.{4})/g,"$1-").substring(0,19);
    console.log('hyphanisedCaseId '+ hyphanisedCaseId);
    this.setInitialSearchFields(state);
    I.wait(5);
    I.fillField(this.fields.caseNumber, hyphanisedCaseId);
    I.grabCurrentUrl();
    I.click(this.fields.search);
    I.wait(5);
  },
  seeCaseInSearchResult(caseId) {
    I.waitForElement(`//a[contains(@href,'/cases/case-details/${caseId}')]`);
    I.grabCurrentUrl();
    I.seeElement(this.locateCase(caseId));
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
  clickOnCaseId(caseId){
    console.log('clicking on case ID');
    I.click(`//a[contains(@href,'/cases/case-details/${caseId}')]`)
    I.waitForInvisible(this.fields.spinner, 30);
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
