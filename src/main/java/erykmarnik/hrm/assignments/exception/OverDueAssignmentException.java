package erykmarnik.hrm.assignments.exception;

public class OverDueAssignmentException extends RuntimeException {
  public OverDueAssignmentException(Long assignmentId) {
    super("Cannot set assignment + " + assignmentId + " to done because it is overdue");
  }
}
