package erykmarnik.hrm.utils;

import erykmarnik.hrm.user.dto.UserContext;
import erykmarnik.hrm.user.dto.UserRoleDto;
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

  public static Long getUserId() {
    return userContext.get().getUserId();
  }

  public static boolean isAdmin() {
    return userContext.get().getRole().equals(UserRoleDto.ADMIN);
  }

  public static void clear() {
    userContext.remove();
  }
}
