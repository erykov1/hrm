package erykmarnik.hrm.assignments.domain

import erykmarnik.hrm.assignments.dto.AssignmentDto
import erykmarnik.hrm.assignments.dto.AssignmentNoteDto
import erykmarnik.hrm.assignments.dto.AssignmentNoteModifyDto
import erykmarnik.hrm.assignments.dto.CreateAssignmentDto
import erykmarnik.hrm.assignments.dto.CreateAssignmentNoteDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.user.dto.UserDto
import erykmarnik.hrm.utils.ContextHolder

class AssignmentNoteAcceptanceSpec extends AssignmentAcceptanceBaseSpec {
  private UserDto jane
  private UserDto mike
  private TaskDto onboarding
  private AssignmentDto assignment
  private AssignmentNoteDto assignmentNote

  def setup() {
    timeApiFacade.useFixedClock(NOW)

    given: "there is admin $jane"
      jane = userApiFacade.createAdmin(createNewUser(username: "jane123", name: "Jane", surname: "Doe", email: "jane@mail.com"))
    and: "there is employee $mike"
      mike = userApiFacade.createEmployee(createNewUser(username: "mike123", name: "Mike", surname: "Smith", email: "mike@mail.com"))
    and: "there is task $onboarding"
      ContextHolder.setUserContext(new UserContext(jane.userId))
      onboarding = taskApiFacade.createTask(createNewTask(createdAt: NOW))
    and: "user $mike is assigned to task $onboarding"
      assignment = createAssignment(jane.userId, new CreateAssignmentDto(mike.userId, onboarding.taskId))
  }

  def cleanup() {
    deleteAssignmentNote(assignment.assignmentId, assignmentNote, mike.userId)
    deleteAssignment(assignment, jane.userId)
    userApiFacade.deleteUser(jane.userId)
    userApiFacade.deleteUser(mike.userId)
    taskApiFacade.deleteTask(onboarding.taskId)
    timeApiFacade.useSystemClock()
    ContextHolder.clear()
  }

  def "Should be able to add note to assigned object"() {
    when: "user $mike adds note"
      assignmentNote = addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId), mike.userId)
    then: "note is added to assigned object"
      assignmentNote == createNoteAssignment(noteId: assignmentNote.noteId, noteContent: NOTE_CONTENT, assignmentId: assignment.assignmentId)
  }

  def "Admin should be able to add note to assigned object to user"() {
    when: "admin $jane adds note"
      assignmentNote = addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId), jane.userId)
    then: "note is added to assigned object"
      assignmentNote == createNoteAssignment(noteId: assignmentNote.noteId, noteContent: NOTE_CONTENT, assignmentId: assignment.assignmentId)
  }

  def "Should be able to delete note"() {
    given: "user $mike adds note"
      assignmentNote = addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId), mike.userId)
    when: "$mike deletes note"
      deleteAssignmentNote(assignment.assignmentId, assignmentNote, mike.userId)
    then: "note $assignmentNote is deleted"
      getAssignmentsNotesFor(assignment.assignmentId, mike.userId) == []
  }

  def "Should be able to modify note content"() {
    given: "user $mike adds note"
      assignmentNote = addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId), mike.userId)
    when: "$mike modifies note $assignmentNote content"
      modifyAssignmentNote(assignmentNote.noteId, new AssignmentNoteModifyDto("new assignment note content"), mike.userId)
    then: "note content $assignmentNote is modified"
      getAssignmentsNotesFor(assignment.assignmentId, mike.userId) == [createNoteAssignment(noteId: assignmentNote.noteId,
        noteContent: "new assignment note content", assignmentId: assignment.assignmentId
      )]
  }
}
