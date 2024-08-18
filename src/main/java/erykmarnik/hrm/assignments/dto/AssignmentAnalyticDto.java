package erykmarnik.hrm.assignments.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Builder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentAnalyticDto {
  Long userId;
  String username;
  String name;
  String surname;
  String objectName;
  Long minutesTakenToDone;
  Date startedAt;
  Date endedAt;
  AssignmentStatusDto assignmentStatus;
}
