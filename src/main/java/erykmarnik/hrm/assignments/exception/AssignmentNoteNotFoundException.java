package erykmarnik.hrm.assignments.exception;

import java.util.UUID;

public class AssignmentNoteNotFoundException extends RuntimeException {
  public AssignmentNoteNotFoundException(UUID noteId) {
    super("assignment note: " + noteId + " not found");
  }
}
