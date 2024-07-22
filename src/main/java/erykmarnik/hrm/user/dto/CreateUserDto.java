package erykmarnik.hrm.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateUserDto {
  String username;
  String name;
  String surname;
  String email;
  String password;
}
