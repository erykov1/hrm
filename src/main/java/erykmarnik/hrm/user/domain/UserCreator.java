package erykmarnik.hrm.user.domain;

import erykmarnik.hrm.user.dto.CreateUserDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserCreator {
  BCryptPasswordEncoder bCryptPasswordEncoder;

  User createEmployee(CreateUserDto employee) {
    return User.builder()
            .username(employee.getUsername())
            .name(employee.getName())
            .surname(employee.getSurname())
            .email(employee.getEmail())
            .password(bCryptPasswordEncoder.encode(employee.getPassword()))
            .userRole(UserRole.EMPLOYEE)
            .build();
  }

  User createAdmin(CreateUserDto admin) {
    return User.builder()
            .username(admin.getUsername())
            .name(admin.getName())
            .surname(admin.getSurname())
            .email(admin.getEmail())
            .password(bCryptPasswordEncoder.encode(admin.getPassword()))
            .userRole(UserRole.ADMIN)
            .build();
  }
}
