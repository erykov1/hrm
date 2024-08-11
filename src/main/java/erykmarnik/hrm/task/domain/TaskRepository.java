package erykmarnik.hrm.task.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface TaskRepository extends JpaRepository<Task, Long> {
  Optional<Task> findTaskByTaskId(Long taskId);
  boolean existsByTaskId(Long taskId);
}
