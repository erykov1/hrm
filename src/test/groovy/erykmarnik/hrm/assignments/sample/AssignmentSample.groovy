package erykmarnik.hrm.assignments.sample

import erykmarnik.hrm.assignments.dto.AssignmentDto
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto

import java.time.Instant

trait AssignmentSample {
  static final long ASSIGNMENT_ID = 191L
  static final long ASSIGNEE = 11L
  static final UUID OBJECT_ID = UUID.fromString("bb2f96af-839d-46c7-a558-b0ec5d7069ac")
  static final long CREATED_BY = 113L

  private Map<String, Object> DEFAULT_ASSIGNMENT_DATA = [
          assignmentId: ASSIGNMENT_ID,
          userId: ASSIGNEE,
          objectId: OBJECT_ID,
          assignedAt: Instant.now(),
          doneAt: Instant.now(),
          assignmentCreatedBy: CREATED_BY,
          assignmentStatus: AssignmentStatusDto.DONE
  ] as Map<String, Object>

  AssignmentDto createAssignment(Map<String, Object> changes = [:]) {
    def changesWithDefaults = DEFAULT_ASSIGNMENT_DATA + changes
    AssignmentDto.builder()
      .assignmentId(changesWithDefaults.assignmentId as Long)
      .userId(changesWithDefaults.userId as Long)
      .objectId(changesWithDefaults.objectId as UUID)
      .assignedAt(changesWithDefaults.assignedAt as Instant)
      .doneAt(changesWithDefaults.doneAt as Instant)
      .assignmentCreatedBy(changesWithDefaults.assignmentCreatedBy as Long)
      .assignmentStatus(changesWithDefaults.assignmentStatus as AssignmentStatusDto)
      .build()
  }
}