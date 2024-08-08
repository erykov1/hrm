package erykmarnik.hrm.task.domain

import erykmarnik.hrm.task.dto.ModifyTaskDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.task.dto.TaskStatusDto
import erykmarnik.hrm.task.exception.ForbiddenTaskOperationException
import erykmarnik.hrm.task.exception.TaskNotFoundException
import erykmarnik.hrm.task.sample.TaskSample
import erykmarnik.hrm.utils.sample.TimeSample


class TaskSpec extends TaskBaseSpec implements TaskSample, TimeSample {
  def setup() {
    instantProvider.useFixedClock(NOW)

    given: "admin $ADMIN_JANE is logged in"
      loginUser(ADMIN_JANE)
  }

  def "Should create new task"() {
    when: "creates new task for user $EMPLOYEE_JOHN"
      TaskDto task = taskFacade.createTask(createNewTask(createdAt: NOW, assignedTo: EMPLOYEE_JOHN))
    then: "task is created"
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, assignedTo: EMPLOYEE_JOHN, createdBy: ADMIN_JANE,
              doneAt: null, taskStatus: TaskStatusDto.NOT_STARTED)
  }

  def "Should delete task"() {
    given: "task for user $EMPLOYEE_JOHN is created"
      TaskDto task = taskFacade.createTask(createNewTask(createdAt: NOW, assignedTo: EMPLOYEE_JOHN))
    and: "admin deletes task"
      taskFacade.deleteTask(task.getTaskId())
    when: "asks for task $task"
      taskFacade.findByTaskId(task.getTaskId())
    then: "task is deleted"
      thrown(TaskNotFoundException)
  }

  def "Should modify task"() {
    given: "task for user $EMPLOYEE_JOHN is created"
      Long taskId = taskFacade.createTask(createNewTask(createdAt: NOW, assignedTo: EMPLOYEE_JOHN)).taskId
    when: "modifies task by changing assigned user and task name"
      TaskDto task = taskFacade.modifyTask(taskId, ModifyTaskDto.builder().assignedTo(EMPLOYEE_MIKE).taskName(ONBOARDING_TASK).build())
    then: "task is modified"
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, assignedTo: EMPLOYEE_MIKE, createdBy: ADMIN_JANE, taskName: ONBOARDING_TASK,
              doneAt: null, taskStatus: TaskStatusDto.NOT_STARTED)
  }

  def "Should not modify task that does not exist"() {
    when: "modifies task that does not exist"
      taskFacade.modifyTask(TASK_ID, ModifyTaskDto.builder().assignedTo(EMPLOYEE_MIKE).taskName(ONBOARDING_TASK).build())
    then: "task is not modified"
      thrown(TaskNotFoundException)
  }

  def "Should not modify task if user has no privileges"() {
    given: "task for user $EMPLOYEE_JOHN is created"
      Long taskId = taskFacade.createTask(createNewTask(createdAt: NOW, assignedTo: EMPLOYEE_JOHN)).taskId
    and: "user $EMPLOYEE_MIKE logs in"
      loginUser(EMPLOYEE_MIKE)
    when: "user $EMPLOYEE_MIKE tries to modify task"
      taskFacade.modifyTask(taskId, ModifyTaskDto.builder().assignedTo(EMPLOYEE_MIKE).taskName(ONBOARDING_TASK).build())
    then: "task is not modified by user $EMPLOYEE_MIKE"
      thrown(ForbiddenTaskOperationException)
  }

  def "Should change task status when user set task to done"() {
    given: "task is created with user assigned $EMPLOYEE_JOHN"
      Long taskId = taskFacade.createTask(createNewTask(createdAt: NOW, assignedTo: EMPLOYEE_JOHN)).taskId
    and: "user $EMPLOYEE_JOHN logs in $WEEK_LATER"
      instantProvider.useFixedClock(WEEK_LATER)
      loginUser(EMPLOYEE_JOHN)
    when: "user $EMPLOYEE_JOHN set task to done"
      taskFacade.setTaskToDone(taskId)
    then: "task is set as done"
      TaskDto task = taskFacade.findByTaskId(taskId)
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, assignedTo: EMPLOYEE_JOHN, createdBy: ADMIN_JANE,
              doneAt: WEEK_LATER, taskStatus: TaskStatusDto.DONE)
  }

  def "Should not change task status by user that is not assigned to task"() {
    given: "task is created with user assigned $EMPLOYEE_JOHN"
      Long taskId = taskFacade.createTask(createNewTask(createdAt: NOW, assignedTo: EMPLOYEE_JOHN)).taskId
    and: "user $EMPLOYEE_MIKE logs in"
      loginUser(EMPLOYEE_MIKE)
    when: "user $EMPLOYEE_MIKE tries to set task to done"
      taskFacade.setTaskToDone(taskId)
    then: "user $EMPLOYEE_MIKE cannot set task to done"
      thrown(ForbiddenTaskOperationException)
  }

  def "Admin should be able to change task status to done"() {
    given: "task is created with user assigned $EMPLOYEE_JOHN"
      Long taskId = taskFacade.createTask(createNewTask(createdAt: NOW, assignedTo: EMPLOYEE_JOHN)).taskId
    when: "admin $ADMIN_JANE set task to done with assigned $EMPLOYEE_JOHN"
      taskFacade.setTaskToDone(taskId)
    then: "task is set as done"
      TaskDto task = taskFacade.findByTaskId(taskId)
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, assignedTo: EMPLOYEE_JOHN, createdBy: ADMIN_JANE,
              doneAt: NOW, taskStatus: TaskStatusDto.DONE)
  }

  def "Should not change task status if task does not exist"() {
    when: "updates task that does not exist"
      taskFacade.setTaskToDone(TASK_ID)
    then: "task is not set as done"
      thrown(TaskNotFoundException)
  }
}
