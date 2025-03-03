package erykmarnik.hrm.assignments.domain

import erykmarnik.hrm.assignments.dto.AssignmentDto
import erykmarnik.hrm.assignments.dto.AssignmentNoteDto
import erykmarnik.hrm.assignments.dto.AssignmentNoteModifyDto
import erykmarnik.hrm.assignments.dto.CreateAssignmentNoteDto
import erykmarnik.hrm.integration.UserRequest
import erykmarnik.hrm.task.dto.CategoryDto
import erykmarnik.hrm.task.dto.CreateCategoryDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.user.dto.UserDto

class AssignmentNoteAcceptanceSpec extends AssignmentAcceptanceBaseSpec {
  private UserDto jane
  private UserDto mike
  private TaskDto onboarding
  private AssignmentDto assignment
  private AssignmentNoteDto assignmentNote
  private CategoryDto newEmployee

  def setup() {
    timeApiFacade.useFixedClock(NOW)

    given: "there is admin $jane"
      jane = userApiFacade.createAdmin(createNewUser(username: "jane123", name: "Jane", surname: "Doe", email: "jane@mail.com"))
    and: "there is employee $mike"
      mike = userApiFacade.createEmployee(createNewUser(username: "mike123", name: "Mike", surname: "Smith", email: "mike@mail.com"))
    and: "there is category $newEmployee"
      newEmployee = categoryApiFacade.createCategory(new CreateCategoryDto(CATEGORY_NAME), new UserRequest(jane))
    and: "there is task $onboarding assigned to category $newEmployee"
      onboarding = taskApiFacade.createTask(createNewTask(createdAt: NOW, categoryId: newEmployee.categoryId), new UserRequest(jane))
    and: "user $mike is assigned to task $onboarding"
      assignment = assignmentApiFacade.createAssignment(createNewAssignment(userId: mike.userId, objectId: onboarding.taskId), new UserRequest(jane))
  }

  def cleanup() {
    deleteAssignmentNote(assignment.assignmentId, assignmentNote, new UserRequest(mike))
    deleteAssignment(assignment.assignmentId, new UserRequest(jane))
    taskApiFacade.deleteTask(onboarding.taskId, new UserRequest(jane))
    categoryApiFacade.deleteCategory(newEmployee.categoryId, new UserRequest(jane))
    userApiFacade.deleteUser(jane.userId, new UserRequest(jane))
    userApiFacade.deleteUser(mike.userId, new UserRequest(mike))
    timeApiFacade.useSystemClock()
  }

  def "Should be able to add note to assigned object"() {
    when: "user $mike adds note"
      assignmentNote = assignmentNoteApiFacade.addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId), new UserRequest(mike))
    then: "note is added to assigned object"
      assignmentNote == createNoteAssignment(noteId: assignmentNote.noteId, noteContent: NOTE_CONTENT, assignmentId: assignment.assignmentId)
  }

  def "Admin should be able to add note to assigned object to user"() {
    when: "admin $jane adds note"
      assignmentNote = assignmentNoteApiFacade.addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId), new UserRequest(jane))
    then: "note is added to assigned object"
      assignmentNote == createNoteAssignment(noteId: assignmentNote.noteId, noteContent: NOTE_CONTENT, assignmentId: assignment.assignmentId)
  }

  def "Should be able to delete note"() {
    given: "user $mike adds note"
      assignmentNote = assignmentNoteApiFacade.addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId), new UserRequest(mike))
    when: "$mike deletes note"
      deleteAssignmentNote(assignment.assignmentId, assignmentNote, new UserRequest(mike))
    then: "note $assignmentNote is deleted"
      assignmentNoteApiFacade.getAssignmentNotesFor(assignment.assignmentId, new UserRequest(mike)) == []
  }

  def "Should be able to modify note content"() {
    given: "user $mike adds note"
      assignmentNote = assignmentNoteApiFacade.addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId), new UserRequest(mike))
    when: "$mike modifies note $assignmentNote content"
      assignmentNoteApiFacade.modifyAssignmentNote(assignmentNote.noteId, new AssignmentNoteModifyDto("new assignment note content"), new UserRequest(mike))
    then: "note content $assignmentNote is modified"
      assignmentNoteApiFacade.getAssignmentNotesFor(assignment.assignmentId, new UserRequest(mike)) == [createNoteAssignment(noteId: assignmentNote.noteId,
        noteContent: "new assignment note content", assignmentId: assignment.assignmentId
      )]
  }
}
