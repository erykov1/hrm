package erykmarnik.hrm.assignments;

import erykmarnik.hrm.assignments.domain.AssignmentFacade;
import erykmarnik.hrm.assignments.dto.*;
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

  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  @GetMapping("/user")
  ResponseEntity<List<AssignmentDto>> getUserAssignments() {
    return ResponseEntity.ok(assignmentFacade.getUserAssignments());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/all")
  ResponseEntity<List<AssignmentDto>> getAllAssignments() {
    return ResponseEntity.ok(assignmentFacade.getAllAssignments());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/all/done")
  ResponseEntity<List<AssignmentAnalyticDto>> getAllDoneAssignments() {
    return ResponseEntity.ok(assignmentFacade.getAllDone());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/all/notStarted")
  ResponseEntity<List<AssignmentAnalyticDto>> getAllNotStartedAssignments() {
    return ResponseEntity.ok(assignmentFacade.getAllNotStarted());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/all/{userId}")
  ResponseEntity<List<AssignmentAnalyticDto>> getAllForUser(@PathVariable Long userId) {
    return ResponseEntity.ok(assignmentFacade.getAllUserAssignmentsFor(userId));
  }

  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  @PostMapping("/note/add")
  ResponseEntity<AssignmentNoteDto> addAssignmentNote(@RequestBody CreateAssignmentNoteDto note) {
    return ResponseEntity.ok(assignmentFacade.addAssignmentNote(note));
  }

  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  @DeleteMapping("/note/delete/{noteId}")
  ResponseEntity<Void> deleteAssignmentNote(@PathVariable UUID noteId) {
    assignmentFacade.deleteAssignmentNote(noteId);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  @GetMapping("/note/{assignmentId}")
  ResponseEntity<List<AssignmentNoteDto>> getAssignmentNotes(@PathVariable Long assignmentId) {
    return ResponseEntity.ok(assignmentFacade.getNotesForAssignment(assignmentId));
  }

  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  @PutMapping("/note/modify/{noteId}")
  ResponseEntity<AssignmentNoteDto> modifyAssignmentNote(@PathVariable UUID noteId, @RequestBody AssignmentNoteModifyDto noteModify) {
    return ResponseEntity.ok(assignmentFacade.modifyAssignmentNote(noteId, noteModify));
  }
}
