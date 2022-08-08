package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

//import uk.gov.hmcts.ccd.sdk.type.ListValue;

import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionAgencyOrLocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Children;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.PlacementOrder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.PlacementOrderType;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Sibling;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SiblingPoType;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SocialWorker;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.time.temporal.ChronoUnit.WEEKS;
import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.ApplyingWith.ALONE;

public final class ValidationUtil {

    public static final String EMPTY = " cannot be empty or null";
    public static final String YES = "YES";

    public static final String NOT_SURE = "NotSure";
    public static final String SOT_REQUIRED_APPLICANT1 = "Statement of truth must be accepted by the applicant1";
    public static final String SOT_REQUIRED_APPLICANT2 = "Statement of truth must be accepted by the applicant2";
    public static final String LESS_THAN_TEN_WEEKS_AGO = " can not be less than ten weeks ago.";

    private ValidationUtil() {
    }

    public static List<String> validateBasicCase(CaseData caseData) {
        boolean applyingAlone = ALONE.equals(caseData.getApplyingWith());

        return flattenLists(
            notNull(caseData.getApplyingWith(), "ApplyingWith"),
            notNull(caseData.getDateChildMovedIn(), "DateChildMovedIn"),
            validateDateChildMovedIn(caseData.getDateChildMovedIn(), "DateChildMovedIn"),
            validateApplicant1(caseData),
            validateApplicant2(applyingAlone, caseData)
        /*    validateChildren(caseData.getChildren()),
            notNull(caseData.getBirthMother().getFirstName(), "BirthMotherFirstName"),
            notNull(caseData.getBirthMother().getLastName(), "BirthMotherLastName"),
            validateBirthFather(caseData.getBirthFather()),
            validateOtherParent(caseData.getOtherParent()),
            validatePlacementOrders(caseData.getPlacementOrders()),
            validateSocialWorker(caseData.getChildSocialWorker()),
            validateLocalAuthorityAndAdoptionAgency(
                caseData.getLocalAuthority(),
                caseData.getAdopAgencyOrLA(),
                caseData.getHasAnotherAdopAgencyOrLA()
            )*/
        );
    }

    public static List<String> validateLocalAuthorityAndAdoptionAgency(LocalAuthority localAuthority,
                                                                       AdoptionAgencyOrLocalAuthority adopAgencyOrLA,
                                                                       YesOrNo hasAnotherAdopAgencyOrLA) {
        List<String> list = flattenLists(
            notNull(localAuthority.getLocalAuthorityContactEmail(), "LocalAuthorityEmail"),
            notNull(localAuthority.getLocalAuthorityPhoneNumber(), "LocalAuthorityPhoneNumber")
        );
        if (hasAnotherAdopAgencyOrLA.equals(YesOrNo.YES)) {
            list.addAll(flattenLists(
                notNull(adopAgencyOrLA.getAdopAgencyOrLaContactEmail(), "AdoptionAgencyOrLocalAuthorityEmail"),
                notNull(adopAgencyOrLA.getAdopAgencyOrLaPhoneNumber(), "AdoptionAgencyOrLocalAuthorityPhoneNumber")
            ));
        }
        return list;
    }

    public static List<String> validateSocialWorker(SocialWorker socialWorker) {
        return flattenLists(
            notNull(socialWorker.getSocialWorkerEmail(), "SocialWorkerEmail"),
            notNull(socialWorker.getSocialWorkerPhoneNumber(), "SocialWorkerPhoneNumber")
        );
    }

    /*private static List<String> validatePlacementOrders(List<ListValue<PlacementOrder>> placementOrders) {
        boolean emptyPlacementOrderNumber = nonNull(placementOrders) && placementOrders
            .stream().anyMatch(placementOrderListValue -> isEmpty(placementOrderListValue.getValue().getPlacementOrderNumber()));
        return emptyPlacementOrderNumber ? List.of("PlacementOrderNumber" + EMPTY) : emptyList();
    }*/

    public static List<String> validateBirthFather(Parent birthFather) {
        if (YES.equalsIgnoreCase(birthFather.getNameOnCertificate())) {
            return flattenLists(
                notNull(birthFather.getFirstName(), "BirthFatherFirstName"),
                notNull(birthFather.getLastName(), "BirthFatherLastName")
            );
        }
        return emptyList();
    }

    public static List<String> validateOtherParent(Parent otherParent) {
        if (YES.equalsIgnoreCase(otherParent.getStillAlive())) {
            return flattenLists(
                notNull(otherParent.getFirstName(), "BirthFatherFirstName"),
                notNull(otherParent.getLastName(), "BirthFatherLastName"),
                validateParentAddress(otherParent)
            );
        }
        return emptyList();
    }

    private static List<String> validateApplicant1(CaseData caseData) {
        return flattenLists(
            notNull(caseData.getApplicant1().getFirstName(), "Applicant1FirstName"),
            notNull(caseData.getApplicant1().getLastName(), "Applicant1LastName"),
            notNull(caseData.getApplicant1().getEmailAddress(), "Applicant1EmailAddress"),
            notNull(caseData.getApplicant1().getPhoneNumber(), "Applicant1PhoneNumber"),
            hasStatementOfTruth(caseData.getApplicant1StatementOfTruth(), SOT_REQUIRED_APPLICANT1),
            notNull(caseData.getApplicant1SotFullName(), "Applicant1SotFullName")
        );
    }

    private static List<String> validateApplicant2(boolean applyingAlone, CaseData caseData) {
        if (!applyingAlone) {
            return flattenLists(
                notNull(caseData.getApplicant2().getFirstName(), "Applicant2FirstName"),
                notNull(caseData.getApplicant2().getLastName(), "Applicant2LastName"),
                notNull(caseData.getApplicant2().getEmailAddress(), "Applicant2EmailAddress"),
                notNull(caseData.getApplicant2().getPhoneNumber(), "Applicant2PhoneNumber"),
                hasStatementOfTruth(caseData.getApplicant2StatementOfTruth(), SOT_REQUIRED_APPLICANT2),
                notNull(caseData.getApplicant2SotFullName(), "Applicant2SotFullName")
            );
        }
        return emptyList();
    }

    /*private static List<String> validateChildren(Children children) {
        return flattenLists(
            notNull(children.getFirstName(), "ChildrenFirstName"),
            notNull(children.getLastName(), "ChildrenLastName"),
            notNull(children.getDateOfBirth(), "ChildrenDateOfBirth")
        );
    }*/

    public static List<String> validateCase(CaseData caseData) {
        return flattenLists(
            validateChildren(caseData.getChildren()),
            validateBirthFatherParent(caseData.getBirthFather()),
            validateBirthMotherParent(caseData.getBirthMother()),
            validateOtherParent(caseData.getOtherParent()),
            validatePlacementOrders(caseData.getPlacementOrders()),
            validateSiblingOrders(caseData.getSiblings(), caseData.getHasSiblings())
        );
    }

    private static List<String> validateSiblingOrders(List<ListValue<Sibling>> siblings, String hasSiblings) {
        if (YES.equalsIgnoreCase(hasSiblings)) {
            return flattenLists(
                validateSiblingRelation(siblings),
                validateSiblingOrderType(siblings),
                validateSiblingOrderOtherType(siblings),
                validateSiblingOrderNumber(siblings)
            );
        }
        return emptyList();
    }

    private static List<String> validateSiblingOrderOtherType(List<ListValue<Sibling>> siblings) {
        boolean emptySiblingOrderOtherType = nonNull(siblings) && siblings
            .stream().anyMatch(siblingOrderListValue -> {
                if (SiblingPoType.OTHER.equals(siblingOrderListValue.getValue().getSiblingPoType())) {
                    return isEmpty(siblingOrderListValue.getValue().getSiblingPlacementOtherType());
                }
                return false;
            });
        return emptySiblingOrderOtherType ? List.of("SiblingRelationshipsOtherType" + EMPTY) : emptyList();
    }

    private static List<String> validateSiblingOrderNumber(List<ListValue<Sibling>> siblings) {
        boolean emptySiblingOrderNumber = nonNull(siblings) && siblings
            .stream().anyMatch(siblingOrderListValue -> isEmpty(siblingOrderListValue.getValue().getSiblingPoNumber()));
        return emptySiblingOrderNumber ? List.of("SiblingRelationships" + EMPTY) : emptyList();
    }

    private static List<String> validateSiblingOrderType(List<ListValue<Sibling>> siblings) {
        boolean emptySiblingOrderType = nonNull(siblings) && siblings
            .stream().anyMatch(siblingOrderListValue -> isEmpty(siblingOrderListValue.getValue().getSiblingPoType()));

        return emptySiblingOrderType ? List.of("SiblingRelationships" + EMPTY) : emptyList();
    }

    private static List<String> validateSiblingRelation(List<ListValue<Sibling>> siblings) {
        boolean emptySiblingRelation = nonNull(siblings) && siblings
            .stream().anyMatch(siblingOrderListValue -> isEmpty(siblingOrderListValue.getValue().getSiblingRelation()));
        return emptySiblingRelation ? List.of("SiblingRelationships" + EMPTY) : emptyList();
    }

    private static List<String> validatePlacementOrders(List<ListValue<PlacementOrder>> placementOrders) {
        return flattenLists(
            validatePlacementOrderNumber(placementOrders),
            validatePlacementOrderType(placementOrders),
            validatePlacementOtherOrderType(placementOrders),
            validatePlacementOrderCourt(placementOrders),
            validatePlacementOrderDate(placementOrders)
        );
    }

    private static List<String> validatePlacementOtherOrderType(List<ListValue<PlacementOrder>> placementOrders) {
        boolean emptyPlacementOrderOtherType = nonNull(placementOrders) && placementOrders
            .stream().anyMatch(placementOrderListValue -> {
                if (PlacementOrderType.OTHER.equals(placementOrderListValue.getValue().getPlacementOrderType())) {
                    return isEmpty(placementOrderListValue.getValue().getOtherPlacementOrderType());
                }
                return false;
            });
        return emptyPlacementOrderOtherType ? List.of("PlacementOrderOtherType" + EMPTY) : emptyList();
    }

    private static List<String> validatePlacementOrderDate(List<ListValue<PlacementOrder>> placementOrders) {
        boolean emptyPlacementOrderDate = nonNull(placementOrders) && placementOrders
            .stream().anyMatch(placementOrderListValue -> isEmpty(placementOrderListValue.getValue().getPlacementOrderDate()));
        return emptyPlacementOrderDate ? List.of("PlacementOrderDate" + EMPTY) : emptyList();
    }

    private static List<String> validatePlacementOrderCourt(List<ListValue<PlacementOrder>> placementOrders) {
        boolean emptyPlacementOrderCourt = nonNull(placementOrders) && placementOrders
            .stream().skip(1).anyMatch(placementOrderListValue -> isEmpty(placementOrderListValue.getValue().getPlacementOrderCourt()));
        return emptyPlacementOrderCourt ? List.of("PlacementOrderCourt" + EMPTY) : emptyList();
    }

    private static List<String> validatePlacementOrderType(List<ListValue<PlacementOrder>> placementOrders) {
        boolean emptyPlacementOrderType = nonNull(placementOrders) && placementOrders
            .stream().skip(1).anyMatch(placementOrderListValue -> isEmpty(placementOrderListValue.getValue().getPlacementOrderType()));
        return emptyPlacementOrderType ? List.of("PlacementOrderType" + EMPTY) : emptyList();
    }

    private static List<String> validatePlacementOrderNumber(List<ListValue<PlacementOrder>> placementOrders) {
        boolean emptyPlacementOrderNumber = nonNull(placementOrders) && placementOrders
            .stream().anyMatch(placementOrderListValue -> isEmpty(placementOrderListValue.getValue().getPlacementOrderNumber()));
        return emptyPlacementOrderNumber ? List.of("PlacementOrderNumber" + EMPTY) : emptyList();
    }

    private static List<String> validateBirthMotherParent(Parent birthMother) {
        return flattenLists(
            validateParentDetails(birthMother),
            validateParentIfAlive(birthMother)
        );
    }

    private static List<String> validateBirthFatherParent(Parent birthFather) {
        if (YES.equalsIgnoreCase(birthFather.getNameOnCertificate())) {
            return flattenLists(
                validateParentDetails(birthFather),
                validateParentIfAlive(birthFather)
            );
        }

        return emptyList();
    }

    private static List<String> validateParentAddress(Parent parent) {
        if (YesOrNo.YES.equals(parent.getAddressKnown())) {
            return flattenLists(
                notNull(parent.getAddress1(), "BirthFatherAddress1"),
                notNull(parent.getAddress2(), "BirthFatherAddress2"),
                notNull(parent.getAddressPostCode(), "BirthFatherAddressPostCode"),
                notNull(parent.getAddressCountry(), "BirthFatherAddressCountry"),
                notNull(parent.getLastAddressDate(), "BirthFatherLastAddressDate")
            );
        } else if (YesOrNo.NO.equals(parent.getAddressKnown())) {
            return flattenLists(
                notNull(parent.getAddressNotKnownReason(), "BirthFatherAddressNotKnownReason")
            );
        }
        return emptyList();
    }

    private static List<String> validateParentIfAlive(Parent parent) {
        if (YES.equalsIgnoreCase(parent.getStillAlive())) {
            return flattenLists(
                notNull(parent.getNationality(), "BirthFatherNationality"),
                notNull(parent.getOccupation(), "BirthFatherOccupation"),
                notNull(parent.getAddressKnown(), "BirthFatherAddressKnown"),
                validateParentAddress(parent)
            );
        } else if (NOT_SURE.equalsIgnoreCase(parent.getStillAlive())) {
            return flattenLists(
                notNull(parent.getNotAliveReason(), "BirthFatherNotAliveReason")
            );
        }
        return emptyList();
    }

    private static List<String> validateParentDetails(Parent parent) {
        return flattenLists(
            notNull(parent.getFirstName(), "BirthFatherFirstName"),
            notNull(parent.getLastName(), "BirthFatherLastName"),
            notNull(parent.getStillAlive(), "BirthFatherStillAlive")
        );
    }

    private static List<String> validateChildren(Children children) {
        return flattenLists(
            notNull(children.getFirstName(), "ChildrenFirstName"),
            notNull(children.getLastName(), "ChildrenLastName"),
            notNull(children.getDateOfBirth(), "ChildrenDateOfBirth"),
            notNull(children.getSexAtBirth(), "ChildrenSexAtBirth"),
            notNull(children.getNationality(), "childrenNationality")
        );
    }

    public static List<String> validateDateChildMovedIn(LocalDate dateChildMovedIn, String field) {
        if (nonNull(dateChildMovedIn) && dateChildMovedIn.isAfter(LocalDate.now().minus(10, WEEKS))) {
            return List.of(field + LESS_THAN_TEN_WEEKS_AGO);
        }
        return emptyList();
    }

    private static List<String> hasStatementOfTruth(YesOrNo statementOfTruth, String message) {
        return YesOrNo.YES.equals(statementOfTruth) ? emptyList() : List.of(message);
    }

    public static List<String> notNull(Object value, String field) {
        return isEmpty(value) ? List.of(field + EMPTY) : emptyList();
    }

    @SafeVarargs
    public static <E> List<E> flattenLists(List<E>... lists) {
        return Arrays.stream(lists).flatMap(Collection::stream).collect(toList());
    }
}
