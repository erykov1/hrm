package erykmarnik.hrm.task.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignedTaskDto {
  String taskName;
  String categoryName;
}
