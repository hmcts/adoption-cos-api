import Axios, { AxiosInstance } from 'axios';
import * as otplib from 'otplib';
import * as https from 'https';
import * as utils from '../utils/utils';
import * as cui from './create_cui_case';

export const laPortalCase = async (caseId: string): Promise<string> => {
    const clientId: string = process.env.ADOPTION_WEB_CLIENT_ID || '';
    const s2sSecret: string = process.env.S2S_SECRET_WEB || '';
    const idamSecret: string = process.env.IDAM_CLIENT_SECRET || '';
    const url: string = process.env.CASE_DATA_STORE_BASEURL || '';
    const s2sUrl: string = process.env.TEST_S2S_URL || '';
    const idamUrl: string = process.env.IDAM_API_BASEURL || '';
    const oneTimePassword: string = otplib.authenticator.generate(s2sSecret);
    const callbackUrl: string = "http://localhost:3001/receiver";
    let s2sAuth: string | undefined;

    const agent: https.Agent = new https.Agent({
        rejectUnauthorized: false
    });

    let newUser: { [key: string]: any } = {
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
        let idamData: string = `username=${process.env.IDAM_SYSTEM_UPDATE_USERNAME}&password=${process.env.IDAM_SYSTEM_UPDATE_PASSWORD}&client_id=${clientId}&client_secret=${idamSecret}&grant_type=password&redirect_uri=${callbackUrl}&scope=openid%20profile%20roles`;
        let idamAccess = await Axios.post(`${idamUrl}/o/token`, idamData, { httpsAgent: agent });
        let idamToken: string = idamAccess.data.access_token;

        const axiosClient: AxiosInstance = Axios.create({
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

        const tokenRes = await axiosClient.get(`${url}/cases/${caseId}/event-triggers/local-authority-application-submit`);
        const token: string = tokenRes.data.token;
        const event: { id: string } = { id: "local-authority-application-submit" };
        const caseData = utils.caseDataLa;
        const response = await axiosClient.post(`/cases/${caseId}/events`, {
            event,
            data: caseData,
            event_token: token,
        });
        return caseId;

    } catch (err) {
        console.log("could not create account");
        throw err; // Rethrow error to handle it in caller function
    }
};

export const createCompleteCase = async (): Promise<string> => {
    const caseId = await cui.createCase();
    await laPortalCase(caseId);
    return caseId;
}
