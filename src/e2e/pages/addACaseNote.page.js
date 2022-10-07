const { I } = inject();
const retryCount = 3;

const browserHelper = require('../helpers/browser_helper');

module.exports = {
  fields: {
    addACaseNoteHeader: 'h1.govuk-heading-l.ng-star-inserted',
    addACaseNoteHeaderText: 'Add a case note',
    subjectField: '#note_subject',
    noteField: 'textarea#note_note',
    tableTitleName: 'Notes 1',
    tableCaseNoteRowName: 'Case note',
    tableCaseNoteSubjectName: 'Subject',
    subjectText: 'Test subject',
    noteText: 'This is the test note for this case',
  },

  async addACaseNote(){
    await I.retry(retryCount).waitForText(this.fields.addACaseNoteHeaderText, 30);
    await I.retry(retryCount).fillField(this.fields.subjectField, this.fields.subjectText);
    await I.retry(retryCount).fillField(this.fields.noteField, this.fields.noteText);
    await I.retry(retryCount).click('Continue');
    await I.wait(retryCount);
    await I.retry(retryCount).waitForText('Check your answers', 30);
    await I.retry(retryCount).waitForText(this.fields.subjectText, 30);
    await I.retry(retryCount).waitForText(this.fields.noteText, 30);
    await I.retry(retryCount).click('Save and continue');
    await I.wait('10');
  },

};
