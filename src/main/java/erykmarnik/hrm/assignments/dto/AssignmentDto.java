package erykmarnik.hrm.assignments.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Builder
@Getter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentDto {
  Long assignmentId;
  Long userId;
  Long objectId;
  Instant assignedAt;
  Instant doneAt;
  Long assignmentCreatedBy;
  AssignmentStatusDto assignmentStatus;
}
