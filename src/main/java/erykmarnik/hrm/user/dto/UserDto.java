package erykmarnik.hrm.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@EqualsAndHashCode
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
