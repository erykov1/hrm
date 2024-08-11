package erykmarnik.hrm.assignments.exception;

public class ForbiddenAssignmentOperationException extends RuntimeException {
  public ForbiddenAssignmentOperationException(Long assignmentId) {
    super("Forbidden assignment operation in assignment: " + assignmentId);
  }
}
