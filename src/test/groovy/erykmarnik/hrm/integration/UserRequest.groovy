package erykmarnik.hrm.integration

import erykmarnik.hrm.user.dto.UserDto
import erykmarnik.hrm.user.dto.UserRoleDto

class UserRequest {
  Long userId
  UserRoleDto userRole

  UserRequest(UserDto user) {
    this.userId = user.getUserId()
    this.userRole = user.getUserRole()
  }
}
