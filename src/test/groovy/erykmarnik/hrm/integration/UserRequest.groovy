package erykmarnik.hrm.integration

import erykmarnik.hrm.user.dto.UserDto

class UserRequest {
  Long userId

  UserRequest(Long userId) {
    this.userId = userId
  }

  UserRequest(UserDto user) {
    this.userId = user.getUserId()
  }
}
