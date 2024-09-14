package erykmarnik.hrm.task.exception;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {
  public TaskNotFoundException(UUID taskId) {
    super("task with id: " + taskId + " not found");
  }
}
