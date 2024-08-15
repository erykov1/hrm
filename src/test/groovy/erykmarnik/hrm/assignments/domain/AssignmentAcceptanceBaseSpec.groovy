package erykmarnik.hrm.assignments.domain

import erykmarnik.hrm.assignments.dto.AssignmentDto
import erykmarnik.hrm.assignments.dto.CreateAssignmentDto
import erykmarnik.hrm.assignments.sample.AssignmentSample
import erykmarnik.hrm.integration.IntegrationSpec
import erykmarnik.hrm.task.domain.TaskApiFacade
import erykmarnik.hrm.task.sample.TaskSample
import erykmarnik.hrm.user.domain.UserApiFacade
import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.user.sample.UserSample
import erykmarnik.hrm.utils.ContextHolder
import erykmarnik.hrm.utils.TimeApiFacade
import erykmarnik.hrm.utils.sample.TimeSample

class AssignmentAcceptanceBaseSpec extends IntegrationSpec implements UserSample, AssignmentSample, TaskSample, TimeSample {
  AssignmentApiFacade assignmentApiFacade
  UserApiFacade userApiFacade
  TaskApiFacade taskApiFacade
  TimeApiFacade timeApiFacade

  def setup() {
    assignmentApiFacade = new AssignmentApiFacade(mockMvc, objectMapper)
    userApiFacade = new UserApiFacade(mockMvc, objectMapper)
    taskApiFacade = new TaskApiFacade(mockMvc, objectMapper)
    timeApiFacade = new TimeApiFacade(mockMvc, objectMapper)
  }

  void deleteAssignment(Long assignmentId, Long userId) {
    if (assignmentId != null) {
      ContextHolder.setUserContext(new UserContext(userId))
      assignmentApiFacade.deleteAssignment(assignmentId)
    }
  }

  void setToDone(Long assignmentId, Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    assignmentApiFacade.setToDone(assignmentId)
  }

  AssignmentDto getAssignment(Long assignmentId, Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    assignmentApiFacade.getAssignmentById(assignmentId)
  }

  List<AssignmentDto> getUserAssignment(Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    assignmentApiFacade.getUserAssignments()
  }

  AssignmentDto createAssignment(Long userId, CreateAssignmentDto createAssignment) {
    ContextHolder.setUserContext(new UserContext(userId))
    assignmentApiFacade.createAssignment(createAssignment)
  }
}
