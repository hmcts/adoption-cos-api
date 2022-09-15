const Axios = require('axios');
const otplib = require('otplib');
const utils = require('../utils/utils.js');

module.exports.createCase = async () => {

    const clientId = process.env.ADOPTION_WEB_CLIENT_ID;
    const s2sSecret = process.env.S2S_SECRET_WEB;
    const idamSecret = process.env.IDAM_CLIENT_SECRET;
    const url = process.env.CASE_DATA_STORE_BASEURL;
    const s2sUrl = process.env.TEST_S2S_URL;
    const idamUrl = process.env.IDAM_API_BASEURL;
    const oneTimePassword = otplib.authenticator.generate(s2sSecret);
    const callbackUrl = "http://localhost:3001/receiver";
    let s2sAuth;
    let caseId;

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
        let s2sAuthRequest = await Axios.post(`${s2sUrl}/lease`, { "microservice": process.env.ADOPTION_WEB_MICROSERVICE, "oneTimePassword": oneTimePassword });
        s2sAuth = s2sAuthRequest.data;
        let createdAccount = await Axios.post(`${idamUrl}/testing-support/accounts`, newUser);
        let idamData = `username=${newUser.email}&password=${newUser.password}&client_id=${clientId}&client_secret=${idamSecret}&grant_type=password&redirect_uri=${callbackUrl}&scope=openid%20profile%20roles`;
        let idamAccess = await Axios.post(`${idamUrl}/o/token`, idamData);
        let idamToken = idamAccess.data.access_token;

        let axiosClient = Axios.create({
            baseURL: url,
            headers: {
                Authorization: 'Bearer ' + idamToken,
                ServiceAuthorization: s2sAuth,
                experimental: 'true',
                Accept: '*/*',
                'Content-Type': 'application/json',
            },
        });

        let event = { id: "citizen-create-application" };
        data = {
            applicant1FirstName: "Will",
            applicant1LastName: "Smith",
            applicant1Email: "willsmith@mailinator.com",
        };

        tokenRes = await axiosClient.get(`/case-types/A58/event-triggers/citizen-create-application`);
        token = tokenRes.data.token;
        event = { id: "citizen-create-application" };
        response = await axiosClient.post(`/case-types/A58/cases`, {
            event,
            data,
            event_token: token,
        });
        caseId = response.data.id;

        tokenRes = await axiosClient.get(`${url}/cases/${caseId}/event-triggers/citizen-submit-application`);
        token = tokenRes.data.token;
        event = { id: "citizen-submit-application" };
        data = utils.data;
        response = await axiosClient.post(`/cases/${caseId}/events`, {
            event,
            data,
            event_token: token,
        });

        tokenRes = await axiosClient.get(`${url}/cases/${caseId}/event-triggers/citizen-add-payment`);
        token = tokenRes.data.token;
        event = { id: "citizen-add-payment" };
        data = utils.data;
        response = await axiosClient.post(`/cases/${caseId}/events`, {
            event,
            data,
            event_token: token,
        });
        console.log(caseId)
        return caseId;

    } catch (err) {
        console.log("could not create account", err);
    }

};
