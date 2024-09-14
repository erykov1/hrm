package erykmarnik.hrm.task.domain;

import erykmarnik.hrm.task.dto.CreateCategoryDto;
import erykmarnik.hrm.task.dto.CreateTaskDto;
import erykmarnik.hrm.utils.ContextHolder;
import erykmarnik.hrm.utils.InstantProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.ArrayList;
import java.util.UUID;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class TaskCreator {
  InstantProvider instantProvider;

  Task createTask(CreateTaskDto createTask, Category category) {
    return Task.builder()
            .taskId(UUID.randomUUID())
            .createdAt(instantProvider.now())
            .createdBy(ContextHolder.getUserContext().getUserId())
            .taskName(createTask.getTaskName())
            .description(createTask.getDescription())
            .category(category)
            .build();
  }

  Category createCategory(CreateCategoryDto category) {
    return Category.builder()
            .categoryName(category.getCategoryName())
            .createdBy(ContextHolder.getUserContext().getUserId())
            .createdAt(instantProvider.now())
            .tasks(new ArrayList<>())
            .build();
  }
}
