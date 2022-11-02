package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionAgencyOrLocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Children;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Guardian;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.OtherAdoptionAgencyOrLocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SocialWorker;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Solicitor;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class AmendOtherPartiesDetails implements CcdPageConfiguration {


    @Override
    public void addTo(PageBuilder pageBuilder) {
        buildPageWithChildDetails(pageBuilder);
    }

    private void buildPageWithChildDetails(PageBuilder pageBuilder) {
        pageBuilder.page("amendOtherParties")
            .complex(CaseData::getChildren).label("childDetaillabel1", "### Child Details",null,true)
            .label("childDetaillabel2", "### Child",null,true)
            .mandatory(Children::getFirstName).mandatory(Children::getLastName)
            .mandatory(Children::getDateOfBirth).mandatory(Children::getSexAtBirth).mandatory(Children::getNationality)
            .mandatory(Children::getFirstNameAfterAdoption).mandatory(Children::getLastNameAfterAdoption)
            .done().label("localGuardianLabel1", "### Legal guardian (CAFCASS)",null,true)
            .mandatoryWithLabel(CaseData::getIsChildRepresentedByGuardian, "Is the child represented by a guardian?")
            .complex(CaseData::getLocalGuardian)
            .mandatory(Guardian::getName, "isChildRepresentedByGuardian=\"Yes\"")
            .mandatory(Guardian::getGuardianAddress, "isChildRepresentedByGuardian=\"Yes\"")
            .mandatory(Guardian::getPhoneNumber, "isChildRepresentedByGuardian=\"Yes\"")
            .mandatory(Guardian::getEmail, "isChildRepresentedByGuardian=\"Yes\"")
            .done()
            .label("childDetaillabel3", "### Solicitor","isChildRepresentedByGuardian=\"Yes\"",true)
            .mandatory(CaseData::getIsChildRepresentedBySolicitor, "isChildRepresentedByGuardian=\"Yes\"")
            .complex(CaseData::getChildSolicitor)
            .mandatory(Solicitor::getSolicitorFirm, "isChildRepresentedByGuardian=\"Yes\" AND isChildRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getSolicitorRef, "isChildRepresentedByGuardian=\"Yes\" AND isChildRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getSolicitorAddress, "isChildRepresentedByGuardian=\"Yes\" AND isChildRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getPhoneNumber, "isChildRepresentedByGuardian=\"Yes\" AND isChildRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getEmail, "isChildRepresentedByGuardian=\"Yes\" AND isChildRepresentedBySolicitor=\"Yes\"")
            .done()
            .complex(CaseData::getAdopAgencyOrLA)
            .label("localauthorylable1", "### Agencies/Local authorities details",null,true)
            .label("localauthorylable2", "### Adoption agency",null,true)
            .mandatory(AdoptionAgencyOrLocalAuthority::getAdopAgencyOrLaName)
            .mandatory(AdoptionAgencyOrLocalAuthority::getAdopAgencyOrLaContactName)
            .mandatory(AdoptionAgencyOrLocalAuthority::getAdopAgencyAddressLine1)
            .optional(AdoptionAgencyOrLocalAuthority::getAdopAgencyAddressLine2)
            .optional(AdoptionAgencyOrLocalAuthority::getAdopAgencyAddressLine3)
            .mandatory(AdoptionAgencyOrLocalAuthority::getAdopAgencyTown)
            .optional(AdoptionAgencyOrLocalAuthority::getAdopAgencyAddressCounty)
            .mandatory(AdoptionAgencyOrLocalAuthority::getAdopAgencyPostcode)
            .mandatory(AdoptionAgencyOrLocalAuthority::getAdopAgencyCountry)
            .mandatory(AdoptionAgencyOrLocalAuthority::getAdopAgencyOrLaPhoneNumber)
            .mandatory(AdoptionAgencyOrLocalAuthority::getAdopAgencyOrLaContactEmail)
            .done()
            .label("otherAdopAgencylable1", "### Other adoption agency",null,true)
            .mandatoryWithLabel(CaseData::getHasAnotherAdopAgencyOrLAinXui, "Is there any other adoption agency?")
            .complex(CaseData::getOtherAdoptionAgencyOrLA)
            .mandatory(
                OtherAdoptionAgencyOrLocalAuthority::getAgencyOrLaName,
                "hasAnotherAdopAgencyOrLAinXui=\"Yes\""
            )
            .mandatory(
                OtherAdoptionAgencyOrLocalAuthority::getAgencyOrLaContactName,
                "hasAnotherAdopAgencyOrLAinXui=\"Yes\""
            )
            .mandatory(
                OtherAdoptionAgencyOrLocalAuthority::getAgencyAddress,
                "hasAnotherAdopAgencyOrLAinXui=\"Yes\""
            )
            .mandatory(
                OtherAdoptionAgencyOrLocalAuthority::getAgencyOrLaPhoneNumber,
                "hasAnotherAdopAgencyOrLAinXui=\"Yes\""
            )
            .mandatory(
                OtherAdoptionAgencyOrLocalAuthority::getAgencyOrLaContactEmail,
                "hasAnotherAdopAgencyOrLAinXui=\"Yes\""
            )
            .done()
            .complex(CaseData::getChildSocialWorker)
            .label("childLocalAuthoritylable1", "### Child's local authority",null,true)
            .mandatory(SocialWorker::getSocialWorkerName).mandatory(SocialWorker::getLocalAuthority)
            .mandatory(SocialWorker::getSocialWorkerAddressLine1).optional(SocialWorker::getSocialWorkerAddressLine2)
            .optional(SocialWorker::getSocialWorkerAddressLine3).mandatory(SocialWorker::getSocialWorkerTown)
            .optional(SocialWorker::getSocialWorkerAddressCounty).mandatory(SocialWorker::getSocialWorkerPostcode)
            .mandatory(SocialWorker::getSocialWorkerCountry).mandatory(SocialWorker::getSocialWorkerPhoneNumber)
            .mandatory(SocialWorker::getLocalAuthorityEmail)
            .done()
            .complex(CaseData::getApplicantSocialWorker)
            .label("applicantsocilaworkerlabel1", "### Applicant's local authority",null,true)
            .mandatory(SocialWorker::getSocialWorkerName).mandatory(SocialWorker::getLocalAuthority)
            .mandatory(SocialWorker::getSocialWorkerAddressLine1)
            .optional(SocialWorker::getSocialWorkerAddressLine2).optional(SocialWorker::getSocialWorkerAddressLine3)
            .mandatory(SocialWorker::getSocialWorkerTown).optional(SocialWorker::getSocialWorkerAddressCounty)
            .mandatory(SocialWorker::getSocialWorkerPostcode).mandatory(SocialWorker::getSocialWorkerCountry)
            .mandatory(SocialWorker::getSocialWorkerPhoneNumber).mandatory(SocialWorker::getLocalAuthorityEmail)
            .done()
            .label("RespondentDetailsLabel1", "### Respondent details",null,true)
            .label("birthMotherLabel1", "### Birth mother",null,true)
            .complex(CaseData::getBirthMother)
            .mandatory(Parent::getFirstName).mandatory(Parent::getLastName)
            .mandatory(Parent::getDeceased).mandatory(Parent::getAddress1)
            .optional(Parent::getAddress2).optional(Parent::getAddress3)
            .mandatory(Parent::getAddressTown).optional(Parent::getAddressCounty)
            .mandatory(Parent::getAddressPostCode).mandatory(Parent::getAddressCountry)
            .mandatory(Parent::getLastAddressDate).mandatory(Parent::getToBeServed, "", "Yes")
            .done()
            .label("birthMotherSolicitorLab1", "### Solicitor",null,true)
            .mandatoryWithLabel(CaseData::getIsBirthMotherRepresentedBySolicitor,
                                "Is the birth mother represented by a solicitor?")
            .complex(CaseData::getBirthMotherSolicitor)
            .mandatory(Solicitor::getSolicitorFirm, "isBirthMotherRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getSolicitorRef, "isBirthMotherRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getSolicitorAddress, "isBirthMotherRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getPhoneNumber, "isBirthMotherRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getEmail, "isBirthMotherRepresentedBySolicitor=\"Yes\"")
            .done()
            .label("birthFatherLabel1", "### Birth Father",null,true)
            .complex(CaseData::getBirthFather)
            .mandatory(Parent::getFirstName).mandatory(Parent::getLastName)
            .mandatory(Parent::getDeceased).mandatory(Parent::getAddress1)
            .optional(Parent::getAddress2).optional(Parent::getAddress3)
            .mandatory(Parent::getAddressTown).optional(Parent::getAddressCounty)
            .mandatory(Parent::getAddressPostCode).mandatory(Parent::getAddressCountry)
            .mandatory(Parent::getLastAddressDate)
            .mandatory(Parent::getToBeServed, "", "Yes")
            .done()
            .mandatory(CaseData::getIsBirthFatherRepresentedBySolicitor)
            .complex(CaseData::getBirthFatherSolicitor)
            .mandatory(Solicitor::getSolicitorFirm, "isBirthFatherRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getSolicitorRef, "isBirthFatherRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getSolicitorAddress, "isBirthFatherRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getPhoneNumber, "isBirthFatherRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getEmail, "isBirthFatherRepresentedBySolicitor=\"Yes\"")
            .done()
            .label("otherpersonlable1", "### Other person with parental responsibility",null,true)
            .mandatory(CaseData::getIsThereAnyOtherPersonWithParentalResponsibility)
            .complex(CaseData::getOtherParent)
            .mandatory(Parent::getFirstName, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\"")
            .mandatory(Parent::getLastName, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\"")
            .mandatory(Parent::getAddress1, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\"")
            .optional(Parent::getAddress2, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\"")
            .optional(Parent::getAddress3, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\"")
            .mandatory(Parent::getAddressTown, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\"")
            .optional(Parent::getAddressCounty, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\"")
            .mandatory(Parent::getAddressPostCode, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\"")
            .optional(Parent::getAddressCountry, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\"")
            .mandatory(Parent::getLastAddressDate, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\"")
            .mandatory(Parent::getRelationShipWithChild, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\"")
            .mandatory(Parent::getToBeServed, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\"", "Yes")
            .done()
            .label(
                "otherParentSolicitorlabel1",
                "### Solicitor",
                "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\"",
                true
            )
            .mandatory(CaseData::getIsOtherParentRepresentedBySolicitor,
                       "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\"")
            .complex(CaseData::getOtherParentSolicitor)
            .mandatory(Solicitor::getSolicitorFirm, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\" "
                + "AND isOtherParentRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getSolicitorRef, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\" "
                + "AND isOtherParentRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getSolicitorAddress, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\" "
                + "AND isOtherParentRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getPhoneNumber, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\" "
                + "AND isOtherParentRepresentedBySolicitor=\"Yes\"")
            .mandatory(Solicitor::getEmail, "isThereAnyOtherPersonWithParentalResponsibility=\"Yes\" "
                + "AND isOtherParentRepresentedBySolicitor=\"Yes\"")
            .done();
    }
}
