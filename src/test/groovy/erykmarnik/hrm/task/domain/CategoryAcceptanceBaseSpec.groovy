package erykmarnik.hrm.task.domain

import erykmarnik.hrm.integration.IntegrationSpec
import erykmarnik.hrm.task.dto.CategoryDto
import erykmarnik.hrm.task.dto.CreateCategoryDto
import erykmarnik.hrm.task.dto.CreateTaskDto
import erykmarnik.hrm.task.dto.ModifyTaskDto
import erykmarnik.hrm.task.dto.NewCategoryNameDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.task.sample.TaskSample
import erykmarnik.hrm.user.domain.UserApiFacade
import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.user.sample.UserSample
import erykmarnik.hrm.utils.ContextHolder
import erykmarnik.hrm.utils.TimeApiFacade
import erykmarnik.hrm.utils.sample.TimeSample

class CategoryAcceptanceBaseSpec extends IntegrationSpec implements TaskSample, TimeSample, UserSample, CategorySample {
  TaskApiFacade taskApiFacade
  CategoryApiFacade categoryApiFacade
  UserApiFacade userApiFacade
  TimeApiFacade timeApiFacade

  CategoryDto createCategoryRequest(Long userId, CreateCategoryDto categoryDto) {
    ContextHolder.setUserContext(new UserContext(userId))
    return categoryApiFacade.createCategory(categoryDto)
  }

  CategoryDto getCategory(Long userId, Long categoryId) {
    ContextHolder.setUserContext(new UserContext(userId))
    return categoryApiFacade.getCategory(categoryId)
  }

  CategoryDto modifyCategory(Long userId, Long categoryId, NewCategoryNameDto newCategoryName) {
    ContextHolder.setUserContext(new UserContext(userId))
    return categoryApiFacade.modifyCategory(categoryId, newCategoryName)
  }

  List<CategoryDto> getAllCategories(Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    return categoryApiFacade.getAllCategories()
  }

  List<TaskDto> getTasksForCategory(Long userId, Long categoryId) {
    ContextHolder.setUserContext(new UserContext(userId))
    return categoryApiFacade.getTasksForCategory(categoryId)
  }

  TaskDto createTaskRequest(Long userId, CreateTaskDto createTask) {
    ContextHolder.setUserContext(new UserContext(userId))
    return taskApiFacade.createTask(createTask)
  }

  TaskDto modifyTask(Long userId, UUID taskId, ModifyTaskDto modifyTaskDto) {
    ContextHolder.setUserContext(new UserContext(userId))
    return taskApiFacade.modifyTask(taskId, modifyTaskDto)
  }

  List<TaskDto> getAll(Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    return taskApiFacade.getAll()
  }

  TaskDto getTaskById(Long userId, UUID taskId) {
    ContextHolder.setUserContext(new UserContext(userId))
    return taskApiFacade.getTaskById(taskId)
  }

  TaskDto deleteTask(Long userId, UUID taskId) {
    if (taskId != null) {
      ContextHolder.setUserContext(new UserContext(userId))
      taskApiFacade.deleteTask(taskId)
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
