package erykmarnik.hrm.task.domain;

import erykmarnik.hrm.task.dto.ModifyTaskDto;
import erykmarnik.hrm.task.dto.TaskDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@Entity
@Table(name = "task")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long taskId;
  Instant createdAt;
  Long assignedTo;
  Long createdBy;
  @Enumerated(EnumType.STRING)
  TaskStatus taskStatus;
  Instant doneAt;
  String taskName;
  String description;

  TaskDto dto() {
    return TaskDto.builder()
            .taskId(taskId)
            .createdAt(createdAt)
            .assignedTo(assignedTo)
            .createdBy(createdBy)
            .taskStatus(taskStatus.dto())
            .doneAt(doneAt)
            .taskName(taskName)
            .description(description)
            .build();
  }

  Task modifyTask(ModifyTaskDto modifyTaskDto) {
    return Task.builder()
            .taskId(taskId)
            .createdAt(createdAt)
            .assignedTo(modifyTaskDto.getAssignedTo() == null ? assignedTo : modifyTaskDto.getAssignedTo())
            .createdBy(createdBy)
            .taskStatus(modifyTaskDto.getTaskStatus() == null ? taskStatus : TaskStatus.valueOf(modifyTaskDto.getTaskStatus().name()))
            .doneAt(doneAt)
            .taskName(modifyTaskDto.getTaskName() == null ? taskName : modifyTaskDto.getTaskName())
            .description(modifyTaskDto.getDescription() == null ? description : modifyTaskDto.getDescription())
            .build();
  }

  Task setToDone(Instant doneAt) {
    return Task.builder()
            .taskId(taskId)
            .createdAt(createdAt)
            .assignedTo(assignedTo)
            .createdBy(createdBy)
            .taskStatus(TaskStatus.DONE)
            .doneAt(doneAt)
            .taskName(taskName)
            .description(description)
            .build();
  }

  void setTaskId(Long taskId) {
    this.taskId = taskId;
  }
}
