package erykmarnik.hrm.assignments.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentNoteModifyDto {
  String noteContent;

  @JsonCreator
  public AssignmentNoteModifyDto(@JsonProperty("noteContent") String noteContent) {
    this.noteContent = noteContent;
  }
}
