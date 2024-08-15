package erykmarnik.hrm.assignments.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface AssignmentRepository extends JpaRepository<Assignment, Long> {
  Optional<Assignment> findByAssignmentId(Long assignmentId);
  @Query("SELECT a FROM Assignment a WHERE a.objectId = objectId AND a.userId = userId")
  Optional<Assignment> findByObjectIdAndUserId(@Param("objectId") Long objectId, @Param("userId") Long userId);
  @Query("SELECT a FROM Assignment a WHERE a.userId = userId")
  List<Assignment> findUserAssignments(@Param("userId") Long userId);
}
