package uk.gov.hmcts.reform.adoption.framework.exceptions;

public class WorkflowException extends Exception {

    private static final long serialVersionUID = -909516530385203706L;

    public WorkflowException(String message, Throwable cause) {
        super(message, cause);
    }
}
