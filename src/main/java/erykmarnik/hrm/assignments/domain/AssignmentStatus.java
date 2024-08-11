package erykmarnik.hrm.assignments.domain;

import erykmarnik.hrm.assignments.dto.AssignmentStatusDto;

enum AssignmentStatus {
  DONE,
  NOT_STARTED;

  AssignmentStatusDto dto() {
    return AssignmentStatusDto.valueOf(name());
  }
}
