const { I } = inject();
const retryCount = 3;

module.exports = {
  fields: {
    addACaseNoteHeader: 'h1.govuk-heading-l.ng-star-inserted',
    addACaseNoteHeaderText: 'Add a case note',
    subjectField: 'note_subject',
    noteField: 'note_note',
  },

  async verifyAddACaseNotePageLoaded(){

  }


  // await I.retry(retryCount).click('Continue');
};
