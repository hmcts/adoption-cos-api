const Axios = require('axios');
const otplib = require('otplib');
const https = require('https');
const utils = require('../utils/utils.js');
const cui = require('./create_cui_case.js');

const laPortalCase = async (caseId) => {

    const clientId = process.env.ADOPTION_WEB_CLIENT_ID;
    const s2sSecret = process.env.S2S_SECRET_WEB;
    const idamSecret = process.env.IDAM_CLIENT_SECRET;
    const url = process.env.CASE_DATA_STORE_BASEURL;
    const s2sUrl = process.env.TEST_S2S_URL;
    const idamUrl = process.env.IDAM_API_BASEURL;
    const oneTimePassword = otplib.authenticator.generate(s2sSecret);
    const callbackUrl = "http://localhost:3001/receiver";
    let s2sAuth;
  
    const agent = new https.Agent({  
      rejectUnauthorized: false
    });

    let newUser = {
        "forename": "User",
        "surname": "Test",
        "password": "AdoptionPassword@123",
        "roles": [
          { "code": "caseworker" },
          { "code": "caseworker-adoption-caseworker" },
          { "code": "caseworker-adoption" },
          { "code": "citizen" }
        ]
    };

    newUser.email = `adop-test.${Date.now()}@mailinator.com`;

    try {
        let s2sAuthRequest = await Axios.post(`${s2sUrl}/lease`, { "microservice": process.env.ADOPTION_WEB_MICROSERVICE, oneTimePassword }, { httpsAgent: agent });
        s2sAuth = s2sAuthRequest.data;
        let idamData = `username=${process.env.IDAM_SYSTEM_UPDATE_USERNAME}&password=${process.env.IDAM_SYSTEM_UPDATE_PASSWORD}&client_id=${clientId}&client_secret=${idamSecret}&grant_type=password&redirect_uri=${callbackUrl}&scope=openid%20profile%20roles`;
        let idamAccess = await Axios.post(`${idamUrl}/o/token`, idamData, { httpsAgent: agent });
        let idamToken = idamAccess.data.access_token;

        axiosClient = Axios.create({
            baseURL: url,
            headers: {
                Authorization: 'Bearer ' + idamToken,
                ServiceAuthorization: s2sAuth,
                experimental: 'true',
                Accept: '*/*',
                'Content-Type': 'application/json',
            },
            httpsAgent: new https.Agent({  
              rejectUnauthorized: false
            })
        });

        tokenRes = await axiosClient.get(`${url}/cases/${caseId}/event-triggers/local-authority-application-submit`);
        token = tokenRes.data.token;
        event = { id: "local-authority-application-submit" };
        const caseData = utils.caseDataLa;
        response = await axiosClient.post(`/cases/${caseId}/events`, {
            event,
            data: caseData,
            event_token: token,
        });
        return caseId;

    } catch (err) {
        console.log("could not create account");
    }

};

module.exports.createCompleteCase = async () => {
  const caseId = await cui.createCase();
  await laPortalCase(caseId);
  return caseId;
}
