package erykmarnik.hrm.user.exception;

public class InvalidEmailException extends RuntimeException {
  public InvalidEmailException(String email) {
    super(email + " is invalid");
  }
}
