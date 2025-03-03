package erykmarnik.hrm.task.domain

import erykmarnik.hrm.integration.IntegrationSpec
import erykmarnik.hrm.integration.UserRequest
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.task.sample.TaskSample
import erykmarnik.hrm.user.domain.UserApiFacade
import erykmarnik.hrm.user.sample.UserSample
import erykmarnik.hrm.utils.TimeApiFacade
import erykmarnik.hrm.utils.sample.TimeSample

class CategoryAcceptanceBaseSpec extends IntegrationSpec implements TaskSample, TimeSample, UserSample, CategorySample {
  TaskApiFacade taskApiFacade
  CategoryApiFacade categoryApiFacade
  UserApiFacade userApiFacade
  TimeApiFacade timeApiFacade

  TaskDto deleteTask(UUID taskId, UserRequest userRequest) {
    if (taskId != null) {
      taskApiFacade.deleteTask(taskId, userRequest)
    }
    return TaskDto.builder().build()
  }

  def setup() {
    taskApiFacade = new TaskApiFacade(mockMvc, objectMapper)
    timeApiFacade = new TimeApiFacade(mockMvc, objectMapper)
    userApiFacade = new UserApiFacade(mockMvc, objectMapper)
    categoryApiFacade = new CategoryApiFacade(mockMvc, objectMapper)
  }
}
