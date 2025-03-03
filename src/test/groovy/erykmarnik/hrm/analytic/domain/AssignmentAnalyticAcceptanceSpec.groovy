package erykmarnik.hrm.analytic.domain

import erykmarnik.hrm.analytic.dto.AssignmentInfoDto
import erykmarnik.hrm.analytic.dto.RequestParams
import erykmarnik.hrm.analytic.dto.UserAssignmentDto
import erykmarnik.hrm.assignments.domain.AssignmentAcceptanceBaseSpec
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto
import erykmarnik.hrm.integration.UserRequest
import erykmarnik.hrm.task.dto.CategoryDto
import erykmarnik.hrm.task.dto.CreateCategoryDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.user.dto.UserDto
import org.springframework.data.domain.Sort

class AssignmentAnalyticAcceptanceSpec extends AssignmentAcceptanceBaseSpec {
  private UserDto jane
  private UserDto mike
  private UserDto john
  private TaskDto onboarding
  private CategoryDto newEmployee
  private long assignmentId
  private long johnAssignmentId
  AnalyticApiFacade analyticApiFacade

  def setup() {
    timeApiFacade.useFixedClock(NOW)
    analyticApiFacade = new AnalyticApiFacade(mockMvc, objectMapper)

    given: "there is admin $jane"
      jane = userApiFacade.createAdmin(createNewUser(username: "jane123", name: "Jane", surname: "Doe", email: "jane@mail.com"))
    and: "there is employee $mike"
      mike = userApiFacade.createEmployee(createNewUser(username: "mike123", name: "Mike", surname: "Smith", email: "mike@mail.com"))
    and: "there is employee $john"
      john = userApiFacade.createEmployee(createNewUser(username: "john123", name: "John", surname: "Butcher", email: "john@mail.com"))
    and: "there is category $newEmployee"
      newEmployee = categoryApiFacade.createCategory(new CreateCategoryDto(CATEGORY_NAME), new UserRequest(jane))
    and: "there is task $onboarding assigned to category $newEmployee"
      onboarding = taskApiFacade.createTask(createNewTask(createdAt: NOW, categoryId: newEmployee.categoryId), new UserRequest(jane))
    and: "admin $jane assinges user $mike to task $onboarding"
      assignmentId = assignmentApiFacade.createAssignment(createNewAssignment(userId: mike.userId, objectId: onboarding.taskId), new UserRequest(jane)).assignmentId
  }

  def cleanup() {
    deleteAssignment(assignmentId, new UserRequest(jane))
    deleteAssignment(johnAssignmentId, new UserRequest(jane))
    deleteTask(onboarding.taskId, new UserRequest(jane))
    categoryApiFacade.deleteCategory(newEmployee.categoryId, new UserRequest(jane))
    userApiFacade.deleteUser(jane.userId, new UserRequest(jane))
    userApiFacade.deleteUser(mike.userId, new UserRequest(mike))
    userApiFacade.deleteUser(john.userId, new UserRequest(john))
    timeApiFacade.useSystemClock()
  }

  def "Should get analytic data with status assignment"() {
    given: "admin $jane assigned $john to task $onboarding $WEEK_EARLIER"
      timeApiFacade.useFixedClock(WEEK_EARLIER)
      johnAssignmentId = assignmentApiFacade.createAssignment(createNewAssignment(userId: john.userId, objectId: onboarding.taskId), new UserRequest(jane)).assignmentId
    when: "admin $jane asks for assignemnts with status 'NOT_STARTED'"
      List<AssignmentInfoDto> result = analyticApiFacade.getAssignmentsWithStatus(createRequestParam(Sort.Direction.ASC, "username"),
              AssignmentStatusDto.NOT_STARTED, new UserRequest(jane))
    then: "admin $jane gets assignments"
      result == [createAssignmentAnalytic(username: john.username, name: john.name, surname: john.surname,
              objectName: onboarding.taskName, assignedAt: Date.from(WEEK_EARLIER), endedAt: null,
              assignmentStatus: AssignmentStatusDto.NOT_STARTED, category: newEmployee.categoryName),
                 createAssignmentAnalytic(username: mike.username, name: mike.name, surname: mike.surname,
            objectName: onboarding.taskName, assignedAt: Date.from(NOW), endedAt: null, assignmentStatus: AssignmentStatusDto.NOT_STARTED)]
    when: "user $john set assignment as done"
      timeApiFacade.useFixedClock(NOW)
      assignmentApiFacade.setToDone(johnAssignmentId, new UserRequest(john))
    and: "admin $jane asks for assignemnts with status 'DONE'"
      result = analyticApiFacade.getAssignmentsWithStatus(createRequestParam(Sort.Direction.ASC, "username"),
              AssignmentStatusDto.DONE, new UserRequest(jane))
    then: "admin $jane gets assignments with status 'DONE'"
      result == [createAssignmentAnalytic(username: john.username, name: john.name, surname: john.surname,
              objectName: onboarding.taskName, assignedAt: Date.from(WEEK_EARLIER), endedAt: Date.from(NOW),
              assignmentStatus: AssignmentStatusDto.DONE, category: newEmployee.categoryName)]
  }

  def "Should get all overdue assignments"() {
    given: "admin $jane assigned $john to task $onboarding $WEEK_EARLIER with deadline to $THREE_DAYS_LATER"
      timeApiFacade.useFixedClock(WEEK_EARLIER)
      johnAssignmentId = assignmentApiFacade.createAssignment(createNewAssignment(userId: john.userId, objectId: onboarding.taskId, dueTo: THREE_DAYS_LATER), new UserRequest(jane)).assignmentId
    and: "user $john's assignment is overdue"
      timeApiFacade.useFixedClock(WEEK_LATER)
      assignmentApiFacade.outDateNotStarted()
    when: "admin $jane asks for assignments with status 'OVERDUE'"
      List<AssignmentInfoDto> result = analyticApiFacade.getAssignmentsWithStatus(createRequestParam(Sort.Direction.ASC, "username"),
            AssignmentStatusDto.OVERDUE, new UserRequest(jane))
    then: "admin $jane gets $john's assignment"
      result == [createAssignmentAnalytic(username: john.username, name: john.name, surname: john.surname,
              objectName: onboarding.taskName, assignedAt: Date.from(WEEK_EARLIER), endedAt: null,
              assignmentStatus: AssignmentStatusDto.OVERDUE, category: newEmployee.categoryName)]
  }

  def "User should get his assignments"() {
    when: "user $mike asks for his assignments"
      List<UserAssignmentDto> result = analyticApiFacade.getUserAssignments(createRequestParam(Sort.Direction.ASC, "assignedAt"), new UserRequest(mike))
    then: "user $mike gets his assignments"
      result == [createUserAssignment(objectName: onboarding.taskName, assignedAt: Date.from(NOW), endedAt: null,
              assignmentStatus: AssignmentStatusDto.NOT_STARTED, category: newEmployee.categoryName)]
  }

  private RequestParams createRequestParam(Sort.Direction direction, String parameter, Integer limit = 50, Integer pageNumber = 0) {
    RequestParams.builder()
            .order(new Sort.Order(direction, parameter))
            .limit(limit)
            .pageNumber(pageNumber)
            .build()
  }
}
