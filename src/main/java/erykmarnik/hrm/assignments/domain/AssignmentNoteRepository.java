package erykmarnik.hrm.assignments.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface AssignmentNoteRepository extends JpaRepository<AssignmentNote, UUID> {
  Optional<AssignmentNote> findByNoteId(UUID noteId);
  @Query("SELECT an FROM AssignmentNote an WHERE an.assignment.assignmentId = :assignmentId")
  List<AssignmentNote> findNotesForAssignment(@Param("assignmentId") Long assignmentId);
}
