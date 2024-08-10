package erykmarnik.hrm.task.domain

import erykmarnik.hrm.integration.IntegrationSpec
import erykmarnik.hrm.task.dto.ModifyTaskDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.task.sample.TaskSample
import erykmarnik.hrm.user.domain.UserApiFacade
import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.user.dto.UserDto
import erykmarnik.hrm.user.sample.UserSample
import erykmarnik.hrm.utils.ContextHolder
import erykmarnik.hrm.utils.TimeApiFacade
import erykmarnik.hrm.utils.sample.TimeSample

class TaskAcceptanceSpec extends IntegrationSpec implements TaskSample, TimeSample, UserSample {
  private TaskApiFacade taskApiFacade
  private UserApiFacade userApiFacade
  private TimeApiFacade timeApiFacade
  private UserDto jane
  private TaskDto task

  def setup() {
    taskApiFacade = new TaskApiFacade(mockMvc, objectMapper)
    timeApiFacade = new TimeApiFacade(mockMvc, objectMapper)
    userApiFacade = new UserApiFacade(mockMvc, objectMapper)
    timeApiFacade.useFixedClock(NOW)
    given: "there is admin $jane"
      jane = userApiFacade.createAdmin(createNewUser(username: "jane123", name: "Jane", surname: "Doe"))
      ContextHolder.setUserContext(new UserContext(jane.userId))
  }

  def cleanup() {
    timeApiFacade.useSystemClock()
    if (task.taskId != null) {
      taskApiFacade.deleteTask(task.getTaskId())
    }
    userApiFacade.deleteUser(jane.getUserId())
    ContextHolder.clear()
  }

  def "Should create new task"() {
    when: "admin $jane creates new task"
      task = taskApiFacade.createTask(createNewTask(createdAt: NOW))
    then: "task is created"
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, createdBy: jane.userId)
  }

  def "Should get task by task id"() {
    given: "admin $jane creates new task"
      Long taskId = taskApiFacade.createTask(createNewTask(createdAt: NOW)).taskId
    when: "asks for $task by his id"
      task = taskApiFacade.getTaskById(taskId)
    then: "task is created"
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, createdBy: jane.userId)
  }

  def "Should delete task"() {
    given: "admin $jane creates new task with assigned user $EMPLOYEE_JOHN"
      task = taskApiFacade.createTask(createNewTask(createdAt: NOW))
    when: "admin $jane deletes task"
      taskApiFacade.deleteTask(task.getTaskId())
    then: "task is deleted"
      taskApiFacade.getAll() == []
  }

  def "Should modify task"() {
    given: "admin $jane creates new task with assigned user $EMPLOYEE_JOHN"
      Long taskId = taskApiFacade.createTask(createNewTask(createdAt: NOW)).getTaskId()
    when: "modifies task by changing assigned user and task name"
      ContextHolder.setUserContext(new UserContext(jane.userId))
      task = taskApiFacade.modifyTask(taskId, ModifyTaskDto.builder().taskName(ONBOARDING_TASK).build())
    then: "task is modified"
      task == createTask(taskId: task.getTaskId(), createdAt: NOW, createdBy: jane.userId, taskName: ONBOARDING_TASK)
  }

}
