package erykmarnik.hrm.assignments.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface AssignmentRepository extends JpaRepository<Assignment, Long> {
  Optional<Assignment> findByAssignmentId(Long assignmentId);
  @Query("SELECT a FROM Assignment a WHERE a.objectId = :objectId AND a.userId = :userId")
  Optional<Assignment> findByObjectIdAndUserId(@Param("objectId") Long objectId, @Param("userId") Long userId);
  @Query("SELECT a FROM Assignment a WHERE a.userId = :userId")
  List<Assignment> findUserAssignments(@Param("userId") Long userId);
  @Query("SELECT a FROM Assignment a WHERE a.assignmentStatus = 'NOT_STARTED'")
  List<Assignment> findAllNotStarted();
  @Query("SELECT a FROM Assignment a WHERE a.assignmentStatus = 'DONE'")
  List<Assignment> findAllDone();
  @Query("SELECT an FROM AssignmentNote an WHERE an.noteId = :noteId")
  Optional<AssignmentNote> findAssignmentNoteById(@Param("noteId") UUID noteId);
}
