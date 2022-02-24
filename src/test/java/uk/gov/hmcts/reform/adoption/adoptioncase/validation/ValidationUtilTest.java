package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ApplyingWith;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.WEEKS;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.LESS_THAN_TEN_WEEKS_AGO;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.YES;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateBasicCase;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateBirthFather;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateDateChildMovedIn;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.ValidationUtil.validateOtherParent;

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
}
