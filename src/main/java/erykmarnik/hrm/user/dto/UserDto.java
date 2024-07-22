package erykmarnik.hrm.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDto {
  Long userId;
  String username;
  String name;
  String surname;
  String email;
  String password;
  UserRoleDto userRole;
}
