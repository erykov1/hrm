package erykmarnik.hrm.user.domain;

import erykmarnik.hrm.user.exception.InvalidEmailException;

import java.util.regex.Pattern;

class EmailValidator {
  private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
  private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

  static void validateUserEmailData(String email) {
    if (email.isEmpty() || !pattern.matcher(email).matches()) {
      throw new InvalidEmailException(email);
    }
  }
}
