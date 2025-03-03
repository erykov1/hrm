package erykmarnik.hrm.assignments.domain

import erykmarnik.hrm.assignments.dto.AssignmentNoteDto
import erykmarnik.hrm.assignments.sample.AssignmentAnalyticSample
import erykmarnik.hrm.assignments.sample.AssignmentNoteSample
import erykmarnik.hrm.assignments.sample.AssignmentSample
import erykmarnik.hrm.integration.IntegrationSpec
import erykmarnik.hrm.integration.UserRequest
import erykmarnik.hrm.task.domain.CategoryApiFacade
import erykmarnik.hrm.task.domain.CategorySample
import erykmarnik.hrm.task.domain.TaskApiFacade
import erykmarnik.hrm.task.sample.TaskSample
import erykmarnik.hrm.user.domain.UserApiFacade
import erykmarnik.hrm.user.sample.UserSample
import erykmarnik.hrm.utils.TimeApiFacade
import erykmarnik.hrm.utils.sample.TimeSample

class AssignmentAcceptanceBaseSpec extends IntegrationSpec implements UserSample, AssignmentSample, TaskSample, TimeSample,
        AssignmentAnalyticSample, AssignmentNoteSample, CategorySample {
  AssignmentApiFacade assignmentApiFacade
  UserApiFacade userApiFacade
  TaskApiFacade taskApiFacade
  TimeApiFacade timeApiFacade
  AssignmentNoteApiFacade assignmentNoteApiFacade
  CategoryApiFacade categoryApiFacade

  def setup() {
    assignmentApiFacade = new AssignmentApiFacade(mockMvc, objectMapper)
    userApiFacade = new UserApiFacade(mockMvc, objectMapper)
    taskApiFacade = new TaskApiFacade(mockMvc, objectMapper)
    timeApiFacade = new TimeApiFacade(mockMvc, objectMapper)
    assignmentNoteApiFacade = new AssignmentNoteApiFacade(mockMvc, objectMapper)
    categoryApiFacade = new CategoryApiFacade(mockMvc, objectMapper)
  }

  long deleteAssignment(long assignmentId, UserRequest userRequest) {
    if (assignmentId != 0) {
      assignmentApiFacade.deleteAssignment(assignmentId, userRequest)
    }
    return 0
  }

  void deleteAssignmentNote(Long assignmentId, AssignmentNoteDto note, UserRequest userRequest) {
    if (note != null && isNoteExisting(assignmentId, note, userRequest)) {
      assignmentNoteApiFacade.deleteAssignmentNote(note.noteId, userRequest)
    }
  }

  void deleteTask(UUID taskId, UserRequest userRequest) {
    if (taskId != null) {
      taskApiFacade.deleteTask(taskId, userRequest)
    }
  }

  private boolean isNoteExisting(Long assignmentId, AssignmentNoteDto note, UserRequest userRequest) {
    return assignmentNoteApiFacade.getAssignmentNotesFor(assignmentId, userRequest).contains(note)
  }
}
