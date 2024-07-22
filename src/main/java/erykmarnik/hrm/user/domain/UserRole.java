package erykmarnik.hrm.user.domain;

import erykmarnik.hrm.user.dto.UserRoleDto;

enum UserRole {
  ADMIN,
  EMPLOYEE;

  UserRoleDto dto() {
    return UserRoleDto.valueOf(name());
  }
}
