package erykmarnik.hrm.assignments.domain

import erykmarnik.hrm.assignments.dto.AssignmentDto
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto
import erykmarnik.hrm.assignments.dto.CreateAssignmentDto
import erykmarnik.hrm.assignments.sample.AssignmentSample
import erykmarnik.hrm.integration.IntegrationSpec
import erykmarnik.hrm.task.domain.TaskApiFacade
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.task.sample.TaskSample
import erykmarnik.hrm.user.domain.UserApiFacade
import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.user.dto.UserDto
import erykmarnik.hrm.user.sample.UserSample
import erykmarnik.hrm.utils.ContextHolder
import erykmarnik.hrm.utils.TimeApiFacade
import erykmarnik.hrm.utils.sample.TimeSample

class AssignmentAcceptanceSpec extends IntegrationSpec implements UserSample, AssignmentSample, TaskSample, TimeSample {
  private AssignmentApiFacade assignmentApiFacade
  private UserApiFacade userApiFacade
  private TaskApiFacade taskApiFacade
  private TimeApiFacade timeApiFacade
  private UserDto jane
  private UserDto mike
  private TaskDto onboarding
  private AssignmentDto assignment


  def setup() {
    assignmentApiFacade = new AssignmentApiFacade(mockMvc, objectMapper)
    userApiFacade = new UserApiFacade(mockMvc, objectMapper)
    taskApiFacade = new TaskApiFacade(mockMvc, objectMapper)
    timeApiFacade = new TimeApiFacade(mockMvc, objectMapper)

    timeApiFacade.useFixedClock(NOW)

    given: "there is admin $jane"
      jane = userApiFacade.createAdmin(createNewUser(username: "jane123", name: "Jane", surname: "Doe", email: "jane@mail.com"))
    and: "there is employee $mike"
      mike = userApiFacade.createAdmin(createNewUser(username: "mike123", name: "Mike", surname: "Smith", email: "mike@mail.com"))
    and: "there is task $onboarding"
      ContextHolder.setUserContext(new UserContext(jane.userId))
      onboarding = taskApiFacade.createTask(createNewTask(createdAt: NOW))
  }

  def cleanup() {
    deleteAssignment()
    userApiFacade.deleteUser(jane.userId)
    userApiFacade.deleteUser(mike.userId)
    taskApiFacade.deleteTask(onboarding.taskId)
    timeApiFacade.useSystemClock()
    ContextHolder.clear()
  }

  def "Should create new assignment"() {
    given: "admin $jane is logged in"
      loginUser(jane.userId)
    when: "admin $jane assignes user $mike to task $onboarding"
      assignment = assignmentApiFacade.createAssignment(new CreateAssignmentDto(mike.userId, onboarding.taskId))
    then: "user $mike is assigned to task $onboarding"
      assignment == createAssignment(assignmentId: assignment.assignmentId, userId: mike.userId, objectId: onboarding.taskId, assignedAt: NOW,
              doneAt: null, assignmentCreatedBy: jane.userId, assignmentStatus: AssignmentStatusDto.NOT_STARTED
      )
  }

  def "Should delete assignment"() {
    given: "admin $jane is logged in"
      ContextHolder.setUserContext(new UserContext(jane.userId))
    and: "admin $jane assignes user $mike to task $onboarding"
      assignment = assignmentApiFacade.createAssignment(new CreateAssignmentDto(mike.userId, onboarding.taskId))
    when: "admin $jane deletes assignment $assignment"
      deleteAssignment()
    then: "assignment is removed"
      assignmentApiFacade.getAllAssignments() == []
  }

  def "Should set assignment to done"() {
    given: "admin $jane is logged in"
      ContextHolder.setUserContext(new UserContext(jane.userId))
    and: "admin $jane assignes user $mike to task $onboarding"
      assignment = assignmentApiFacade.createAssignment(new CreateAssignmentDto(mike.userId, this.onboarding.taskId))
    and: "employee $mike logs in $WEEK_LATER"
      timeApiFacade.useFixedClock(WEEK_LATER)
      ContextHolder.setUserContext(new UserContext(mike.userId))
    when: "employee $mike sets task $onboarding to done status"
      setToDone(mike.userId)
    then: "assignment has status done"
      getAssignment(assignment.assignmentId, mike.userId) == createAssignment(assignmentId: assignment.assignmentId,
              userId: mike.userId, objectId: onboarding.taskId, assignedAt: NOW, doneAt: WEEK_LATER, assignmentCreatedBy: jane.userId,
              assignmentStatus: AssignmentStatusDto.DONE
      )
  }

  void deleteAssignment() {
    if (assignment.assignmentId != null) {
      ContextHolder.setUserContext(new UserContext(jane.userId))
      assignmentApiFacade.deleteAssignment(assignment.assignmentId)
    }
  }

  void setToDone(Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    assignmentApiFacade.setToDone(assignment.assignmentId)
  }

  AssignmentDto getAssignment(Long assignmentId, Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    assignmentApiFacade.getAssignmentById(assignmentId)
  }
}
