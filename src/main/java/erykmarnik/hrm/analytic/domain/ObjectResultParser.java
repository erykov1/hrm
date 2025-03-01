package erykmarnik.hrm.analytic.domain;

import erykmarnik.hrm.analytic.dto.AssignmentInfoDto;
import erykmarnik.hrm.analytic.dto.UserAssignmentDto;
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ObjectResultParser {
  static UserAssignmentDto parseToUserAssignment(Object[] results) {
    String objectName = results[0].toString();
    Date assignedAt = Date.from(Instant.parse(results[1].toString()));
    Date endedAt = results[2] == null ? null : Date.from(Instant.parse(results[2].toString()));
    AssignmentStatusDto assignmentStatus = AssignmentStatusDto.valueOf(results[3].toString());
    String category = results[4].toString();
    return UserAssignmentDto.builder()
            .objectName(objectName)
            .assignedAt(assignedAt)
            .endedAt(endedAt)
            .assignmentStatus(assignmentStatus)
            .category(category)
            .build();
  }

  static AssignmentInfoDto parseToAssignmentInfo(Object[] results) {
    String username = results[0].toString();
    String name = results[1].toString();
    String surname = results[2].toString();
    String objectName = results[3].toString();
    Date assignedAt = Date.from(Instant.parse(results[4].toString()));
    Date endedAt = results[5] == null ? null : Date.from(Instant.parse(results[5].toString()));
    AssignmentStatusDto assignmentStatus = AssignmentStatusDto.valueOf(results[6].toString());
    String category = results[7].toString();
    return AssignmentInfoDto.builder()
            .username(username)
            .name(name)
            .surname(surname)
            .objectName(objectName)
            .assignedAt(assignedAt)
            .endedAt(endedAt)
            .assignmentStatus(assignmentStatus)
            .category(category)
            .build();
  }
}
