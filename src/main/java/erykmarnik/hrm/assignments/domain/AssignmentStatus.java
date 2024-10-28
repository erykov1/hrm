package erykmarnik.hrm.assignments.domain;

import erykmarnik.hrm.assignments.dto.AssignmentStatusDto;

enum AssignmentStatus {
  DONE,
  OVERDUE,
  NOT_STARTED;

  AssignmentStatusDto dto() {
    return AssignmentStatusDto.valueOf(name());
  }
}
