package erykmarnik.hrm.analytic.dto;

import erykmarnik.hrm.assignments.dto.AssignmentStatusDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Builder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAssignmentDto {
  String objectName;
  Date assignedAt;
  Date endedAt;
  AssignmentStatusDto assignmentStatus;
  String category;
}
