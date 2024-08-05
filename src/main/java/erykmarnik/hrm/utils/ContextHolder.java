package erykmarnik.hrm.utils;

import erykmarnik.hrm.user.dto.UserContext;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContextHolder {
  private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

  public static void setUserContext(UserContext context) {
    userContext.set(context);
  }

  public static UserContext getUserContext() {
    return userContext.get();
  }

  public static void clear() {
    userContext.remove();
  }
}
