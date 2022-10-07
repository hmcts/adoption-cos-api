module.exports = {
  caseWorkerUserOne: {
    email: process.env.CASEWORKER_USERNAME,
    password: process.env.CASEWORKER_PASSWORD,
  },
  hmctsUser: {
    email: process.env.HMCTS_USER_USERNAME,
    password: process.env.HMCTS_USER_PASSWORD,
  },

  baseUrl: process.env.CASE_API_URL || 'https://adoption-web-staging.service.core-compute-aat.internal/',

  definition: {
    jurisdiction: 'Adoption',
    jurisdictionFullDesc: 'Adoption',
    caseType: 'New Adoption case',
    caseTypeFullDesc: 'New Adoption case',
  },

  // actions
  administrationActions: {
    amendCaseDetails: 'Amend case details',
    amendApplicantDetails: 'Amend applicant details',
    transferCourt: 'Transfer Court',
    addACaseNote: 'Add a case note',

  },

  // tabs
  tabs: {
    historyTab: 'History',
    summaryTab: 'Summary',
    applicantsTab: 'Applicants',
    otherPartiesTab: 'Other Parties',
    documentsTab: 'Documents',
    correspondenceTab: 'Correspondence',
    notesTab: 'Notes',
  },

  // files
  testFile: './src/test/e2e/fixtures/testFiles/mockFile.txt',
  testPdfFile: './src/test/e2e/fixtures/testFiles/mockFile.pdf',
  testWordFile: './src/test/e2e/fixtures/testFiles/mockFile.docx',
};
