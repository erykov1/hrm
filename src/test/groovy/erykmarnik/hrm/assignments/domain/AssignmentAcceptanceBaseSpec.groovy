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
import erykmarnik.hrm.task.domain.CategoryApiFacade
import erykmarnik.hrm.task.domain.CategorySample
import erykmarnik.hrm.task.domain.TaskApiFacade
import erykmarnik.hrm.task.dto.CategoryDto
import erykmarnik.hrm.task.dto.CreateCategoryDto
import erykmarnik.hrm.task.dto.CreateTaskDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.task.sample.TaskSample
import erykmarnik.hrm.user.domain.UserApiFacade
import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.user.sample.UserSample
import erykmarnik.hrm.utils.ContextHolder
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

  long deleteAssignment(long assignmentId, Long userId) {
    if (assignmentId != 0) {
      ContextHolder.setUserContext(new UserContext(userId))
      assignmentApiFacade.deleteAssignment(assignmentId)
    }
    return 0
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

  TaskDto createTaskRequest(Long userId, CreateTaskDto createTask) {
    ContextHolder.setUserContext(new UserContext(userId))
    return taskApiFacade.createTask(createTask)
  }

  void deleteCategory(long categoryId, long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    categoryApiFacade.deleteCategory(categoryId)
  }

  CategoryDto createCategoryRequest(CreateCategoryDto createCategory, long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    return categoryApiFacade.createCategory(createCategory)
  }

  void deleteTask(UUID taskId, long userId) {
    if (taskId != null) {
      ContextHolder.setUserContext(new UserContext(userId))
      taskApiFacade.deleteTask(taskId)
    }
  }

  private boolean isNoteExisting(Long assignmentId, AssignmentNoteDto note, Long userId) {
    ContextHolder.setUserContext(new UserContext(userId))
    return assignmentNoteApiFacade.getAssignmentNotesFor(assignmentId).contains(note)
  }
}
