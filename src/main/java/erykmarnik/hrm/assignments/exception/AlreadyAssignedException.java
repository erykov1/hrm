package erykmarnik.hrm.assignments.exception;

import java.util.UUID;

public class AlreadyAssignedException extends RuntimeException {
  public AlreadyAssignedException(Long userId, UUID objectId) {
    super("user: " + userId + " is already assigned to object " + objectId);
  }
}
