package erykmarnik.hrm.assignments.dto;

import erykmarnik.hrm.notification.domain.MessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentEvent {
  MessageType messageType;
  String userMail;
  String taskName;
}
