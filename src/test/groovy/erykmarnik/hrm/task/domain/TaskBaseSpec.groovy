package erykmarnik.hrm.task.domain

import erykmarnik.hrm.security.SecurityFacade
import erykmarnik.hrm.task.sample.TaskSample
import erykmarnik.hrm.utils.ContextSpec
import erykmarnik.hrm.utils.InstantProvider
import erykmarnik.hrm.utils.sample.TimeSample

class TaskBaseSpec extends ContextSpec implements TaskSample, TimeSample, CategorySample {
  SecurityFacade securityFacade = Stub(SecurityFacade.class)
  InstantProvider instantProvider = new InstantProvider()
  TaskFacade taskFacade = new TaskConfiguration().taskFacade(instantProvider, securityFacade)
}
