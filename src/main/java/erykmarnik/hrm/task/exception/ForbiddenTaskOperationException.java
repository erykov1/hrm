package erykmarnik.hrm.task.exception;

public class ForbiddenTaskOperationException extends RuntimeException {
  public ForbiddenTaskOperationException(Long taskId) {
    super("Cannot modify task: " + taskId);
  }
}
