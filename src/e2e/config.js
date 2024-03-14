module.exports = {
  caseWorkerUserOne: {
    email: process.env.CASEWORKER_USERNAME,
    password: process.env.CASEWORKER_PASSWORD,
  },
  hmctsUser: {
    email: process.env.HMCTS_USER_USERNAME,
    password: process.env.HMCTS_USER_PASSWORD,
  },

  baseUrl: process.env.CASE_API_URL || 'https://manage-case.aat.platform.hmcts.net/',

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
    manageOrders: 'Manage orders',
    allocateJudge: 'Allocate judge',
    transferCourt: 'Transfer Court',
    manageHearings: 'Manage hearings',
    seekFurtherInfo: 'Seek further information',
    amendOtherPartiesDetails: 'Amend other parties details',
    checkAndSendOrders: 'Check and send orders',
    generalDirectionOrders: 'General directions order',
    sendOrReplyToMessages:  'Send or reply to messages',
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
