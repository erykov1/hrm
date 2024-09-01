package erykmarnik.hrm.assignments.domain;

import erykmarnik.hrm.assignments.dto.AssignmentDto;
import erykmarnik.hrm.assignments.dto.AssignmentNoteModifyDto;
import erykmarnik.hrm.assignments.dto.CreateAssignmentNoteDto;
import erykmarnik.hrm.assignments.exception.AssignmentNoteNotFoundException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
  @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
  List<AssignmentNote> assignmentNotes = new ArrayList<>();

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
            .assignmentNotes(assignmentNotes)
            .build();
  }

  AssignmentNote addAssignmentNote(CreateAssignmentNoteDto createAssignmentNote) {
    if (assignmentNotes == null) {
      assignmentNotes = new ArrayList<>();
    }
    AssignmentNote assignmentNote = AssignmentNote.builder()
            .noteId(UUID.randomUUID())
            .noteContent(createAssignmentNote.getNoteContent())
            .assignment(this)
            .build();
    this.assignmentNotes.add(assignmentNote);
    return assignmentNote;
  }

  Assignment removeAssignmentNote(AssignmentNote assignmentNote) {
    this.assignmentNotes.remove(assignmentNote);
    return this;
  }

  List<AssignmentNote> getAssignmentNotes() {
    return this.assignmentNotes;
  }

  AssignmentNote getAssignmentNote(UUID noteId) {
    return this.assignmentNotes.stream()
            .filter(note -> note.dto().getNoteId().equals(noteId))
            .findFirst().orElseThrow(() -> new AssignmentNoteNotFoundException(noteId));
  }

  Assignment modifyAssignmentNote(UUID noteId, AssignmentNoteModifyDto noteModify) {
    AssignmentNote assignmentNote = getAssignmentNote(noteId);
    this.assignmentNotes.remove(assignmentNote);
    this.assignmentNotes.add(assignmentNote.modifyAssignmentNote(noteModify));
    return this;
  }
}
