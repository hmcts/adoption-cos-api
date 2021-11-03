package uk.gov.hmcts.reform.adoption.framework.exceptions;

public class WorkflowException extends Exception {

    public WorkflowException(String message, Throwable cause) {
        super(message, cause);
    }
}
