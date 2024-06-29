import Axios from 'axios';
import otplib from 'otplib';
import https from 'https';
import utils from '../utils/utils';

interface NewUser {
    forename: string;
    surname: string;
    password: string;
    roles: { code: string }[];
    email?: string;
}

export const createCase = async (): Promise<string | undefined> => {

    const clientId = process.env.ADOPTION_WEB_CLIENT_ID as string;
    const s2sSecret = process.env.S2S_SECRET_WEB as string;
    const idamSecret = process.env.IDAM_CLIENT_SECRET as string;
    const url = process.env.CASE_DATA_STORE_BASEURL as string;
    const s2sUrl = process.env.TEST_S2S_URL as string;
    const idamUrl = process.env.IDAM_API_BASEURL as string;
    const oneTimePassword = otplib.authenticator.generate(s2sSecret);
    const callbackUrl = "http://localhost:3001/receiver";
    let s2sAuth: string;
    let caseId: string | undefined;
  
    const agent = new https.Agent({  
        rejectUnauthorized: false
    });

    const newUser: NewUser = {
        forename: "User",
        surname: "Test",
        password: "AdoptionPassword@123",
        roles: [
            { code: "caseworker" },
            { code: "caseworker-adoption-caseworker" },
            { code: "caseworker-adoption" },
            { code: "citizen" }
        ]
    };

    newUser.email = `adop-test.${Date.now()}@mailinator.com`;

    try {
        const s2sAuthRequest = await Axios.post(`${s2sUrl}/lease`, {
            microservice: process.env.ADOPTION_WEB_MICROSERVICE,
            oneTimePassword: oneTimePassword
        }, { httpsAgent: agent });
        s2sAuth = s2sAuthRequest.data;
        
        const createdAccount = await Axios.post(`${idamUrl}/testing-support/accounts`, newUser, { httpsAgent: agent });
        
        const idamData = `username=${newUser.email}&password=${newUser.password}&client_id=${clientId}&client_secret=${idamSecret}&grant_type=password&redirect_uri=${callbackUrl}&scope=openid%20profile%20roles`;
        const idamAccess = await Axios.post(`${idamUrl}/o/token`, idamData, { httpsAgent: agent });
        const idamToken = idamAccess.data.access_token;

        const axiosClient = Axios.create({
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

        const event = { id: "citizen-create-application" };
      
        const data = {
            applicant1FirstName: "Will",
            applicant1LastName: "Smith",
            applicant1Email: "willsmith@mailinator.com",
        };

        const tokenRes = await axiosClient.get(`/case-types/A58/event-triggers/citizen-create-application`);
        let token = tokenRes.data.token;

        let response = await axiosClient.post(`/case-types/A58/cases`, {
            event,
            data,
            event_token: token,
        });
        caseId = response.data.id;

        let submitTokenRes = await axiosClient.get(`${url}/cases/${caseId}/event-triggers/citizen-submit-application`);
        token = submitTokenRes.data.token;
        const submitEvent = { id: "citizen-submit-application" };
        const submitData = utils.data;

        response = await axiosClient.post(`/cases/${caseId}/events`, {
            event: submitEvent,
            data: submitData,
            event_token: token,
        });

        let paymentTokenRes = await axiosClient.get(`${url}/cases/${caseId}/event-triggers/citizen-add-payment`);
        token = paymentTokenRes.data.token;
        const paymentEvent = { id: "citizen-add-payment" };
        const paymentData = utils.data;

        response = await axiosClient.post(`/cases/${caseId}/events`, {
            event: paymentEvent,
            data: paymentData,
            event_token: token,
        });
        console.log(caseId);
        return caseId;

    } catch (err) {
        console.error("could not create account", err);
    }
};
