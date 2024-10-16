package erykmarnik.hrm.task.domain;

import erykmarnik.hrm.security.SecurityFacade;
import erykmarnik.hrm.task.dto.*;
import erykmarnik.hrm.task.exception.*;
import erykmarnik.hrm.utils.ContextHolder;
import erykmarnik.hrm.utils.InstantProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskFacade {
  TaskCreator taskCreator;
  SecurityFacade securityFacade;
  InstantProvider instantProvider;
  CategoryRepository categoryRepository;

  public CategoryDto createCategory(CreateCategoryDto createCategory) {
    log.info("creating category: " + createCategory.getCategoryName());
    validateCategory(createCategory.getCategoryName());
    return categoryRepository.save(taskCreator.createCategory(createCategory)).categoryDto();
  }

  public TaskDto createTask(CreateTaskDto createTask) {
    log.info("creating task");
    Category category = findCategoryById(createTask.getCategoryId());
    Task task = taskCreator.createTask(createTask, category);
    categoryRepository.save(category.addTask(task));
    return task.dto();
  }

  public TaskDto modifyTask(UUID taskId, ModifyTaskDto modifyTask) {
    log.info("modifying task: " + taskId);
    validateUserPrivileges(taskId, ContextHolder.getUserContext().getUserId());
    Task task = findTaskByTaskId(taskId);
    Category category = findCategoryById(task.dto().getCategoryId());
    return category.modifyTask(modifyTask, taskId).dto();
  }

  public TaskDto findByTaskId(UUID taskId) {
    log.info("finding task: " + taskId);
    return categoryRepository.findTaskById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId)).dto();
  }

  public void deleteTask(UUID taskId) {
    log.info("deleting task: " + taskId);
    Task task = findTaskByTaskId(taskId);
    Category category = findCategoryById(task.dto().getCategoryId());
    categoryRepository.save(category.removeTask(task));
  }

  public List<TaskDto> getAll() {
    log.info("getting all tasks");
    return categoryRepository.findAllTasks().stream().map(Task::dto).collect(Collectors.toList());
  }

  public CategoryDto modifyCategory(Long categoryId, NewCategoryNameDto newName) {
    Category category = findCategoryById(categoryId);
    validateCategory(newName.getNewCategoryName());
    log.info("changing category name from: " + category.categoryDto().getCategoryName() + " to: " + newName);
    return categoryRepository.save(category.changeName(newName.getNewCategoryName())).categoryDto();
  }

  public CategoryDto getCategory(Long categoryId) {
    log.info("getting category with id " + categoryId);
    return findCategoryById(categoryId).categoryDto();
  }

  public void deleteCategory(Long categoryId) {
    validateCategoryDeletion(categoryId);
    log.info("deleting category with id: " + categoryId);
    categoryRepository.deleteById(categoryId);
  }

  public List<CategoryDto> getAllCategories() {
    log.info("getting all categories");
    return categoryRepository.findAll().stream()
            .map(Category::categoryDto)
            .collect(Collectors.toList());
  }

  public List<TaskDto> getAllTasksForCategory(Long categoryId) {
    log.info("getting all tasks for category: " + categoryId);
    return findCategoryById(categoryId).getTasks().stream()
            .map(Task::dto)
            .collect(Collectors.toList());
  }

  private void validateUserPrivileges(UUID taskId, Long userId) {
    if (!isAbleToModifyTask(taskId, userId)) {
      throw new ForbiddenTaskOperationException(taskId);
    }
  }

  private boolean isAbleToModifyTask(UUID taskId, Long userId) {
    TaskDto task = findByTaskId(taskId);
    return task.getCreatedBy().equals(userId) || securityFacade.isAdmin(userId);
  }

  private Category findCategoryById(Long categoryId) {
    return categoryRepository.findByCategoryId(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
  }

  private Task findTaskByTaskId(UUID taskId) {
    return categoryRepository.findTaskById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
  }

  private void validateCategory(String categoryName) {
    boolean isCategoryTaken = categoryRepository.findAll().stream()
            .map(category -> category.categoryDto().getCategoryName())
            .anyMatch(existingName -> existingName.equals(categoryName));
    if (isCategoryTaken) {
      throw new AlreadyTakenException("Category name is already taken");
    }
  }

  private void validateCategoryDeletion(Long categoryId) {
    if (!findCategoryById(categoryId).getTasks().isEmpty()) {
      throw new ForbiddenCategoryOperationException("Cannot delete category due to assigned tasks");
    }
  }
}
