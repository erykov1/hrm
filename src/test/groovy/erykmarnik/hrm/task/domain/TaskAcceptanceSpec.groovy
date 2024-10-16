package erykmarnik.hrm.task.domain

import erykmarnik.hrm.task.dto.CreateCategoryDto
import erykmarnik.hrm.task.dto.ModifyTaskDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.user.dto.UserDto
import erykmarnik.hrm.utils.ContextHolder

class TaskAcceptanceSpec extends CategoryAcceptanceBaseSpec {
  private UserDto jane
  private TaskDto task
  private long onboardingCategory

  def setup() {
    timeApiFacade.useFixedClock(NOW)

    given: "there is admin $jane"
      jane = userApiFacade.createAdmin(createNewUser(username: "jane123", name: "Jane", surname: "Doe"))
      ContextHolder.setUserContext(new UserContext(jane.userId))
    and: "there is category $onboardingCategory"
      onboardingCategory = createCategoryRequest(jane.userId, new CreateCategoryDto(ONBOARDING)).categoryId
  }

  def cleanup() {
    timeApiFacade.useSystemClock()
    userApiFacade.deleteUser(jane.getUserId())
    deleteTask(jane.userId, task.taskId)
    categoryApiFacade.deleteCategory(onboardingCategory)
    ContextHolder.clear()
  }

  def "Should create new task"() {
    when: "admin $jane creates new task"
      task = createTaskRequest(jane.userId, createNewTask(createdAt: NOW, categoryId: onboardingCategory))
    then: "task is created"
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, createdBy: jane.userId, categoryId: onboardingCategory)
  }

  def "Should get task by task id"() {
    given: "admin $jane creates new task"
      UUID taskId = createTaskRequest(jane.userId, createNewTask(createdAt: NOW, categoryId: onboardingCategory)).taskId
    when: "asks for $task by his id"
      task = getTaskById(jane.userId, taskId)
    then: "task is created"
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, createdBy: jane.userId, categoryId: onboardingCategory)
  }

  def "Should delete task"() {
    given: "admin $jane creates new task with assigned user $EMPLOYEE_JOHN"
      task = createTaskRequest(jane.userId, createNewTask(createdAt: NOW, categoryId: onboardingCategory))
    when: "admin $jane deletes task"
      task = deleteTask(jane.userId, task.taskId)
    then: "task is deleted"
      getAll(jane.userId) == []
  }

  def "Should modify task"() {
    given: "admin $jane creates new task with assigned user $EMPLOYEE_JOHN"
      UUID taskId = createTaskRequest(jane.userId, createNewTask(createdAt: NOW, categoryId: onboardingCategory)).getTaskId()
    when: "modifies task by changing assigned user and task name"
      task = modifyTask(jane.userId, taskId, ModifyTaskDto.builder().taskName(ONBOARDING_TASK).build())
    then: "task is modified"
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, createdBy: jane.userId, taskName: ONBOARDING_TASK, categoryId: onboardingCategory)
  }
}
