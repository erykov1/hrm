package erykmarnik.hrm.utils

import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.user.dto.UserRoleDto
import erykmarnik.hrm.user.sample.UserSample
import spock.lang.Specification

class ContextSpec extends Specification implements UserSample {
  def loginUser(long userId, UserRoleDto role = UserRoleDto.ADMIN) {
    ContextHolder.clear()
    ContextHolder.setUserContext(new UserContext(userId, role))
  }
}
