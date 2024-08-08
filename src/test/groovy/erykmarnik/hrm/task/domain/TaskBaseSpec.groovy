package erykmarnik.hrm.task.domain

import erykmarnik.hrm.security.SecurityFacade
import erykmarnik.hrm.utils.ContextSpec
import erykmarnik.hrm.utils.InstantProvider

class TaskBaseSpec extends ContextSpec {
  SecurityFacade securityFacade = Stub(SecurityFacade.class)
  InstantProvider instantProvider = new InstantProvider()
  TaskFacade taskFacade = new TaskConfiguration().taskFacade(instantProvider, securityFacade)
}
