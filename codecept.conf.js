require('./src/e2e/helpers/event_listener');
const lodash = require('lodash');
exports.config = {
  output: './output',
  multiple: {
    parallel: {
      chunks: files => {
        const splitFiles = (list, size) => {
          const sets = [];
          const chunks = list.length / size;
          let i = 0;

          while (i < chunks) {
            sets[i] = list.splice(0, size);
            i++;
          }
          return sets;
        };

        const buckets = parseInt(process.env.PARALLEL_CHUNKS || '1');
        const slowTests = lodash.filter(files, file => file.includes('@slow'));
        const otherTests = lodash.difference(files, slowTests);

        let chunks = [];
        if (buckets > slowTests.length + 1) {
          const slowTestChunkSize = 1;
          const regularChunkSize = Math.ceil((files.length - slowTests.length) / (buckets - slowTests.length));
          chunks = lodash.union(splitFiles(slowTests, slowTestChunkSize), splitFiles(otherTests, regularChunkSize));
        } else {
          chunks = splitFiles(files, Math.ceil(files.length / buckets));
        }

        console.log(chunks);

        return chunks;
      },
    },
  },
  helpers: {
    Puppeteer: {
      show: process.env.SHOW_BROWSER_WINDOW || false,
      waitForTimeout: parseInt(process.env.WAIT_FOR_TIMEOUT || '20000'),
      chrome: {
        ignoreHTTPSErrors: true,
        args: process.env.DISABLE_WEB_SECURITY === 'true' ? ['--disable-web-security'] : [],
        devtools: process.env.SHOW_BROWSER_WINDOW || false,
      },
      windowSize: '1280x960',
    },
    HooksHelper: {
      require: './src/e2e/helpers/hooks_helper.js',
    },
    BrowserHelpers: {
      require: './src/e2e/helpers/browser_helper.js',
    },
    DumpBrowserLogsHelper: {
      require: './src/e2e/helpers/dump_browser_logs_helper.js',
    },
    StepListener: {
      require: './src/e2e/helpers/stepListener.js',
    },
    Mochawesome: {
      uniqueScreenshotNames: true,
    },
  },

  include: {
    config: './src/e2e/config.js',
    I: './src/e2e/actors/main.js',
    loginPage: './src/e2e/pages/login.page.js',
    openApplicationEventPage: './src/e2e/pages/events/openApplicationEvent.page.js',
    caseListPage: './src/e2e/pages/caseList.page.js',
    eventSummaryPage: './src/e2e/pages/eventSummary.page.js',
    caseViewPage: './src/e2e/pages/caseView.page.js',
    amendApplicationDetailsPage: './src/e2e/pages/amendApplicationDetails.page.js',
    transferCourtPage: './src/e2e/pages/transferCourt.page.js',
    addACaseNotePage: './src/e2e/pages/addACaseNote.page.js',

  },
  plugins: {
    retryFailedStep: {
      enabled: true,
    },
    screenshotOnFail: {
      enabled: true,
      fullPageScreenshots: true,
    },
  },
  tests: './src/e2e/tests/*_test.js',
  mocha: {
    reporterOptions: {
      'codeceptjs-cli-reporter': {
        stdout: '-',
        options: {
          steps: false,
        },
      },
      'mocha-junit-reporter': {
        stdout: '-',
        options: {
          mochaFile: 'test-results/result.xml',
        },
      },
      mochawesome: {
        stdout: '-',
        options: {
          reportDir: './output',
          inlineAssets: true,
          json: false,
        },
      },
      '../../src/e2e/reporters/json-file-reporter/reporter': {
        stdout: '-',
      },
    },
  },
};
