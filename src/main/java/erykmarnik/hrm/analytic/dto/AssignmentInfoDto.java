package erykmarnik.hrm.analytic.dto;

import erykmarnik.hrm.assignments.dto.AssignmentStatusDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Builder
@EqualsAndHashCode
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentInfoDto {
  String username;
  String name;
  String surname;
  String objectName;
  Date assignedAt;
  Date endedAt;
  AssignmentStatusDto assignmentStatus;
  String category;
}
