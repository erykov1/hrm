package erykmarnik.hrm.assignments;

import erykmarnik.hrm.assignments.domain.AssignmentFacade;
import erykmarnik.hrm.assignments.dto.AssignmentDto;
import erykmarnik.hrm.assignments.dto.CreateAssignmentDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignment")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class AssignmentController {
  AssignmentFacade assignmentFacade;

  @Autowired
  AssignmentController(AssignmentFacade assignmentFacade) {
    this.assignmentFacade = assignmentFacade;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/create")
  ResponseEntity<AssignmentDto> createAssignment(@RequestBody CreateAssignmentDto createAssignment) {
    return ResponseEntity.ok(assignmentFacade.createAssignment(createAssignment));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/delete/{assignmentId}")
  ResponseEntity<Void> deleteAssignment(@PathVariable Long assignmentId) {
    assignmentFacade.deleteAssignment(assignmentId);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  @PutMapping("/done/{assignmentId}")
  ResponseEntity<Void> setToDone(@PathVariable Long assignmentId) {
    assignmentFacade.setAssignmentToDone(assignmentId);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  @GetMapping("/{assignmentId}")
  ResponseEntity<AssignmentDto> getAssignmentById(@PathVariable Long assignmentId) {
    return ResponseEntity.ok(assignmentFacade.getAssignmentById(assignmentId));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/all")
  ResponseEntity<List<AssignmentDto>> getAllAssignments() {
    return ResponseEntity.ok(assignmentFacade.getAllAssignments());
  }
}
