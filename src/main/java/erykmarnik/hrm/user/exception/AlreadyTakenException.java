package erykmarnik.hrm.user.exception;

public class AlreadyTakenException extends RuntimeException {
  public AlreadyTakenException(String value) {
    super(value + " is already taken");
  }
}
