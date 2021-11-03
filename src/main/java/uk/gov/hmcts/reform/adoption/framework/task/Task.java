package uk.gov.hmcts.reform.adoption.framework.task;

import uk.gov.hmcts.reform.adoption.framework.context.TaskContext;
import uk.gov.hmcts.reform.adoption.framework.exceptions.TaskException;

@FunctionalInterface
public interface Task<T> {
    T execute(TaskContext context, T payload) throws TaskException;
}
