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
            .assignedTo(createTask.getAssignedTo())
            .createdBy(ContextHolder.getUserContext().getUserId())
            .taskStatus(TaskStatus.NOT_STARTED)
            .taskName(createTask.getTaskName())
            .description(createTask.getDescription())
            .build();
  }
}
