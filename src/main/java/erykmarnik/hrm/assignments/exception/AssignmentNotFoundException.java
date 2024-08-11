package erykmarnik.hrm.assignments.exception;

public class AssignmentNotFoundException extends RuntimeException {
  public AssignmentNotFoundException(Long assignmentId) {
    super("assignment with id " + assignmentId + " not found");
  }
}
