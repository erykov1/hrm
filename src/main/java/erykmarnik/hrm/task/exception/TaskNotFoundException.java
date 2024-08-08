package erykmarnik.hrm.task.exception;

public class TaskNotFoundException extends RuntimeException {
  public TaskNotFoundException(Long taskId) {
    super("task with id: " + taskId + " not found");
  }
}
