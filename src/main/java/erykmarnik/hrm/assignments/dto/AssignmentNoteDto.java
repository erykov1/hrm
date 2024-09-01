package erykmarnik.hrm.assignments.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentNoteDto {
  UUID noteId;
  String noteContent;
  Long assignmentId;
}
