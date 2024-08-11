package erykmarnik.hrm.assignments.exception;

public class InvalidAssignmentException extends RuntimeException {
  public InvalidAssignmentException(Long userId, Long objectId) {
    super("cannot create assignment with user " + userId + " and object " + objectId);
  }
}
