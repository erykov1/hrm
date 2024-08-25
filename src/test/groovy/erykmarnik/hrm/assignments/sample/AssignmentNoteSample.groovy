package erykmarnik.hrm.assignments.sample

import erykmarnik.hrm.assignments.dto.AssignmentNoteDto


trait AssignmentNoteSample {
  private static final UUID NOTE_ID = UUID.fromString("344c45d8-6f97-4432-93f5-5ee73c848ca3")
  static final String NOTE_CONTENT = "Onboarding note"
  private static final Long ASSIGNMENT_ID = 111L

  private Map<String, Object> DEFAULT_ASSIGNMENT_NOTE_DATA = [
          noteId: NOTE_ID,
          noteContent: NOTE_CONTENT,
          assignmentId: ASSIGNMENT_ID
  ] as Map<String, Object>

  AssignmentNoteDto createNoteAssignment(Map<String, Object> changes = [:]) {
    def changesWithDefaults = DEFAULT_ASSIGNMENT_NOTE_DATA + changes
    AssignmentNoteDto.builder()
      .noteId(changesWithDefaults.noteId as UUID)
      .noteContent(changesWithDefaults.noteContent as String)
      .assignmentId(changesWithDefaults.assignmentId as Long)
      .build()
  }
}