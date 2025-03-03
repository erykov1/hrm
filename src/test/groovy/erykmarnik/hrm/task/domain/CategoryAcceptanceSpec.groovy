package erykmarnik.hrm.task.domain

import erykmarnik.hrm.integration.UserRequest
import erykmarnik.hrm.task.dto.CategoryDto
import erykmarnik.hrm.task.dto.CreateCategoryDto
import erykmarnik.hrm.task.dto.NewCategoryNameDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.user.dto.UserDto

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
  }

  def cleanup() {
    timeApiFacade.useSystemClock()
    if (!isTaskDeleted && task != null) {
      taskApiFacade.deleteTask(task.getTaskId(), new UserRequest(jane))
    }
    categoryApiFacade.deleteCategory(onboardingCategoryId, new UserRequest(jane))
    categoryApiFacade.deleteCategory(newsCategoryId, new UserRequest(jane))
    userApiFacade.deleteUser(jane.getUserId(), new UserRequest(jane))
  }

  def "Should create new category"() {
    when: "admin $jane creates new category $onboardingCategoryId"
      onboardingCategoryId = categoryApiFacade.createCategory(new CreateCategoryDto(ONBOARDING), new UserRequest(jane)).categoryId
    then: "category $onboardingCategoryId is created"
      categoryApiFacade.getCategory(onboardingCategoryId, new UserRequest(jane)) == createCategory(categoryId: onboardingCategoryId, categoryName: ONBOARDING,
              createdBy: jane.userId, createdAt: NOW)
  }

  def "Should change category name"() {
    given: "there is category $onboardingCategoryId"
      onboardingCategoryId = categoryApiFacade.createCategory(new CreateCategoryDto(ONBOARDING), new UserRequest(jane)).categoryId
    when: "admin $jane change category $onboardingCategoryId name"
      CategoryDto onboardingCategory = categoryApiFacade.modifyCategory(onboardingCategoryId, new NewCategoryNameDto(NEWS), new UserRequest(jane))
    then: "category $onboardingCategoryId name is changed to $NEWS"
      onboardingCategory == createCategory(categoryId: onboardingCategoryId, categoryName: NEWS, createdBy: jane.userId, createdAt: NOW)
  }

  def "Should get category by category id"() {
    given: "there is category $onboardingCategoryId"
      onboardingCategoryId = categoryApiFacade.createCategory(new CreateCategoryDto(ONBOARDING), new UserRequest(jane)).categoryId
    when: "admin $jane asks for category"
      CategoryDto result = categoryApiFacade.getCategory(onboardingCategoryId, new UserRequest(jane))
    then: "gets category $onboardingCategoryId"
      result == createCategory(categoryId: onboardingCategoryId, categoryName: ONBOARDING, createdBy: jane.userId,
            createdAt: NOW)
  }

  def "Should delete category"() {
    given: "there is category $onboardingCategoryId"
      onboardingCategoryId = categoryApiFacade.createCategory(new CreateCategoryDto(ONBOARDING), new UserRequest(jane)).categoryId
    when: "admin $jane deletes category $onboardingCategoryId"
      onboardingCategoryId = deleteCategory(onboardingCategoryId, new UserRequest(jane))
    then: "category $onboardingCategoryId is deleted"
      categoryApiFacade.getAllCategories(new UserRequest(jane)) == []
  }

  def "Should get all categories"() {
    given: "there is category $onboardingCategoryId"
      onboardingCategoryId = categoryApiFacade.createCategory(new CreateCategoryDto(ONBOARDING), new UserRequest(jane)).categoryId
    and: "admin $jane creates $newsCategoryId $WEEK_LATER"
      timeApiFacade.useFixedClock(WEEK_LATER)
      newsCategoryId = categoryApiFacade.createCategory(new CreateCategoryDto(NEWS), new UserRequest(jane)).categoryId
    when: "admin $jane asks for all categories"
      List<CategoryDto> result = categoryApiFacade.getAllCategories(new UserRequest(jane))
    then: "admin $jane gets all categories"
      equalsCategories(result, [createCategory(categoryId: onboardingCategoryId, categoryName: ONBOARDING, createdBy: jane.userId,
              createdAt: NOW), createCategory(categoryId: newsCategoryId, categoryName: NEWS, createdBy: jane.userId,
              createdAt: WEEK_LATER)])
  }

  def "Should get all tasks for category"() {
    given: "there is category $onboardingCategoryId"
      onboardingCategoryId = categoryApiFacade.createCategory(new CreateCategoryDto(ONBOARDING), new UserRequest(jane)).categoryId
    and: "admin $jane creates task $task in category $onboardingCategoryId"
      task = taskApiFacade.createTask(createNewTask(categoryId: onboardingCategoryId), new UserRequest(jane))
    when: "admin $jane asks for all tasks in category $onboardingCategoryId"
      List<TaskDto> result = categoryApiFacade.getTasksForCategory(onboardingCategoryId, new UserRequest(jane))
    then: "admin $jane gets all tasks assigned to category $onboardingCategoryId"
      result == [createTask(taskId: task.taskId, createdAt: NOW, createdBy: jane.userId, categoryId: onboardingCategoryId)]
    when: "admin $jane deletes task $task"
      isTaskDeleted = deleteTaskBy(task.taskId, new UserRequest(jane))
    then: "there are no tasks assigned to category $onboardingCategoryId"
      categoryApiFacade.getTasksForCategory(onboardingCategoryId, new UserRequest(jane)) == []
  }

  private boolean deleteTaskBy(UUID taskId, UserRequest userRequest) {
    taskApiFacade.deleteTask(taskId, userRequest)
    return true
  }

  private long deleteCategory(long categoryId, UserRequest userRequest) {
    categoryApiFacade.deleteCategory(categoryId, userRequest)
    return 0
  }
}
