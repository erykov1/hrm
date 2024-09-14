package erykmarnik.hrm.task.domain

import erykmarnik.hrm.task.dto.CreateCategoryDto
import erykmarnik.hrm.task.dto.ModifyTaskDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.task.exception.ForbiddenTaskOperationException
import erykmarnik.hrm.task.exception.TaskNotFoundException


class TaskSpec extends TaskBaseSpec {
  Long newEmployeeCategory

  def setup() {
    instantProvider.useFixedClock(NOW)

    given: "admin $ADMIN_JANE is logged in"
      loginUser(ADMIN_JANE)
    and: "there is category $newEmployeeCategory"
      newEmployeeCategory = taskFacade.createCategory(new CreateCategoryDto(CATEGORY_NAME)).categoryId
  }

  def "Should create new task"() {
    when: "creates new task for user $EMPLOYEE_JOHN"
      TaskDto task = taskFacade.createTask(createNewTask(createdAt: NOW, categoryId: newEmployeeCategory))
    then: "task is created"
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, createdBy: ADMIN_JANE, categoryId: newEmployeeCategory)
  }

  def "Should delete task"() {
    given: "task for user $EMPLOYEE_JOHN is created"
      TaskDto task = taskFacade.createTask(createNewTask(createdAt: NOW, categoryId: newEmployeeCategory))
    and: "admin deletes task"
      taskFacade.deleteTask(task.getTaskId())
    when: "asks for task $task"
      taskFacade.findByTaskId(task.getTaskId())
    then: "task is deleted"
      thrown(TaskNotFoundException)
  }

  def "Should modify task"() {
    given: "task for user $EMPLOYEE_JOHN is created"
      UUID taskId = taskFacade.createTask(createNewTask(createdAt: NOW, categoryId: newEmployeeCategory)).taskId
    when: "modifies task by changing task name"
      TaskDto task = taskFacade.modifyTask(taskId, ModifyTaskDto.builder().taskName(ONBOARDING_TASK).build())
    then: "task is modified"
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, createdBy: ADMIN_JANE, taskName: ONBOARDING_TASK, categoryId: newEmployeeCategory)
  }

  def "Should not modify task that does not exist"() {
    when: "modifies task that does not exist"
      taskFacade.modifyTask(TASK_ID, ModifyTaskDto.builder().taskName(ONBOARDING_TASK).build())
    then: "task is not modified"
      thrown(TaskNotFoundException)
  }

  def "Should not modify task if user has no privileges"() {
    given: "task for user $EMPLOYEE_JOHN is created"
      UUID taskId = taskFacade.createTask(createNewTask(createdAt: NOW, categoryId: newEmployeeCategory)).taskId
    and: "user $EMPLOYEE_MIKE logs in"
      loginUser(EMPLOYEE_MIKE)
    when: "user $EMPLOYEE_MIKE tries to modify task"
      taskFacade.modifyTask(taskId, ModifyTaskDto.builder().taskName(ONBOARDING_TASK).build())
    then: "task is not modified by user $EMPLOYEE_MIKE"
      thrown(ForbiddenTaskOperationException)
  }
}
