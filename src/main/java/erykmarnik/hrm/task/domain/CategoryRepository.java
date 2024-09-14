package erykmarnik.hrm.task.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByCategoryId(Long categoryId);
  @Query("SELECT t FROM Task t WHERE t.taskId := taskId")
  Optional<Task> findTaskById(@Param("taskId") UUID taskId);
  @Query("SELECT t FROM Task t")
  List<Task> findAllTasks();
}
