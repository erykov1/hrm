package erykmarnik.hrm.task.domain;

import erykmarnik.hrm.task.dto.CreateTaskDto;
import erykmarnik.hrm.utils.ContextHolder;
import erykmarnik.hrm.utils.InstantProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class TaskCreator {
  InstantProvider instantProvider;

  Task createTask(CreateTaskDto createTask) {
    return Task.builder()
            .createdAt(instantProvider.now())
            .createdBy(ContextHolder.getUserContext().getUserId())
            .taskName(createTask.getTaskName())
            .description(createTask.getDescription())
            .build();
  }
}
