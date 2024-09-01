package erykmarnik.hrm.assignments.domain

import erykmarnik.hrm.assignments.dto.AssignmentDto
import erykmarnik.hrm.assignments.dto.AssignmentNoteDto
import erykmarnik.hrm.assignments.dto.AssignmentNoteModifyDto
import erykmarnik.hrm.assignments.dto.CreateAssignmentDto
import erykmarnik.hrm.assignments.dto.CreateAssignmentNoteDto
import erykmarnik.hrm.assignments.sample.AssignmentAnalyticSample
import erykmarnik.hrm.assignments.sample.AssignmentNoteSample
import erykmarnik.hrm.assignments.sample.AssignmentSample
import erykmarnik.hrm.integration.IntegrationSpec
import erykmarnik.hrm.task.domain.TaskApiFacade
import erykmarnik.hrm.task.sample.TaskSample
import erykmarnik.hrm.user.domain.UserApiFacade
import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.user.sample.UserSample
import erykmarnik.hrm.utils.ContextHolder
import erykmarnik.hrm.utils.TimeApiFacade
import erykmarnik.hrm.utils.sample.TimeSample

class AssignmentAcceptanceBaseSpec extends IntegrationSpec implements UserSample, AssignmentSample, TaskSample, TimeSample,
        AssignmentAnalyticSample, AssignmentNoteSample {
  AssignmentApiFacade assignmentApiFacade
  UserApiFacade userApiFacade
  TaskApiFacade taskApiFacade
  TimeApiFacade timeApiFacade
  AssignmentNoteApiFacade assignmentNoteApiFacade

  def setup() {
    assignmentApiFacade = new AssignmentApiFacade(mockMvc, objectMapper)
    userApiFacade = new UserApiFacade(mockMvc, objectMapper)
    taskApiFacade = new TaskApiFacade(mockMvc, objectMapper)
    timeApiFacade = new TimeApiFacade(mockMvc, objectMapper)
    assignmentNoteApiFacade = new AssignmentNoteApiFacade(mockMvc, objectMapper)
  }

  void deleteAssignment(AssignmentDto assignment, Long userId) {
    if (assignment != null) {
      ContextHolder.setUserContext(new UserContext(userId))
      assignmentApiFacade.deleteAssignment(assignment.assignmentId)
    }
  }

  void setToDone(Long assignmentId, Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    assignmentApiFacade.setToDone(assignmentId)
  }

  AssignmentDto getAssignment(Long assignmentId, Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    assignmentApiFacade.getAssignmentById(assignmentId)
  }

  List<AssignmentDto> getUserAssignment(Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    assignmentApiFacade.getUserAssignments()
  }

  AssignmentDto createAssignment(Long userId, CreateAssignmentDto createAssignment) {
    ContextHolder.setUserContext(new UserContext(userId))
    assignmentApiFacade.createAssignment(createAssignment)
  }

  void deleteAssignmentNote(Long assignmentId, AssignmentNoteDto note, Long userId) {
    if (note != null && isNoteExisting(assignmentId, note, userId)) {
      ContextHolder.setUserContext(new UserContext(userId))
      assignmentNoteApiFacade.deleteAssignmentNote(note.noteId)
    }
  }

  AssignmentNoteDto addAssignmentNote(CreateAssignmentNoteDto note, Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    return assignmentNoteApiFacade.addAssignmentNote(note)
  }

  AssignmentNoteDto modifyAssignmentNote(UUID noteId, AssignmentNoteModifyDto modify, Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    return assignmentNoteApiFacade.modifyAssignmentNote(noteId, modify)
  }

  List<AssignmentNoteDto> getAssignmentsNotesFor(Long assignmentId, Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    return assignmentNoteApiFacade.getAssignmentNotesFor(assignmentId)
  }

  private boolean isNoteExisting(Long assignmentId, AssignmentNoteDto note, Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    return assignmentNoteApiFacade.getAssignmentNotesFor(assignmentId).contains(note)
  }
}
