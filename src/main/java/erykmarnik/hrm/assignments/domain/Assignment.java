package erykmarnik.hrm.assignments.domain;

import erykmarnik.hrm.assignments.dto.AssignmentDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "assignment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class Assignment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "assignment_id")
  Long assignmentId;
  @Column(name = "user_id")
  Long userId;
  @Column(name = "object_id")
  Long objectId;
  @Column(name = "assigned_at")
  Instant assignedAt;
  @Column(name = "done_at")
  Instant doneAt;
  @Column(name = "assignment_created_by")
  Long assignmentCreatedBy;
  @Column(name = "assignment_status")
  @Enumerated(EnumType.STRING)
  AssignmentStatus assignmentStatus;

  void setAssignmentId(Long assignmentId) {
    this.assignmentId = assignmentId;
  }

  AssignmentDto dto() {
    return AssignmentDto.builder()
            .assignmentId(assignmentId)
            .userId(userId)
            .objectId(objectId)
            .assignedAt(assignedAt)
            .doneAt(doneAt)
            .assignmentCreatedBy(assignmentCreatedBy)
            .assignmentStatus(assignmentStatus.dto())
            .build();
  }

  Assignment setToDone(Instant doneAt) {
    return Assignment.builder()
            .assignmentId(assignmentId)
            .userId(userId)
            .objectId(objectId)
            .assignedAt(assignedAt)
            .doneAt(doneAt)
            .assignmentCreatedBy(assignmentCreatedBy)
            .assignmentStatus(AssignmentStatus.DONE)
            .build();
  }
}
