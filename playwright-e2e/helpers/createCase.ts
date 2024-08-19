import axios, { AxiosInstance } from 'axios';
import { authenticator } from 'otplib';
import https from 'https';

interface NewUser {
  forename: string;
  surname: string;
  password: string;
  roles: Array<{ code: string }>;
  email?: string;
}

interface AuthResponse {
  data: {
    access_token: string;
  };
}

interface TokenResponse {
  data: {
    token: string;
  };
}

export const createCase = async (): Promise<string | undefined> => {
  const clientId = 'adoption-web';
  const s2sSecret = process.env.S2S_SECRET_WEB!;
  const idamSecret = process.env.IDAM_CLIENT_SECRET!;
  const url = process.env.CASE_DATA_STORE_BASEURL!;
  const s2sUrl = process.env.TEST_S2S_URL!;
  const idamUrl = process.env.IDAM_API_BASEURL!;
  const oneTimePassword = authenticator.generate(s2sSecret);
  const callbackUrl = "http://localhost:3001/receiver";

  let s2sAuth: string;
  let caseId: string;

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
    let s2sAuthRequest = await axios.post(`${s2sUrl}/lease`, {
      microservice: process.env.ADOPTION_WEB_MICROSERVICE!,
      oneTimePassword
    }, { httpsAgent: agent });

    s2sAuth = s2sAuthRequest.data;

    let createdAccount = await axios.post(`${idamUrl}/testing-support/accounts`, newUser, { httpsAgent: agent });

    let idamData = `username=${newUser.email}&password=${newUser.password}&client_id=${clientId}&client_secret=${idamSecret}&grant_type=password&redirect_uri=${callbackUrl}&scope=openid%20profile%20roles`;
    let idamAccess: AuthResponse = await axios.post(`${idamUrl}/o/token`, idamData, { httpsAgent: agent });
    let idamToken = idamAccess.data.access_token;

    let axiosClient: AxiosInstance = axios.create({
      baseURL: url,
      headers: {
        Authorization: `Bearer ${idamToken}`,
        ServiceAuthorization: s2sAuth,
        experimental: 'true',
        Accept: '*/*',
        'Content-Type': 'application/json',
      },
      httpsAgent: new https.Agent({
        rejectUnauthorized: false
      })
    });

    let event = { id: "citizen-create-application" };

    let data = {
      applicant1FirstName: "Will",
      applicant1LastName: "Smith",
      applicant1Email: "willsmith@mailinator.com",
    };

    let tokenRes: TokenResponse = await axiosClient.get(`/case-types/A58/event-triggers/citizen-create-application`);
    let token = tokenRes.data.token;
    event = { id: "citizen-create-application" };

    let response = await axiosClient.post(`/case-types/A58/cases`, {
      event,
      data,
      event_token: token,
    });

    caseId = response.data.id;

    tokenRes = await axiosClient.get(`${url}/cases/${caseId}/event-triggers/citizen-submit-application`);
    token = tokenRes.data.token;
    event = { id: "citizen-submit-application" };
    data = utilsData;
    await axiosClient.post(`/cases/${caseId}/events`, {
      event,
      data,
      event_token: token,
    });

    tokenRes = await axiosClient.get(`${url}/cases/${caseId}/event-triggers/citizen-add-payment`);
    token = tokenRes.data.token;
    event = { id: "citizen-add-payment" };
    data = utilsData;
    await axiosClient.post(`/cases/${caseId}/events`, {
      event,
      data,
      event_token: token,
    });

    console.log(caseId);
    return caseId;

  } catch (err) {
    console.log("Could not create account");
    console.error(err);
    return undefined;
  }
};
