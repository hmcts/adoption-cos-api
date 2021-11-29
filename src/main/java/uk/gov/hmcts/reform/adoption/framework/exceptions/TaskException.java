package uk.gov.hmcts.reform.adoption.framework.exceptions;

public class TaskException extends RuntimeException {

    private static final long serialVersionUID = -387804752917217880L;

    public TaskException(String message) {
        super(message);
    }
}
