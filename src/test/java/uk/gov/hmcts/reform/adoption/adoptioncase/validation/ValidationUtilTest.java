package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionAgencyOrLocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ApplyingWith;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.WEEKS;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.LESS_THAN_TEN_WEEKS_AGO;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.YES;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateLocalAuthorityAndAdoptionAgency;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateBasicCase;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateBirthFather;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateDateChildMovedIn;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateOtherParent;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_PHONE_NUMBER;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL;

public class ValidationUtilTest {

    @Test
    public void shouldValidateBasicCase() {
        CaseData caseData = new CaseData();
        caseData.setHasAnotherAdopAgencyOrLA(YesOrNo.NO);
        List<String> errors = validateBasicCase(caseData);
        assertThat(errors).hasSize(14);
    }

    @Test
    public void shouldValidateBasicCaseWhenApplyingAlone() {
        CaseData caseData = new CaseData();
        caseData.setApplyingWith(ApplyingWith.ALONE);
        caseData.setHasAnotherAdopAgencyOrLA(YesOrNo.NO);
        List<String> errors = validateBasicCase(caseData);
        assertThat(errors).hasSize(7);
    }

    @Test
    public void shouldValidateBirthFather() {
        Parent parent = Parent.builder().nameOnCertificate(YES).build();
        List<String> errors = validateBirthFather(parent);
        assertThat(errors).hasSize(2);
    }

    @Test
    public void shouldValidateOtherParent() {
        Parent parent = Parent.builder().stillAlive(YesOrNo.YES).build();
        List<String> errors = validateOtherParent(parent);
        assertThat(errors).hasSize(2);
    }

    @Test
    void shouldValidateOtherParent2() {
        Parent parent = Parent.builder().stillAlive(YesOrNo.NO).build();
        List<String> errors = validateOtherParent(parent);
        assertThat(errors).isEmpty();
    }

    @Test
    public void shouldReturnErrorWhenDateChileMovedInIsLessThanTenWeeks() {
        LocalDate nineWeeksAgo = LocalDate.now().minus(9, WEEKS);

        List<String> response = validateDateChildMovedIn(nineWeeksAgo, "field");

        assertThat(response).isEqualTo(List.of("field" + LESS_THAN_TEN_WEEKS_AGO));
    }

    @Test
    public void shouldValidateIfFirstLaIsValid() {
        LocalAuthority localAuthority = LocalAuthority.builder()
            .localAuthorityContactEmail(TEST_USER_EMAIL).localAuthorityPhoneNumber(TEST_USER_EMAIL).build();

        List<String> list = validateLocalAuthorityAndAdoptionAgency(localAuthority, null, YesOrNo.NO);
        assertThat(list).isEmpty();
    }

    @Test
    public void shouldValidateIfBothLasAreValid() {
        LocalAuthority localAuthority = LocalAuthority.builder()
            .localAuthorityContactEmail(TEST_USER_EMAIL).localAuthorityPhoneNumber(TEST_PHONE_NUMBER).build();
        AdoptionAgencyOrLocalAuthority adoptionAgencyOrLa = AdoptionAgencyOrLocalAuthority.builder()
            .adopAgencyOrLaContactEmail(TEST_USER_EMAIL).adopAgencyOrLaPhoneNumber(TEST_PHONE_NUMBER).build();

        List<String> list = validateLocalAuthorityAndAdoptionAgency(localAuthority, adoptionAgencyOrLa, YesOrNo.YES);
        assertThat(list).isEmpty();
    }
}
