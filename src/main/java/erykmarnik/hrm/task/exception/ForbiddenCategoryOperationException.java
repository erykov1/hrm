package erykmarnik.hrm.task.exception;

public class ForbiddenCategoryOperationException extends RuntimeException {
  public ForbiddenCategoryOperationException(String message) {
    super(message);
  }
}
