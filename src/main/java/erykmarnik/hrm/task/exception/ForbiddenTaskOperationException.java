package erykmarnik.hrm.task.exception;

public class ForbiddenTaskOperationException extends RuntimeException {
  public ForbiddenTaskOperationException() {
    super("Cannot do current task operation");
  }
}
