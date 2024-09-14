package erykmarnik.hrm.task.domain

import erykmarnik.hrm.task.dto.CategoryDto
import erykmarnik.hrm.task.dto.CreateCategoryDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.task.exception.AlreadyTakenException
import erykmarnik.hrm.task.exception.ForbiddenCategoryOperationException

class CategorySpec extends TaskBaseSpec {
  def setup() {
    instantProvider.useFixedClock(NOW)

    given: "admin $ADMIN_JANE is logged in"
      loginUser(ADMIN_JANE)
  }

  def "Should create new category"() {
    when: "admin $ADMIN_JANE creates new category"
      CategoryDto category = taskFacade.createCategory(new CreateCategoryDto(CATEGORY_NAME))
    then: "category is created"
      category == createCategory(categoryId: category.categoryId, categoryName: CATEGORY_NAME, createdBy: ADMIN_JANE, createdAt: NOW)
  }

  def "Should not create new category if name is taken"() {
    given: "there is category"
      taskFacade.createCategory(new CreateCategoryDto(CATEGORY_NAME))
    when: "admin $ADMIN_JANE creates new category with the same name"
      taskFacade.createCategory(new CreateCategoryDto(CATEGORY_NAME))
    then: "new category is not created"
      thrown(AlreadyTakenException)
  }

  def "Should change category name"() {
    given: "there is category"
      CategoryDto category = taskFacade.createCategory(new CreateCategoryDto(CATEGORY_NAME))
    when: "admin $ADMIN_JANE modifies category name"
      taskFacade.modifyCategory(category.categoryId, "News")
    then: "category name is changed"
      taskFacade.getCategory(category.categoryId) == createCategory(categoryId: category.categoryId, categoryName: "News",
              createdBy: ADMIN_JANE, createdAt: NOW)
  }

  def "Should not change category name if name is taken"() {
    given: "there is category"
      taskFacade.createCategory(new CreateCategoryDto(CATEGORY_NAME))
    and: "there is another category"
      CategoryDto news = taskFacade.createCategory(new CreateCategoryDto(NEWS))
    when: "admin $ADMIN_JANE modifies category 'news' name"
      taskFacade.modifyCategory(news.categoryId, CATEGORY_NAME)
    then: "category name is not changed"
      thrown(AlreadyTakenException)
  }

  def "Should delete category"() {
    given: "there is category"
      CategoryDto category = taskFacade.createCategory(new CreateCategoryDto(CATEGORY_NAME))
    when: "admin $ADMIN_JANE deletes category"
      taskFacade.deleteCategory(category.categoryId)
    then: "category is deleted"
      taskFacade.getAllCategories() == []
  }

  def "Should not delete category if there are tasks assigned to category"() {
    given: "there is category"
      CategoryDto category = taskFacade.createCategory(new CreateCategoryDto(CATEGORY_NAME))
    and: "there is task assigned to category"
      taskFacade.createTask(createNewTask(categoryId: category.categoryId))
    when: "admin $ADMIN_JANE deletes category"
      taskFacade.deleteCategory(category.categoryId)
    then: "category is not deleted"
      thrown(ForbiddenCategoryOperationException)
  }

  def "Should get all tasks assigned to category"() {
    given: "there is category"
      CategoryDto category = taskFacade.createCategory(new CreateCategoryDto(CATEGORY_NAME))
    and: "there is task assigned to category"
      TaskDto task = taskFacade.createTask(createNewTask(categoryId: category.categoryId))
    when: "asks for all tasks assigned to category"
      List<TaskDto> results = taskFacade.getAllTasksForCategory(category.categoryId)
    then: "gets all tasks"
      results == [createTask(taskId: task.getTaskId(), createdAt: NOW, createdBy: ADMIN_JANE, categoryId: category.categoryId)]
  }
}
