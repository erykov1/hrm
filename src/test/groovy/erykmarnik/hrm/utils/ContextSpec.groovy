package erykmarnik.hrm.utils

import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.user.sample.UserSample
import spock.lang.Specification

class ContextSpec extends Specification implements UserSample {
  def loginUser(long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
  }

  def logoutUser() {
    ContextHolder.clear()
  }
}
