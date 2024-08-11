package erykmarnik.hrm.assignments.exception;

public class AlreadyAssignedException extends RuntimeException {
  public AlreadyAssignedException(Long userId, Long objectId) {
    super("user: " + userId + " is already assigned to object " + objectId);
  }
}
