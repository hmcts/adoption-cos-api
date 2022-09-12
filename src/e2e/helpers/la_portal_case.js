const Axios = require('axios');
const otplib = require('otplib');

const createCase = async (caseId) => {

    const clientId = process.env.ADOPTION_WEB_CLIENT_ID;
    const s2sSecret = process.env.S2S_SECRET_WEB;
    const idamSecret = process.env.IDAM_CLIENT_SECRET;
    const url = process.env.CASE_DATA_STORE_BASEURL;
    const s2sUrl = process.env.S2S_SECRET_WEB;
    const idamUrl = process.env.IDAM_API_BASEURL;
    const oneTimePassword = otplib.authenticator.generate(s2sSecret);
    const callbackUrl = "http://localhost:3001/receiver";
    let s2sAuth;

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
        console.log({ "microservice": process.env.ADOPTION_WEB_MICROSERVICE, "oneTimePassword": oneTimePassword });
        let s2sAuthRequest = await Axios.post(s2sUrl, { "microservice": process.env.ADOPTION_WEB_MICROSERVICE, "oneTimePassword": oneTimePassword });
        s2sAuth = s2sAuthRequest.data;
        let idamData = `username=${process.env.IDAM_SYSTEM_UPDATE_USERNAME}&password=${IDAM_SYSTEM_UPDATE_PASSWORD}&client_id=${clientId}&client_secret=${idamSecret}&grant_type=password&redirect_uri=${callbackUrl}&scope=openid%20profile%20roles`;
        let idamAccess = await Axios.post(`${idamUrl}/o/token`, idamData);
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
        });

        tokenRes = await axiosClient.get(`${url}/cases/${caseId}/event-triggers/local-authority-application-submit`);
        token = tokenRes.data.token;
        event = { id: "local-authority-application-submit" };
        const mydata = {

            "birthFatherAddress1": "BUCKINGHAM PALACE",
            "birthFatherAddress2": "",
            "birthFatherAddress3": null,
            "birthFatherAddressCountry": null,
            "birthFatherAddressCounty": "CITY OF WESTMINSTER",
            "birthFatherAddressKnown": "Yes",
            "birthFatherAddressNotKnownReason": "",
            "birthFatherAddressPostCode": "SW1A 1AA",
            "birthFatherAddressTown": "LONDON",
            "birthFatherFirstName": "Father",
            "birthFatherLastName": "Father",
            "birthFatherNameOnCertificate": "Yes",
            "hasSiblings": "Yes",
            "birthFatherNationality": [
                "British"
            ],
            "birthFatherNotAliveReason": "",
            "birthFatherOccupation": "unknown",
            "birthFatherOtherNationalities": [],
            "birthFatherStillAlive": "Yes",
            "birthMotherAddress1": "2 TRIVETT SQUARE",
            "birthMotherAddress2": "",
            "birthMotherAddress3": null,
            "birthMotherAddressCountry": null,
            "birthMotherAddressCounty": "NOTTINGHAM CITY",
            "birthMotherAddressKnown": "Yes",
            "birthMotherAddressNotKnownReason": "Some reason",
            "birthMotherAddressPostCode": "NG1 1JB",
            "birthMotherAddressTown": "NOTTINGHAM",
            "birthMotherFirstName": "Mother",
            "birthMotherLastName": "Mother",
            "birthMotherNameOnCertificate": null,
            "birthMotherNationality": [
                "British",
                "Irish"
            ],
            "birthMotherNotAliveReason": "",
            "birthMotherOccupation": "Teacher",
            "birthMotherOtherNationalities": [],
            "birthMotherStillAlive": "Yes",

            "otherParentAddress1": "BUCKINGHAM PALACE",
            "otherParentAddress2": "",
            "otherParentAddress3": null,
            "otherParentAddressCountry": null,
            "otherParentAddressCounty": "CITY OF WESTMINSTER",
            "otherParentAddressKnown": "Yes",
            "otherParentAddressNotKnownReason": "",
            "otherParentAddressPostCode": "SW1A 1AA",
            "otherParentAddressTown": "LONDON",
            "otherParentFirstName": "other",
            "otherParentLastName": "parent",
            "otherParentNameOnCertificate": null,
            "otherParentNationality": null,
            "otherParentNotAliveReason": null,
            "otherParentOccupation": null,
            "otherParentOtherNationalities": [],
            "otherParentStillAlive": "Yes",
            "placementOrders": [
                {
                    "id": "6e59287f-a337-49ba-ae45-c1f8bfe72f25",
                    "value": {
                        "placementOrderId": "1646222468418",
                        "placementOrderDate": "2021-12-12",
                        "placementOrderCourt": "xyz",
                        "placementOrderNumber": "123456"
                    }
                },
                {
                    "id": "a341637f-c3f8-4a00-9d6f-c560cea7a677",
                    "value": {
                        "placementOrderId": "1646222494045",
                        "placementOrderDate": "2021-12-12",
                        "placementOrderType": "Adoption Order",
                        "placementOrderCourt": "london court",
                        "placementOrderNumber": "12345"
                    }
                }
            ],
            "siblings": [
              {

              }
            ],
            "addAnotherSiblingPlacementOrder": "No",
            "selectedSiblingId": "1646222699254",
            "selectedSiblingPoType": "adoptionOrder",
            "selectedSiblingRelation": "sister",
            "selectedPlacementOrderId": "1646222468418",
            "addAnotherPlacementOrder": "No",
            "laCannotUpload": "Yes",
            "laCannotUploadSupportingDocument": null,
            "laDocumentsUploaded": [],
            "laNameSot": "My LA",
            "laSotFullName": "John Doe",
            "laSotJobtitle": "Head",
            "laStatementOfTruth": "Yes"
        };
        response = await axiosClient.post(`/cases/${caseId}/events`, {
            event,
            data: mydata,
            event_token: token,
        });

        return caseId;

    } catch (err) {
        console.log("could not create account", err);
    }

};
