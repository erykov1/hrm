package erykmarnik.hrm.assignments.domain

import erykmarnik.hrm.assignments.dto.AssignmentDto
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto
import erykmarnik.hrm.assignments.dto.CreateAssignmentDto
import erykmarnik.hrm.task.dto.CategoryDto
import erykmarnik.hrm.task.dto.CreateCategoryDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.user.dto.UserDto
import erykmarnik.hrm.utils.ContextHolder

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
      ContextHolder.setUserContext(new UserContext(jane.userId))
      onboardingCategory = categoryApiFacade.createCategory(new CreateCategoryDto(ONBOARDING))
    and: "there is task $onboarding"
      ContextHolder.setUserContext(new UserContext(jane.userId))
      onboarding = taskApiFacade.createTask(createNewTask(createdAt: NOW, categoryId: onboardingCategory.categoryId))
  }

  def cleanup() {
    deleteAssignment(assignmentId, jane.userId)
    userApiFacade.deleteUser(jane.userId)
    userApiFacade.deleteUser(mike.userId)
    taskApiFacade.deleteTask(onboarding.taskId)
    categoryApiFacade.deleteCategory(onboardingCategory.categoryId)
    timeApiFacade.useSystemClock()
    ContextHolder.clear()
  }

  def "Should create new assignment"() {
    when: "admin $jane assignes user $mike to task $onboarding"
      assignmentId = createAssignment(jane.userId, new CreateAssignmentDto(mike.userId, onboarding.taskId)).assignmentId
    then: "user $mike is assigned to task $onboarding"
      getAssignment(assignmentId, jane.userId) == createAssignment(assignmentId: assignmentId, userId: mike.userId,
              objectId: onboarding.taskId, assignedAt: NOW, doneAt: null, assignmentCreatedBy: jane.userId,
              assignmentStatus: AssignmentStatusDto.NOT_STARTED
      )
  }

  def "Should delete assignment"() {
    given: "admin $jane assignes user $mike to task $onboarding"
      assignmentId = createAssignment(jane.userId, new CreateAssignmentDto(mike.userId, onboarding.taskId)).assignmentId
    when: "admin $jane deletes assignment $assignmentId"
      assignmentId = deleteAssignment(assignmentId, jane.userId)
    then: "assignment is removed"
      assignmentApiFacade.getAllAssignments() == []
  }

  def "Should set assignment to done"() {
    given: "admin $jane assignes user $mike to task $onboarding"
      assignmentId = createAssignment(jane.userId, new CreateAssignmentDto(mike.userId, onboarding.taskId)).assignmentId
    and: "employee $mike logs in $WEEK_LATER"
      timeApiFacade.useFixedClock(WEEK_LATER)
      ContextHolder.setUserContext(new UserContext(mike.userId))
    when: "employee $mike sets task $onboarding to done status"
      setToDone(assignmentId, mike.userId)
    then: "assignment has status done"
      getAssignment(assignmentId, mike.userId) == createAssignment(assignmentId: assignmentId, userId: mike.userId, objectId: onboarding.taskId,
              assignedAt: NOW, doneAt: WEEK_LATER, assignmentCreatedBy: jane.userId, assignmentStatus: AssignmentStatusDto.DONE
      )
  }

  def "Should get user assignment"() {
    given: "admin $jane assignes user $mike to task $onboarding"
      assignmentId = createAssignment(jane.userId, new CreateAssignmentDto(mike.userId, onboarding.taskId)).assignmentId
    when: "$mike asks for assignments"
      List<AssignmentDto> assignments = getUserAssignment(mike.userId)
    then: "$mike gets his all assignments"
      assignments == [createAssignment(assignmentId: assignmentId, userId: mike.userId, objectId: onboarding.taskId, assignedAt: NOW,
              doneAt: null, assignmentCreatedBy: jane.userId, assignmentStatus: AssignmentStatusDto.NOT_STARTED
      )]
  }
}
