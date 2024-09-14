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
import java.util.UUID;


@Entity
@Table(name = "task")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class Task {
  @Id
  UUID taskId;
  Instant createdAt;
  Long createdBy;
  String taskName;
  String description;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  Category category;

  TaskDto dto() {
    return TaskDto.builder()
            .taskId(taskId)
            .createdAt(createdAt)
            .createdBy(createdBy)
            .taskName(taskName)
            .description(description)
            .categoryId(category.categoryDto().getCategoryId())
            .build();
  }

  Task modifyTask(ModifyTaskDto modifyTaskDto, Category category) {
    return Task.builder()
            .taskId(taskId)
            .createdAt(createdAt)
            .createdBy(createdBy)
            .taskName(modifyTaskDto.getTaskName() == null ? taskName : modifyTaskDto.getTaskName())
            .description(modifyTaskDto.getDescription() == null ? description : modifyTaskDto.getDescription())
            .category(category)
            .build();
  }
}
