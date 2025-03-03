package erykmarnik.hrm.assignments.domain

import erykmarnik.hrm.assignments.dto.AssignmentDto
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto
import erykmarnik.hrm.integration.UserRequest
import erykmarnik.hrm.task.dto.CategoryDto
import erykmarnik.hrm.task.dto.CreateCategoryDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.user.dto.UserDto

class AssignmentAcceptanceSpec extends AssignmentAcceptanceBaseSpec {
  private UserDto jane
  private UserDto mike
  private TaskDto onboarding
  private CategoryDto onboardingCategory
  private long assignmentId

  def setup() {
    timeApiFacade.useFixedClock(NOW)

    given: "there is admin $jane"
      jane = userApiFacade.createAdmin(createNewUser(username: "jane123", name: "Jane", surname: "Doe", email: "jane@mail.com"))
    and: "there is employee $mike"
      mike = userApiFacade.createEmployee(createNewUser(username: "mike123", name: "Mike", surname: "Smith", email: "mike@mail.com"))
    and: "there is category $onboardingCategory"
      onboardingCategory = categoryApiFacade.createCategory(new CreateCategoryDto(ONBOARDING), new UserRequest(jane))
    and: "there is task $onboarding"
      onboarding = taskApiFacade.createTask(createNewTask(createdAt: NOW, categoryId: onboardingCategory.categoryId), new UserRequest(jane))
  }

  def cleanup() {
    deleteAssignment(assignmentId, new UserRequest(jane))
    userApiFacade.deleteUser(mike.userId, new UserRequest(mike))
    taskApiFacade.deleteTask(onboarding.taskId, new UserRequest(jane))
    categoryApiFacade.deleteCategory(onboardingCategory.categoryId, new UserRequest(jane))
    userApiFacade.deleteUser(jane.userId, new UserRequest(jane))
    timeApiFacade.useSystemClock()
  }

  def "Should create new assignment"() {
    when: "admin $jane assignes user $mike to task $onboarding"
      assignmentId = assignmentApiFacade.createAssignment(createNewAssignment(userId: mike.userId, objectId: onboarding.taskId), new UserRequest(jane)).assignmentId
    then: "user $mike is assigned to task $onboarding"
      assignmentApiFacade.getAllAssignments(new UserRequest(jane)) == [createAssignment(assignmentId: assignmentId, userId: mike.userId,
              objectId: onboarding.taskId, assignedAt: NOW, doneAt: null, assignmentCreatedBy: jane.userId,
              assignmentStatus: AssignmentStatusDto.NOT_STARTED
      )]
  }

  def "Should delete assignment"() {
    given: "admin $jane assignes user $mike to task $onboarding"
      assignmentId = assignmentApiFacade.createAssignment(createNewAssignment(userId: mike.userId, objectId: onboarding.taskId), new UserRequest(jane)).assignmentId
    when: "admin $jane deletes assignment $assignmentId"
      assignmentId = deleteAssignment(assignmentId, new UserRequest(jane))
    then: "assignment is removed"
      assignmentApiFacade.getAllAssignments(new UserRequest(jane)) == []
  }

  def "Should set assignment to done"() {
    given: "admin $jane assignes user $mike to task $onboarding"
      assignmentId = assignmentApiFacade.createAssignment(createNewAssignment(userId: mike.userId, objectId: onboarding.taskId), new UserRequest(jane)).assignmentId
    when: "employee $mike sets task $onboarding to done status $WEEK_LATER"
      timeApiFacade.useFixedClock(WEEK_LATER)
      assignmentApiFacade.setToDone(assignmentId, new UserRequest(mike))
    then: "assignment has status done"
      assignmentApiFacade.getUserAssignments(new UserRequest(mike)) == [createAssignment(assignmentId: assignmentId, userId: mike.userId, objectId: onboarding.taskId,
              assignedAt: NOW, doneAt: WEEK_LATER, assignmentCreatedBy: jane.userId, assignmentStatus: AssignmentStatusDto.DONE
      )]
  }

  def "Should get user assignment"() {
    given: "admin $jane assignes user $mike to task $onboarding"
      assignmentId = assignmentApiFacade.createAssignment(createNewAssignment(userId: mike.userId, objectId: onboarding.taskId), new UserRequest(jane)).assignmentId
    when: "$mike asks for assignments"
      List<AssignmentDto> assignments = assignmentApiFacade.getUserAssignments(new UserRequest(mike))
    then: "$mike gets his all assignments"
      assignments == [createAssignment(assignmentId: assignmentId, userId: mike.userId, objectId: onboarding.taskId, assignedAt: NOW,
              doneAt: null, assignmentCreatedBy: jane.userId, assignmentStatus: AssignmentStatusDto.NOT_STARTED
      )]
  }

  def "Should create assignment with due date"() {
    when: "admin $jane assignes user $mike to task $onboarding with due date $WEEK_LATER"
      assignmentId = assignmentApiFacade.createAssignment(createNewAssignment(userId: mike.userId, objectId: onboarding.taskId, dueTo: WEEK_LATER), new UserRequest(jane)).assignmentId
    then: "assignment is created with due date"
      assignmentApiFacade.getAllAssignments(new UserRequest(jane)) == [createAssignment(assignmentId: assignmentId, userId: mike.userId, objectId: onboarding.taskId, assignedAt: NOW,
              doneAt: null, assignmentCreatedBy: jane.userId, assignmentStatus: AssignmentStatusDto.NOT_STARTED, dueTo: WEEK_LATER
      )]
  }

  def "Should terminate assignment when user not complete it"() {
    given: "admin $jane assignes user $mike to task $onboarding with due date $THREE_DAYS_LATER"
      assignmentId = assignmentApiFacade.createAssignment(createNewAssignment(userId: mike.userId, objectId: onboarding.taskId, dueTo: THREE_DAYS_LATER), new UserRequest(jane)).assignmentId
    when: "current time is $WEEK_LATER"
      timeApiFacade.useFixedClock(WEEK_LATER)
      assignmentApiFacade.outDateNotStarted()
    then: "assignment $assignmentId is terminated"
      assignmentApiFacade.getAllAssignments(new UserRequest(jane)) == [createAssignment(assignmentId: assignmentId, userId: mike.userId, objectId: onboarding.taskId, assignedAt: NOW,
              doneAt: null, assignmentCreatedBy: jane.userId, assignmentStatus: AssignmentStatusDto.OVERDUE, dueTo: THREE_DAYS_LATER
      )]
  }

  def "Should not terminate assignment if current time is before due date"() {
    given: "admin $jane assignes user $mike to task $onboarding with due date $WEEK_LATER"
      assignmentId = assignmentApiFacade.createAssignment(createNewAssignment(userId: mike.userId, objectId: onboarding.taskId, dueTo: WEEK_LATER), new UserRequest(jane)).assignmentId
    when: "current time is $THREE_DAYS_LATER"
      timeApiFacade.useFixedClock(THREE_DAYS_LATER)
      assignmentApiFacade.outDateNotStarted()
    then: "assignment $assignmentId is not terminated"
      assignmentApiFacade.getAllAssignments(new UserRequest(jane)) == [createAssignment(assignmentId: assignmentId, userId: mike.userId, objectId: onboarding.taskId, assignedAt: NOW,
              doneAt: null, assignmentCreatedBy: jane.userId, assignmentStatus: AssignmentStatusDto.NOT_STARTED, dueTo: WEEK_LATER
      )]
  }
}
