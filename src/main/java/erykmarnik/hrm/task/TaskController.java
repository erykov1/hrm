package erykmarnik.hrm.task;

import erykmarnik.hrm.task.domain.TaskFacade;
import erykmarnik.hrm.task.dto.CreateTaskDto;
import erykmarnik.hrm.task.dto.ModifyTaskDto;
import erykmarnik.hrm.task.dto.TaskDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class TaskController {
  TaskFacade taskFacade;

  @Autowired
  TaskController(TaskFacade taskFacade) {
    this.taskFacade = taskFacade;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/create")
  ResponseEntity<TaskDto> createTask(@RequestBody CreateTaskDto createTask) {
    return ResponseEntity.ok(taskFacade.createTask(createTask));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{taskId}/modify")
  ResponseEntity<TaskDto> modifyTask(@PathVariable UUID taskId, @RequestBody ModifyTaskDto modifyTask) {
    return ResponseEntity.ok(taskFacade.modifyTask(taskId, modifyTask));
  }

  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  @GetMapping("/{taskId}")
  ResponseEntity<TaskDto> getTask(@PathVariable UUID taskId) {
    return ResponseEntity.ok(taskFacade.findByTaskId(taskId));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{taskId}/delete")
  ResponseEntity<Void> deleteTask(@PathVariable UUID taskId) {
    taskFacade.deleteTask(taskId);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Hidden
  @GetMapping("/all")
  ResponseEntity<List<TaskDto>> getAll() {
    return ResponseEntity.ok(taskFacade.getAll());
  }
}
