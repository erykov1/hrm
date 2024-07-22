package erykmarnik.hrm.user.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(Long id) {
    super("user " + id + " not found");
  }
}
