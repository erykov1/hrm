package erykmarnik.hrm.assignments.domain

import erykmarnik.hrm.assignments.dto.AssignmentDto
import erykmarnik.hrm.assignments.dto.AssignmentNoteDto
import erykmarnik.hrm.assignments.dto.AssignmentNoteModifyDto
import erykmarnik.hrm.assignments.dto.CreateAssignmentDto
import erykmarnik.hrm.assignments.dto.CreateAssignmentNoteDto
import erykmarnik.hrm.assignments.exception.ForbiddenAssignmentOperationException
import erykmarnik.hrm.assignments.sample.AssignmentNoteSample
import erykmarnik.hrm.assignments.sample.AssignmentSample
import erykmarnik.hrm.utils.ContextSpec
import erykmarnik.hrm.utils.InstantProvider
import erykmarnik.hrm.utils.sample.TimeSample
import org.springframework.context.ApplicationEventPublisher

class AssignmentNoteSpec extends ContextSpec implements TimeSample, AssignmentSample, AssignmentNoteSample {
  private InstantProvider instantProvider = new InstantProvider()
  private ApplicationEventPublisher eventPublisher = Mock(ApplicationEventPublisher)
  private AssignmentFacade assignmentFacade = new AssignmentConfiguration().assignmentFacade(instantProvider, securityFacade, Stub(AssignmentAnalytic.class), eventPublisher)
  private AssignmentDto assignment

  def setup() {
    instantProvider.useFixedClock(NOW)
    given: "admin $ADMIN_JANE is logged in"
      loginUser(ADMIN_JANE)
    and: "admin $ADMIN_JANE created assignment for user $EMPLOYEE_MIKE"
      assignment = assignmentFacade.createAssignment(new CreateAssignmentDto(EMPLOYEE_MIKE, OBJECT_ID))
    and: "employee $EMPLOYEE_MIKE is logged in"
      loginUser(EMPLOYEE_MIKE)
  }

  def "Should add note to assigned object"() {
    when: "user $EMPLOYEE_MIKE adds note"
      AssignmentNoteDto note = assignmentFacade.addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId))
    then: "note is added"
      note == createNoteAssignment(noteId: note.noteId, noteContent: NOTE_CONTENT, assignmentId: assignment.assignmentId)
  }

  def "Should not add note if not authorized user tries to add"() {
    given: "employee $EMPLOYEE_JOHN logs in"
      loginUser(EMPLOYEE_JOHN)
    when: "employee $EMPLOYEE_JOHN tries to add note to object"
      assignmentFacade.addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId))
    then: "note is not added"
      thrown(ForbiddenAssignmentOperationException)
  }

  def "Should modify note to assigned object"() {
    given: "user $EMPLOYEE_MIKE adds note to assignment"
      UUID noteId = assignmentFacade.addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId)).noteId
    when: "user $EMPLOYEE_MIKE edits note content"
      AssignmentNoteDto assignmentNote = assignmentFacade.modifyAssignmentNote(noteId, new AssignmentNoteModifyDto("new content for note"))
    then: "note content is modified"
      assignmentNote == createNoteAssignment(noteId: assignmentNote.noteId, noteContent: "new content for note", assignmentId: assignment.assignmentId)
  }

  def "Should not modify note to object that user is not assigned"() {
    given: "user $EMPLOYEE_MIKE adds note to assignment"
      UUID noteId = assignmentFacade.addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId)).noteId
    and: "user $EMPLOYEE_JOHN logs in"
      loginUser(EMPLOYEE_JOHN)
    when: "user $EMPLOYEE_JOHN tries to edit note content"
      assignmentFacade.modifyAssignmentNote(noteId, new AssignmentNoteModifyDto("new content for note"))
    then: "note content is not modified"
     thrown(ForbiddenAssignmentOperationException)
  }

  def "Should not get note if user is not assigned to object"() {
    given: "user $EMPLOYEE_MIKE adds note to assignment"
      assignmentFacade.addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId))
    and: "user $EMPLOYEE_JOHN logs in"
      loginUser(EMPLOYEE_JOHN)
    when: "user $EMPLOYEE_JOHN asks for note"
      assignmentFacade.getNotesForAssignment(assignment.assignmentId)
    then: "user does not get note"
      thrown(ForbiddenAssignmentOperationException)
  }

  def "Should be able to delete note to assigned object"() {
    given: "user $EMPLOYEE_MIKE adds note to assignment"
      UUID noteId = assignmentFacade.addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId)).noteId
    when: "user $EMPLOYEE_MIKE deletes note"
      assignmentFacade.deleteAssignmentNote(noteId)
    then: "note is deleted"
      assignmentFacade.getNotesForAssignment(assignment.assignmentId) == []
  }

  def "Should not be able to delete note if user is not assigned to object"() {
    given: "user $EMPLOYEE_MIKE adds note to assignment"
      UUID noteId = assignmentFacade.addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId)).noteId
    and: "user $EMPLOYEE_JOHN logs in"
      loginUser(EMPLOYEE_JOHN)
    when: "user $EMPLOYEE_JOHN tries to delete note"
      assignmentFacade.deleteAssignmentNote(noteId)
    then: "note is not deleted"
      thrown(ForbiddenAssignmentOperationException)
  }

  def "Admin should be able to gets user notes"() {
    given: "user $EMPLOYEE_MIKE adds note to assignment"
      assignmentFacade.addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId)).noteId
    and: "admin $ADMIN_JANE is logged in"
      loginUser(ADMIN_JANE)
    when: "admin $ADMIN_JANE asks for user $EMPLOYEE_MIKE notes"
      List<AssignmentNoteDto> notes = assignmentFacade.getNotesForAssignment(assignment.assignmentId)
    then: "admin gets note"
      notes == [createNoteAssignment(noteId: notes[0].noteId, noteContent: NOTE_CONTENT, assignmentId: assignment.assignmentId)]
  }

  def "Should be able to gets notes"() {
    given: "user $EMPLOYEE_MIKE adds note to assignment"
      assignmentFacade.addAssignmentNote(new CreateAssignmentNoteDto(NOTE_CONTENT, assignment.assignmentId)).noteId
    when: "user $EMPLOYEE_MIKE asks for his notes"
      List<AssignmentNoteDto> notes = assignmentFacade.getNotesForAssignment(assignment.assignmentId)
    then: "user $EMPLOYEE_MIKE gets note"
      notes == [createNoteAssignment(noteId: notes[0].noteId, noteContent: NOTE_CONTENT, assignmentId: assignment.assignmentId)]
  }
}
