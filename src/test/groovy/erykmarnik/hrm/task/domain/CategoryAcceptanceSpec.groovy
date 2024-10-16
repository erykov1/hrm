package erykmarnik.hrm.task.domain

import erykmarnik.hrm.task.dto.CategoryDto
import erykmarnik.hrm.task.dto.CreateCategoryDto
import erykmarnik.hrm.task.dto.NewCategoryNameDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.user.dto.UserDto
import erykmarnik.hrm.utils.ContextHolder

class CategoryAcceptanceSpec extends CategoryAcceptanceBaseSpec {
  private UserDto jane
  private TaskDto task
  private long onboardingCategoryId
  private long newsCategoryId
  private boolean isTaskDeleted

  def setup() {
    timeApiFacade.useFixedClock(NOW)

    given: "there is admin $jane"
      jane = userApiFacade.createAdmin(createNewUser(username: "jane123", name: "Jane", surname: "Doe"))
      ContextHolder.setUserContext(new UserContext(jane.userId))
  }

  def cleanup() {
    timeApiFacade.useSystemClock()
    if (!isTaskDeleted && task != null) {
      taskApiFacade.deleteTask(task.getTaskId())
    }
    categoryApiFacade.deleteCategory(onboardingCategoryId)
    categoryApiFacade.deleteCategory(newsCategoryId)
    userApiFacade.deleteUser(jane.getUserId())
    ContextHolder.clear()
  }

  def "Should create new category"() {
    when: "admin $jane creates new category $onboardingCategoryId"
      onboardingCategoryId = createCategoryRequest(jane.userId, new CreateCategoryDto(ONBOARDING)).categoryId
    then: "category $onboardingCategoryId is created"
      getCategory(jane.userId, onboardingCategoryId) == createCategory(categoryId: onboardingCategoryId, categoryName: ONBOARDING,
              createdBy: jane.userId, createdAt: NOW)
  }

  def "Should change category name"() {
    given: "there is category $onboardingCategoryId"
      onboardingCategoryId = createCategoryRequest(jane.userId, new CreateCategoryDto(ONBOARDING)).categoryId
    when: "admin $jane change category $onboardingCategoryId name"
      CategoryDto onboardingCategory = modifyCategory(jane.userId, onboardingCategoryId, new NewCategoryNameDto(NEWS))
    then: "category $onboardingCategoryId name is changed to $NEWS"
      onboardingCategory == createCategory(categoryId: onboardingCategoryId, categoryName: NEWS, createdBy: jane.userId,
              createdAt: NOW)
  }

  def "Should get category by category id"() {
    given: "there is category $onboardingCategoryId"
      onboardingCategoryId = createCategoryRequest(jane.userId, new CreateCategoryDto(ONBOARDING)).categoryId
    when: "admin $jane asks for category"
      CategoryDto result = getCategory(jane.userId, onboardingCategoryId)
    then: "gets category $onboardingCategoryId"
      result == createCategory(categoryId: onboardingCategoryId, categoryName: ONBOARDING, createdBy: jane.userId,
            createdAt: NOW)
  }

  def "Should delete category"() {
    given: "there is category $onboardingCategoryId"
      onboardingCategoryId = createCategoryRequest(jane.userId, new CreateCategoryDto(ONBOARDING)).categoryId
    when: "admin $jane deletes category $onboardingCategoryId"
      onboardingCategoryId = deleteCategory(onboardingCategoryId)
    then: "category $onboardingCategoryId is deleted"
      getAllCategories(jane.userId) == []
  }

  def "Should get all categories"() {
    given: "there is category $onboardingCategoryId"
      onboardingCategoryId = createCategoryRequest(jane.userId, new CreateCategoryDto(ONBOARDING)).categoryId
    and: "admin jane creates $newsCategoryId $WEEK_LATER"
      timeApiFacade.useFixedClock(WEEK_LATER)
      newsCategoryId = createCategoryRequest(jane.userId, new CreateCategoryDto(NEWS)).categoryId
    when: "admin $jane asks for all categories"
      List<CategoryDto> result = getAllCategories(jane.userId)
    then: "admin $jane gets all categories"
      equalsCategories(result, [createCategory(categoryId: onboardingCategoryId, categoryName: ONBOARDING, createdBy: jane.userId,
              createdAt: NOW), createCategory(categoryId: newsCategoryId, categoryName: NEWS, createdBy: jane.userId,
              createdAt: WEEK_LATER)])
  }

  def "Should get all tasks for category"() {
    given: "there is category $onboardingCategoryId"
      onboardingCategoryId = createCategoryRequest(jane.userId, new CreateCategoryDto(ONBOARDING)).categoryId
    and: "admin $jane creates task $task in category $onboardingCategoryId"
      ContextHolder.setUserContext(new UserContext(jane.userId))
      task = taskApiFacade.createTask(createNewTask(categoryId: onboardingCategoryId))
    when: "admin $jane asks for all tasks in category $onboardingCategoryId"
      List<TaskDto> result = getTasksForCategory(jane.userId, onboardingCategoryId)
    then: "admin $jane gets all tasks assigned to category $onboardingCategoryId"
      result == [createTask(taskId: task.taskId, createdAt: NOW, createdBy: jane.userId, categoryId: onboardingCategoryId)]
    when: "admin $jane deletes task $task"
      isTaskDeleted = deleteTask(task.taskId)
    then: "there are no tasks assigned to category $onboardingCategoryId"
      getTasksForCategory(jane.userId, onboardingCategoryId) == []
  }

  private boolean deleteTask(UUID taskId) {
    taskApiFacade.deleteTask(taskId)
    return true
  }

  private long deleteCategory(long categoryId) {
    ContextHolder.setUserContext(new UserContext(jane.userId))
    categoryApiFacade.deleteCategory(categoryId)
    return 0
  }
}
