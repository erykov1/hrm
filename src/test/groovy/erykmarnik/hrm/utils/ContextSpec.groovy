package erykmarnik.hrm.utils

import erykmarnik.hrm.security.SecurityFacade
import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.user.sample.UserSample
import spock.lang.Specification

class ContextSpec extends Specification implements UserSample {
  SecurityFacade securityFacade = Mock(SecurityFacade.class)

  def loginUser(long userId) {
    ContextHolder.clear()
    ContextHolder.setUserContext(new UserContext(userId))
  }

  def logoutUser() {
    ContextHolder.clear()
  }
}
