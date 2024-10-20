package erykmarnik.hrm.assignments.domain

import erykmarnik.hrm.assignments.dto.AssignmentAnalyticDto
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto
import erykmarnik.hrm.assignments.dto.CreateAssignmentDto
import erykmarnik.hrm.task.dto.CategoryDto
import erykmarnik.hrm.task.dto.CreateCategoryDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.user.dto.UserDto
import erykmarnik.hrm.utils.ContextHolder

class AssignmentAnalyticAcceptanceSpec extends AssignmentAcceptanceBaseSpec {
  private UserDto jane
  private UserDto mike
  private UserDto john
  private TaskDto onboarding
  private CategoryDto newEmployee
  private long assignmentId
  private long johnAssignmentId

  def setup() {
    timeApiFacade.useFixedClock(NOW)

    given: "there is admin $jane"
      jane = userApiFacade.createAdmin(createNewUser(username: "jane123", name: "Jane", surname: "Doe", email: "jane@mail.com"))
    and: "there is employee $mike"
      mike = userApiFacade.createEmployee(createNewUser(username: "mike123", name: "Mike", surname: "Smith", email: "mike@mail.com"))
    and: "there is employee $john"
      john = userApiFacade.createEmployee(createNewUser(username: "john123", name: "John", surname: "Butcher", email: "john@mail.com"))
    and: "there is category $newEmployee"
      newEmployee = createCategoryRequest(new CreateCategoryDto(CATEGORY_NAME), jane.userId)
    and: "there is task $onboarding assigned to category $newEmployee"
      onboarding = createTaskRequest(jane.userId, createNewTask(createdAt: NOW, categoryId: newEmployee.categoryId))
    and: "admin $jane assinges user $mike to task $onboarding"
      assignmentId = createAssignment(jane.userId, new CreateAssignmentDto(mike.userId, onboarding.taskId)).assignmentId
  }

  def cleanup() {
    deleteAssignment(assignmentId, jane.userId)
    deleteAssignment(johnAssignmentId, jane.userId)
    userApiFacade.deleteUser(jane.userId)
    userApiFacade.deleteUser(mike.userId)
    userApiFacade.deleteUser(john.userId)
    deleteTask(onboarding.taskId, jane.userId)
    deleteCategory(newEmployee.categoryId, jane.userId)
    timeApiFacade.useSystemClock()
    ContextHolder.clear()
  }

  def "Should get analytic data for all assignments for user"() {
    when: "admin $jane asks for analytic data for all assignments for user $mike"
      List<AssignmentAnalyticDto> result = assignmentApiFacade.getAllForUser(mike.userId)
    then: "gets all analytic data for all assignments for user $mike"
      result == [createAssignmentAnalytic(userId: mike.userId, username: mike.username, name: mike.name, surname: mike.surname,
              objectName: onboarding.taskName, minutesTakenToDone: EMPTY_MINUTES, startedAt: Date.from(NOW), endedAt: null,
              assignmentStatus: AssignmentStatusDto.NOT_STARTED, category: newEmployee.categoryName)]
    when: "user $mike set task $onboarding to done $WEEK_LATER"
      timeApiFacade.useFixedClock(WEEK_LATER)
      setToDone(assignmentId, mike.userId)
    then: "result for analytic data is changed"
      assignmentApiFacade.getAllForUser(mike.userId) == [createAssignmentAnalytic(userId: mike.userId, username: mike.username, name: mike.name, surname: mike.surname,
              objectName: onboarding.taskName, minutesTakenToDone: MINUTES_FOR_WEEK_LATER, startedAt: Date.from(NOW), endedAt: Date.from(WEEK_LATER),
              assignmentStatus: AssignmentStatusDto.DONE, category: newEmployee.categoryName)]
  }

  def "Should get analytic data for all not started assignments"() {
    given: "admin $jane assignes user $john to task $onboarding $WEEK_LATER"
      timeApiFacade.useFixedClock(WEEK_LATER)
      johnAssignmentId = createAssignment(jane.userId, new CreateAssignmentDto(john.userId, onboarding.taskId)).assignmentId
    when: "admin $jane asks for analytic data for all not started assignments"
      List<AssignmentAnalyticDto> result = assignmentApiFacade.getAllNotStartedAssignments()
    then: "gets all analytic data for all not started assignments"
      equalsAssignments(result, [createAssignmentAnalytic(userId: mike.userId, username: mike.username, name: mike.name, surname: mike.surname,
              objectName: onboarding.taskName, minutesTakenToDone: EMPTY_MINUTES, startedAt: Date.from(NOW), endedAt: null,
              assignmentStatus: AssignmentStatusDto.NOT_STARTED), createAssignmentAnalytic(userId: john.userId, username: john.username,
              name: john.name, surname: john.surname,
              objectName: onboarding.taskName, minutesTakenToDone: EMPTY_MINUTES, startedAt: Date.from(WEEK_LATER), endedAt: null,
              assignmentStatus: AssignmentStatusDto.NOT_STARTED, category: newEmployee.categoryName)])
  }

  def "Should get analytic data for all done assignments"() {
    given: "admin $jane assignes user $john to task $onboarding $WEEK_LATER"
      timeApiFacade.useFixedClock(WEEK_LATER)
      johnAssignmentId = createAssignment(jane.userId, new CreateAssignmentDto(john.userId, onboarding.taskId)).assignmentId
    and: "user $mike set task $onboarding to done"
      setToDone(assignmentId, mike.userId)
    when: "admin $jane asks for analytic data for all done assignments"
      List<AssignmentAnalyticDto> result = assignmentApiFacade.getAllDoneAssignments()
    then: "gets all analytic data for all done assignments"
      result == [createAssignmentAnalytic(userId: mike.userId, username: mike.username, name: mike.name, surname: mike.surname,
              objectName: onboarding.taskName, minutesTakenToDone: MINUTES_FOR_WEEK_LATER, startedAt: Date.from(NOW), endedAt: Date.from(WEEK_LATER),
              assignmentStatus: AssignmentStatusDto.DONE, category: newEmployee.categoryName)]
  }
}
