package uk.gov.hmcts.reform.adoption.common.ccd;

public interface CcdPageConfiguration {
    String NEVER_SHOW = "divorceOrDissolution=\"NEVER_SHOW\"";

    void addTo(final PageBuilder pageBuilder);
}
