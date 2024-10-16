package erykmarnik.hrm.task.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
@EqualsAndHashCode
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskDto {
  UUID taskId;
  Instant createdAt;
  Long createdBy;
  String taskName;
  String description;
  Long categoryId;
}
