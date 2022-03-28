package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionAgencyOrLocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ApplyingWith;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.WEEKS;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.LESS_THAN_TEN_WEEKS_AGO;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.YES;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateAdoptAgencyOrLAsContactEmail;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateBasicCase;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateBirthFather;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateDateChildMovedIn;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateOtherParent;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL;

public class ValidationUtilTest {

    @Test
    public void shouldValidateBasicCase() {
        CaseData caseData = new CaseData();
        List<String> errors = validateBasicCase(caseData);
        assertThat(errors).hasSize(21);
    }

    @Test
    public void shouldValidateBasicCaseWhenApplyingAlone() {
        CaseData caseData = new CaseData();
        caseData.setApplyingWith(ApplyingWith.ALONE);
        List<String> errors = validateBasicCase(caseData);
        assertThat(errors).hasSize(14);
    }

    @Test
    public void shouldValidateBirthFather() {
        Parent parent = Parent.builder().nameOnCertificate(YES).build();
        List<String> errors = validateBirthFather(parent);
        assertThat(errors).hasSize(2);
    }

    @Test
    public void shouldValidateOtherParent() {
        Parent parent = Parent.builder().stillAlive(YES).build();
        List<String> errors = validateOtherParent(parent);
        assertThat(errors).hasSize(2);
    }

    @Test
    public void shouldReturnErrorWhenDateChileMovedInIsLessThanTenWeeks() {
        LocalDate nineWeeksAgo = LocalDate.now().minus(9, WEEKS);

        List<String> response = validateDateChildMovedIn(nineWeeksAgo, "field");

        assertThat(response).isEqualTo(List.of("field" + LESS_THAN_TEN_WEEKS_AGO));
    }

    @Test
    public void shouldReturnFirstLaIfDoesNotHaveAnotherLa() {
        AdoptionAgencyOrLocalAuthority adoptionAgencyOrLa = AdoptionAgencyOrLocalAuthority.builder()
            .adopAgencyOrLaContactEmail(TEST_USER_EMAIL).build();
        ListValue<AdoptionAgencyOrLocalAuthority> aAOrLaListValue = new ListValue<>(StringUtils.EMPTY, adoptionAgencyOrLa);
        List<ListValue<AdoptionAgencyOrLocalAuthority>> agencyOrLocalAuthorityList = new ArrayList<>();
        agencyOrLocalAuthorityList.add(aAOrLaListValue);

        List<String> response = validateAdoptAgencyOrLAsContactEmail(agencyOrLocalAuthorityList, YesOrNo.NO);

        assertThat(response).hasSize(0);
    }


    @Test
    public void shouldReturnFirstLaIfDoesNotHaveAnotherLaIfSecondIsNotNull() {
        AdoptionAgencyOrLocalAuthority adoptionAgencyOrLa = AdoptionAgencyOrLocalAuthority.builder()
            .adopAgencyOrLaContactEmail(TEST_USER_EMAIL).build();
        ListValue<AdoptionAgencyOrLocalAuthority> aAOrLaListValue = new ListValue<>(StringUtils.EMPTY, adoptionAgencyOrLa);
        List<ListValue<AdoptionAgencyOrLocalAuthority>> agencyOrLocalAuthorityList = new ArrayList<>();
        agencyOrLocalAuthorityList.add(aAOrLaListValue);
        agencyOrLocalAuthorityList.add(aAOrLaListValue);

        List<String> response = validateAdoptAgencyOrLAsContactEmail(agencyOrLocalAuthorityList, YesOrNo.NO);

        assertThat(response).hasSize(0);
    }

    @Test
    public void shouldThrowErrorIfSecondLaEmailIsNull() {
        AdoptionAgencyOrLocalAuthority adoptionAgencyOrLa = AdoptionAgencyOrLocalAuthority.builder()
            .adopAgencyOrLaContactEmail(TEST_USER_EMAIL).build();
        ListValue<AdoptionAgencyOrLocalAuthority> aAOrLaListValue = new ListValue<>(StringUtils.EMPTY, adoptionAgencyOrLa);
        AdoptionAgencyOrLocalAuthority adoptionAgencyOrLaSecond = AdoptionAgencyOrLocalAuthority.builder()
            .adopAgencyOrLaContactEmail(null).build();
        ListValue<AdoptionAgencyOrLocalAuthority> aAOrLaSecondListValue = new ListValue<>(StringUtils.EMPTY, adoptionAgencyOrLaSecond);
        List<ListValue<AdoptionAgencyOrLocalAuthority>> agencyOrLocalAuthorityList = new ArrayList<>();
        agencyOrLocalAuthorityList.add(aAOrLaListValue);
        agencyOrLocalAuthorityList.add(aAOrLaSecondListValue);

        List<String> response = validateAdoptAgencyOrLAsContactEmail(agencyOrLocalAuthorityList, YesOrNo.YES);

        assertThat(response).isEqualTo(List.of("AdoptAgencyOrLaContactEmail cannot be empty or null"));
    }

    @Test
    public void shouldThrowErrorIfFirstLaEmailIsNull() {
        AdoptionAgencyOrLocalAuthority adoptionAgencyOrLa = AdoptionAgencyOrLocalAuthority.builder()
            .adopAgencyOrLaContactEmail(null).build();
        ListValue<AdoptionAgencyOrLocalAuthority> aAOrLaListValue = new ListValue<>(StringUtils.EMPTY, adoptionAgencyOrLa);
        AdoptionAgencyOrLocalAuthority adoptionAgencyOrLaSecond = AdoptionAgencyOrLocalAuthority.builder()
            .adopAgencyOrLaContactEmail(TEST_USER_EMAIL).build();
        ListValue<AdoptionAgencyOrLocalAuthority> aAOrLaSecondListValue = new ListValue<>(StringUtils.EMPTY, adoptionAgencyOrLaSecond);
        List<ListValue<AdoptionAgencyOrLocalAuthority>> agencyOrLocalAuthorityList = new ArrayList<>();
        agencyOrLocalAuthorityList.add(aAOrLaListValue);
        agencyOrLocalAuthorityList.add(aAOrLaSecondListValue);

        List<String> response = validateAdoptAgencyOrLAsContactEmail(agencyOrLocalAuthorityList, YesOrNo.NO);

        assertThat(response).isEqualTo(List.of("AdoptAgencyOrLaContactEmail cannot be empty or null"));
    }
}
