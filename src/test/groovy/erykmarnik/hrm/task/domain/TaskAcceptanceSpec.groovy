package erykmarnik.hrm.task.domain

import erykmarnik.hrm.integration.UserRequest
import erykmarnik.hrm.task.dto.CreateCategoryDto
import erykmarnik.hrm.task.dto.ModifyTaskDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.user.dto.UserDto

class TaskAcceptanceSpec extends CategoryAcceptanceBaseSpec {
  private UserDto jane
  private TaskDto task
  private long onboardingCategory

  def setup() {
    timeApiFacade.useFixedClock(NOW)

    given: "there is admin $jane"
      jane = userApiFacade.createAdmin(createNewUser(username: "jane123", name: "Jane", surname: "Doe"))
    and: "there is category $onboardingCategory"
      onboardingCategory = categoryApiFacade.createCategory(new CreateCategoryDto(ONBOARDING), new UserRequest(jane)).categoryId
  }

  def cleanup() {
    timeApiFacade.useSystemClock()
    deleteTask(task.taskId, new UserRequest(jane))
    categoryApiFacade.deleteCategory(onboardingCategory, new UserRequest(jane))
    userApiFacade.deleteUser(jane.getUserId(), new UserRequest(jane))
  }

  def "Should create new task"() {
    when: "admin $jane creates new task"
      task = taskApiFacade.createTask(createNewTask(createdAt: NOW, categoryId: onboardingCategory), new UserRequest(jane))
    then: "task is created"
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, createdBy: jane.userId, categoryId: onboardingCategory)
  }

  def "Should get task by task id"() {
    given: "admin $jane creates new task"
      UUID taskId = taskApiFacade.createTask(createNewTask(createdAt: NOW, categoryId: onboardingCategory), new UserRequest(jane)).taskId
    when: "asks for $task by his id"
      task = taskApiFacade.getTaskById(taskId, new UserRequest(jane))
    then: "task is created"
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, createdBy: jane.userId, categoryId: onboardingCategory)
  }

  def "Should delete task"() {
    given: "admin $jane creates new task with assigned user $EMPLOYEE_JOHN"
      task = taskApiFacade.createTask(createNewTask(createdAt: NOW, categoryId: onboardingCategory), new UserRequest(jane))
    when: "admin $jane deletes task"
      task = deleteTask(task.taskId, new UserRequest(jane))
    then: "task is deleted"
      taskApiFacade.getAll(new UserRequest(jane)) == []
  }

  def "Should modify task"() {
    given: "admin $jane creates new task with assigned user $EMPLOYEE_JOHN"
      UUID taskId = taskApiFacade.createTask(createNewTask(createdAt: NOW, categoryId: onboardingCategory), new UserRequest(jane)).getTaskId()
    when: "modifies task by changing assigned user and task name"
      task = taskApiFacade.modifyTask(taskId, ModifyTaskDto.builder().taskName(ONBOARDING_TASK).build(), new UserRequest(jane))
    then: "task is modified"
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, createdBy: jane.userId, taskName: ONBOARDING_TASK, categoryId: onboardingCategory)
  }
}
