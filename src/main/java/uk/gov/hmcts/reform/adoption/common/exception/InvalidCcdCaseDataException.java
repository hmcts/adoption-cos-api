package uk.gov.hmcts.reform.adoption.common.exception;

public class InvalidCcdCaseDataException extends RuntimeException {

    private static final long serialVersionUID = 1888206621734610397L;

    public InvalidCcdCaseDataException(final String message) {
        super(message);
    }
}
