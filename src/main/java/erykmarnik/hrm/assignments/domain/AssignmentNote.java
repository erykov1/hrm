package erykmarnik.hrm.assignments.domain;

import erykmarnik.hrm.assignments.dto.AssignmentNoteDto;
import erykmarnik.hrm.assignments.dto.AssignmentNoteModifyDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "assignment_note")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class AssignmentNote {
  @Id
  UUID noteId;
  String noteContent;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignment_id")
  Assignment assignment;

  AssignmentNoteDto dto() {
    return AssignmentNoteDto.builder()
            .noteId(noteId)
            .noteContent(noteContent)
            .assignmentId(assignment.dto().getAssignmentId())
            .build();
  }

  AssignmentNote modifyAssignmentNote(AssignmentNoteModifyDto note) {
    return AssignmentNote.builder()
            .noteId(noteId)
            .noteContent(note.getNoteContent())
            .assignment(assignment)
            .build();
  }
}
