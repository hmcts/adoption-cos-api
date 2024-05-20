interface UserCredentials {
  email: string;
  password: string;
}

interface Definition {
  jurisdiction: string;
  jurisdictionFullDesc: string;
  caseType: string;
  caseTypeFullDesc: string;
}

interface AdministrationActions {
  [key: string]: string;
}

interface Tabs {
  [key: string]: string;
}

interface Config {
  caseWorkerUserOne: UserCredentials;
  hmctsUser: UserCredentials;
  baseUrl: string;
  definition: Definition;
  administrationActions: AdministrationActions;
  tabs: Tabs;
  testFile: string;
  testPdfFile: string;
  testWordFile: string;
}

const config: Config = {
  caseWorkerUserOne: {
    email: process.env.CASEWORKER_USERNAME || '',
    password: process.env.CASEWORKER_PASSWORD || '',
  },
  hmctsUser: {
    email: process.env.HMCTS_USER_USERNAME || '',
    password: process.env.HMCTS_USER_PASSWORD || '',
  },
  baseUrl: process.env.CASE_API_URL || 'https://adoption-web-staging.service.core-compute-aat.internal/',
  definition: {
    jurisdiction: 'Adoption',
    jurisdictionFullDesc: 'Adoption',
    caseType: 'New Adoption case',
    caseTypeFullDesc: 'New Adoption case',
  },
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
  tabs: {
    historyTab: 'History',
    summaryTab: 'Summary',
    applicantsTab: 'Applicants',
    otherPartiesTab: 'Other Parties',
    documentsTab: 'Documents',
    correspondenceTab: 'Correspondence',
    notesTab: 'Notes',
  },
  testFile: './src',
  testPdfFile: './src/test/e2e/fixtures/testFiles/mockFile.pdf',
  testWordFile: './src/test/e2e/fixtures/testFiles/mockFile.docx',
};

export default config;
