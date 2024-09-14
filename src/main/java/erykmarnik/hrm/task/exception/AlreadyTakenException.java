package erykmarnik.hrm.task.exception;


public class AlreadyTakenException extends RuntimeException {
  public AlreadyTakenException(String msg) {
    super(msg);
  }
}
