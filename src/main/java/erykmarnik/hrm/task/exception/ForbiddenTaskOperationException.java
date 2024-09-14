package erykmarnik.hrm.task.exception;

import java.util.UUID;

public class ForbiddenTaskOperationException extends RuntimeException {
  public ForbiddenTaskOperationException(UUID taskId) {
    super("Cannot modify task: " + taskId);
  }
}
