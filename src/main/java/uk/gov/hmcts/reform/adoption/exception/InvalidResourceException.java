package uk.gov.hmcts.reform.adoption.exception;

import java.io.Serial;

public class InvalidResourceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7442994120484411077L;

    public InvalidResourceException(String message) {
        super(message);
    }

    public InvalidResourceException(String message, Exception cause) {
        super(message, cause);
    }
}
