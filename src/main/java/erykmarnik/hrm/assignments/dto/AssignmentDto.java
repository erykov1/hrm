package erykmarnik.hrm.assignments.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentDto {
  Long assignmentId;
  Long userId;
  UUID objectId;
  Instant assignedAt;
  Instant doneAt;
  Long assignmentCreatedBy;
  AssignmentStatusDto assignmentStatus;
  Instant dueTo;
}
