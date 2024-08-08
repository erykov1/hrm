package erykmarnik.hrm.task.domain;

import erykmarnik.hrm.task.dto.TaskStatusDto;

enum TaskStatus {
  DONE,
  NOT_STARTED;

  TaskStatusDto dto() {
    return TaskStatusDto.valueOf(name());
  }
}
