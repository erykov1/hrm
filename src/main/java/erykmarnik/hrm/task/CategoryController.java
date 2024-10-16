package erykmarnik.hrm.task;

import erykmarnik.hrm.task.domain.TaskFacade;
import erykmarnik.hrm.task.dto.CategoryDto;
import erykmarnik.hrm.task.dto.CreateCategoryDto;
import erykmarnik.hrm.task.dto.NewCategoryNameDto;
import erykmarnik.hrm.task.dto.TaskDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class CategoryController {
  TaskFacade taskFacade;

  @Autowired
  CategoryController(TaskFacade taskFacade) {
    this.taskFacade = taskFacade;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/create")
  ResponseEntity<CategoryDto> createCategory(@RequestBody CreateCategoryDto createCategory) {
    return ResponseEntity.ok(taskFacade.createCategory(createCategory));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{categoryId}/modify")
  ResponseEntity<CategoryDto> modifyCategory(@PathVariable Long categoryId, @RequestBody NewCategoryNameDto newCategoryName) {
    return ResponseEntity.ok(taskFacade.modifyCategory(categoryId, newCategoryName));
  }

  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  @GetMapping("/{categoryId}")
  ResponseEntity<CategoryDto> getCategory(@PathVariable Long categoryId) {
    return ResponseEntity.ok(taskFacade.getCategory(categoryId));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{categoryId}/delete")
  ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
    taskFacade.deleteCategory(categoryId);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  @GetMapping("/{categoryId}/tasks")
  ResponseEntity<List<TaskDto>> getTasksForCategory(@PathVariable Long categoryId) {
    return ResponseEntity.ok(taskFacade.getAllTasksForCategory(categoryId));
  }

  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  @GetMapping("/all")
  ResponseEntity<List<CategoryDto>> getAllCategories() {
    return ResponseEntity.ok(taskFacade.getAllCategories());
  }
}
