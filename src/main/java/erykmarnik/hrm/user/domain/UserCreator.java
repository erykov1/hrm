package erykmarnik.hrm.user.domain;

import erykmarnik.hrm.user.dto.CreateUserDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserCreator {
  User createEmployee(CreateUserDto employee) {
    return User.builder()
            .username(employee.getUsername())
            .name(employee.getName())
            .surname(employee.getSurname())
            .email(employee.getEmail())
            .password(employee.getPassword())
            .userRole(UserRole.EMPLOYEE)
            .build();
  }

  User createAdmin(CreateUserDto admin) {
    return User.builder()
            .username(admin.getUsername())
            .name(admin.getName())
            .surname(admin.getSurname())
            .email(admin.getEmail())
            .password(admin.getPassword())
            .userRole(UserRole.ADMIN)
            .build();
  }
}
