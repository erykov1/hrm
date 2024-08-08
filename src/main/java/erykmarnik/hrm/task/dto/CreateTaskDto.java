package erykmarnik.hrm.task.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import java.time.Instant;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateTaskDto {
  Instant createdAt;
  Long assignedTo;
  String taskName;
  String description;
}
