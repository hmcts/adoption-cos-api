const Axios = require('axios');
const otplib = require('otplib');

const createCase = async () => {

    const clientId = process.env.ADOPTION_WEB_CLIENT_ID;
    const s2sSecret = process.env.S2S_SECRET_WEB;
    const idamSecret = process.env.IDAM_CLIENT_SECRET;
    let a;
    const url = process.env.CASE_DATA_STORE_BASEURL;
    const s2sUrl = process.env.S2S_SECRET_WEB;
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
        console.log({ "microservice": process.env.ADOPTION_WEB_MICROSERVICE, "oneTimePassword": oneTimePassword });
        let s2sAuthRequest = await Axios.post(s2sUrl, { "microservice": process.env.ADOPTION_WEB_MICROSERVICE, "oneTimePassword": oneTimePassword });
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
        data = {
            "adopAgencyAddressLine1": "address",
            "adopAgencyOrLaContactEmail": "agency1@email.co.uk",
            "adopAgencyOrLaContactName": "contact name1",
            "adopAgencyOrLaName": "agency1",
            "adopAgencyOrLaPhoneNumber": "01234567890",
            "adopAgencyPostcode": "aa14aa",
            "adopAgencyTown": "town",
            "adoptionDocument": {},
            "applicant1AdditionalNames": [],
            "applicant1AdditionalNationalities": [],
            "applicant1Address1": "1 TRIVETT SQUARE",
            "applicant1Address2": "",
            "applicant1AddressCountry": "NOTTINGHAM CITY",
            "applicant1AddressPostCode": "NG1 1JB",
            "applicant1AddressSameAsApplicant1": null,
            "applicant1AddressTown": "NOTTINGHAM",
            "applicant1CannotUpload": "Yes",
            "applicant1CannotUploadSupportingDocument": [
                "birthOrAdoptionCertificate"
            ],
            "applicant1ContactDetails": null,
            "applicant1ContactDetailsConsent": "Yes",
            "applicant1DateOfBirth": "1990-12-12",
            "applicant1DocumentsUploaded": [],
            "applicant1Email": "citizen.automation@mailinator.com",
            "applicant1EmailAddress": "abid@gmail.co",
            "applicant1FirstName": "Abid1",
            "applicant1HasOtherNames": "No",
            "applicant1LanguagePreference": "ENGLISH",
            "applicant1LastName": "sayyad",
            "applicant1Nationality": null,
            "applicant1Occupation": "Software Engineer",
            "applicant1PhoneNumber": "0987654321",
            "applicant1SotFullName": "Usain Bolt",
            "applicant1StatementOfTruth": "Yes",
            "applicant2AdditionalNames": [],
            "applicant2AdditionalNationalities": [],
            "applicant2Address1": "2 WHITE ROSE WR2, BROAD STREET",
            "applicant2Address2": "",
            "applicant2AddressCountry": "NOTTINGHAM CITY",
            "applicant2AddressPostCode": "NG1 3AL",
            "applicant2AddressSameAsApplicant1": "No",
            "applicant2AddressTown": "NOTTINGHAM",
            "applicant2ContactDetails": null,
            "applicant2ContactDetailsConsent": "Yes",
            "applicant2DateOfBirth": "1990-12-12",
            "applicant2Email": null,
            "applicant2EmailAddress": "someone@gmail.com",
            "applicant2FirstName": "second",
            "applicant2HasOtherNames": "No",
            "applicant2LanguagePreference": null,
            "applicant2LastName": "sasass",
            "applicant2Nationality": null,
            "applicant2Occupation": "tester",
            "applicant2PhoneNumber": "12121212121",
            "applicant2SotFullName": "James Bond",
            "applicant2StatementOfTruth": "Yes",
            "applicantLocalAuthority": "LA",
            "applicantLocalAuthorityEmail": "la@email.gov.uk",
            "applicantSocialWorkerAddressLine1": "43 AB Road",
            "applicantSocialWorkerEmail": "socialw@email.gov.uk",
            "applicantSocialWorkerName": "social worker",
            "applicantSocialWorkerPhoneNumber": "01234567893",
            "applicantSocialWorkerPostcode": "social worker",
            "applicantSocialWorkerTown": "London",
            "applicationFeeOrderSummary": {
                "PaymentTotal": "183",
                "Fees": [
                    {
                        "value": {
                            "FeeAmount": "183",
                            "FeeCode": "A58",
                            "FeeVersion": "1"
                        }
                    }
                ]
            },
            "applicationPayments": [
                {
                    "id": "12",
                    "value": {
                        "created": "2021-11-02T02:50:12.208Z",
                        "updated": "2021-11-02T02:50:12.208Z",
                        "feeCode": "A58",
                        "amount": "183",
                        "status": "success",
                        "channel": "online",
                        "reference": "1234",
                        "transactionId": "1234"
                    }
                }
            ],
            "applyingWith": "withSpouseOrCivilPartner",
            "childLocalAuthority": "LA",
            "childLocalAuthorityEmail": "la@email.gov.uk",
            "childSocialWorkerAddressLine1": "43 AB Road",
            "childSocialWorkerEmail": "socialw@email.gov.uk",
            "childSocialWorkerName": "social worker",
            "childSocialWorkerPhoneNumber": "01234567893",
            "childSocialWorkerPostcode": "social worker",
            "childSocialWorkerTown": "London",
            "childrenAdditionalNationalities": [],
            "childrenDateOfBirth": "2015-12-12",
            "childrenFirstName": "child",
            "childrenFirstNameAfterAdoption": "child",
            "childrenLastName": "child",
            "childrenLastNameAfterAdoption": "child",
            "childrenNationality": [
                "British",
                "Irish",
                "Other"
            ],
            "childrenOtherSexAtBirth": "something",
            "childrenSexAtBirth": "other",
            "createdDate": null,
            "dateChildMovedIn": "2021-10-12",
            "dateSubmitted": null,
            "documentsGenerated": [],
            "documentsUploaded": [],
            "dueDate": null,
            "familyCourtEmailId": "adoptionproject@mailinator.com",
            "familyCourtName": "xyz",
            "findFamilyCourt": "Yes",
            "hasAnotherAdopAgencyOrLA": "No",
            "localAuthorityContactEmail": "agency1@email.co.uk",
            "localAuthorityContactName": "contact name1",
            "localAuthorityName": "laname",
            "localAuthorityPhoneNumber": "01234567890",
            "pcqId": "9c04ea3a-c614-4eac-8361-ac75fb59f9dc",
            "placementOrderCourt": "xyz",
            "solicitorEmail": null,
            "solicitorFirm": null,
            "solicitorHelpingWithApplication": null,
            "solicitorName": null,
            "solicitorPhoneNumber": null
        };
        response = await axiosClient.post(`/cases/${caseId}/events`, {
            event,
            data,
            event_token: token,
        });

        tokenRes = await axiosClient.get(`${url}/cases/${caseId}/event-triggers/citizen-add-payment`);
        token = tokenRes.data.token;
        event = { id: "citizen-add-payment" };
        data = {
            "adopAgencyAddressLine1": "address",
            "adopAgencyOrLaContactEmail": "agency1@email.co.uk",
            "adopAgencyOrLaContactName": "contact name1",
            "adopAgencyOrLaName": "agency1",
            "adopAgencyOrLaPhoneNumber": "01234567890",
            "adopAgencyPostcode": "aa14aa",
            "adopAgencyTown": "town",
            "adoptionDocument": {},
            "applicant1AdditionalNames": [],
            "applicant1AdditionalNationalities": [],
            "applicant1Address1": "1 TRIVETT SQUARE",
            "applicant1Address2": "",
            "applicant1AddressCountry": "NOTTINGHAM CITY",
            "applicant1AddressPostCode": "NG1 1JB",
            "applicant1AddressSameAsApplicant1": null,
            "applicant1AddressTown": "NOTTINGHAM",
            "applicant1CannotUpload": "Yes",
            "applicant1CannotUploadSupportingDocument": [
                "birthOrAdoptionCertificate"
            ],
            "applicant1ContactDetails": null,
            "applicant1ContactDetailsConsent": "Yes",
            "applicant1DateOfBirth": "1990-12-12",
            "applicant1DocumentsUploaded": [],
            "applicant1Email": "citizen.automation@mailinator.com",
            "applicant1EmailAddress": "abid@gmail.co",
            "applicant1FirstName": "Abid1",
            "applicant1HasOtherNames": "No",
            "applicant1LanguagePreference": "ENGLISH",
            "applicant1LastName": "sayyad",
            "applicant1Nationality": null,
            "applicant1Occupation": "Software Engineer",
            "applicant1PhoneNumber": "0987654321",
            "applicant1SotFullName": "Usain Bolt",
            "applicant1StatementOfTruth": "Yes",
            "applicant2AdditionalNames": [],
            "applicant2AdditionalNationalities": [],
            "applicant2Address1": "2 WHITE ROSE WR2, BROAD STREET",
            "applicant2Address2": "",
            "applicant2AddressCountry": "NOTTINGHAM CITY",
            "applicant2AddressPostCode": "NG1 3AL",
            "applicant2AddressSameAsApplicant1": "No",
            "applicant2AddressTown": "NOTTINGHAM",
            "applicant2ContactDetails": null,
            "applicant2ContactDetailsConsent": "Yes",
            "applicant2DateOfBirth": "1990-12-12",
            "applicant2Email": null,
            "applicant2EmailAddress": "someone@gmail.com",
            "applicant2FirstName": "second",
            "applicant2HasOtherNames": "No",
            "applicant2LanguagePreference": null,
            "applicant2LastName": "sasass",
            "applicant2Nationality": null,
            "applicant2Occupation": "tester",
            "applicant2PhoneNumber": "12121212121",
            "applicant2SotFullName": "James Bond",
            "applicant2StatementOfTruth": "Yes",
            "applicantLocalAuthority": "LA",
            "applicantLocalAuthorityEmail": "la@email.gov.uk",
            "applicantSocialWorkerAddressLine1": "43 AB Road",
            "applicantSocialWorkerEmail": "socialw@email.gov.uk",
            "applicantSocialWorkerName": "social worker",
            "applicantSocialWorkerPhoneNumber": "01234567893",
            "applicantSocialWorkerPostcode": "social worker",
            "applicantSocialWorkerTown": "London",
            "applicationFeeOrderSummary": {
                "PaymentTotal": "183",
                "Fees": [
                    {
                        "value": {
                            "FeeAmount": "183",
                            "FeeCode": "A58",
                            "FeeVersion": "1"
                        }
                    }
                ]
            },
            "applicationPayments": [
                {
                    "id": "12",
                    "value": {
                        "created": "2021-11-02T02:50:12.208Z",
                        "updated": "2021-11-02T02:50:12.208Z",
                        "feeCode": "A58",
                        "amount": "183",
                        "status": "success",
                        "channel": "online",
                        "reference": "1234",
                        "transactionId": "1234"
                    }
                }
            ],
            "applyingWith": "withSpouseOrCivilPartner",
            "childLocalAuthority": "LA",
            "childLocalAuthorityEmail": "la@email.gov.uk",
            "childSocialWorkerAddressLine1": "43 AB Road",
            "childSocialWorkerEmail": "socialw@email.gov.uk",
            "childSocialWorkerName": "social worker",
            "childSocialWorkerPhoneNumber": "01234567893",
            "childSocialWorkerPostcode": "social worker",
            "childSocialWorkerTown": "London",
            "childrenAdditionalNationalities": [],
            "childrenDateOfBirth": "2015-12-12",
            "childrenFirstName": "child",
            "childrenFirstNameAfterAdoption": "child",
            "childrenLastName": "child",
            "childrenLastNameAfterAdoption": "child",
            "childrenNationality": [
                "British",
                "Irish",
                "Other"
            ],
            "childrenOtherSexAtBirth": "something",
            "childrenSexAtBirth": "other",
            "createdDate": null,
            "dateChildMovedIn": "2021-10-12",
            "dateSubmitted": null,
            "documentsGenerated": [],
            "documentsUploaded": [],
            "dueDate": null,
            "familyCourtEmailId": "adoptionproject@mailinator.com",
            "familyCourtName": "xyz",
            "findFamilyCourt": "Yes",
            "hasAnotherAdopAgencyOrLA": "No",
            "localAuthorityContactEmail": "agency1@email.co.uk",
            "localAuthorityContactName": "contact name1",
            "localAuthorityName": "laname",
            "localAuthorityPhoneNumber": "01234567890",
            "pcqId": "9c04ea3a-c614-4eac-8361-ac75fb59f9dc",
            "placementOrderCourt": "xyz",
            "solicitorEmail": null,
            "solicitorFirm": null,
            "solicitorHelpingWithApplication": null,
            "solicitorName": null,
            "solicitorPhoneNumber": null
        };
        response = await axiosClient.post(`/cases/${caseId}/events`, {
            event,
            data,
            event_token: token,
        });

        s2sAuthRequest = await Axios.post(s2sUrl, { "microservice": process.env.ADOPTION_WEB_MICROSERVICE, "oneTimePassword": oneTimePassword });
        s2sAuth = s2sAuthRequest.data;
        idamData = `username=${process.env.IDAM_SYSTEM_UPDATE_USERNAME}&password=${IDAM_SYSTEM_UPDATE_PASSWORD}&client_id=${clientId}&client_secret=${idamSecret}&grant_type=password&redirect_uri=${callbackUrl}&scope=openid%20profile%20roles`;
        idamAccess = await Axios.post(`${idamUrl}/o/token`, idamData);
        idamToken = idamAccess.data.access_token;

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
