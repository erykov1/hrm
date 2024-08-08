package erykmarnik.hrm.task.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModifyTaskDto {
  Long assignedTo;
  TaskStatusDto taskStatus;
  String taskName;
  String description;
}
