package erykmarnik.hrm.assignments.domain;

import erykmarnik.hrm.assignments.dto.AssignmentEvent;
import erykmarnik.hrm.notification.domain.MessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class EventPublisher {
  ApplicationEventPublisher applicationEventPublisher;

  void emmitAssignationAdded(String userMail, String taskName) {
    applicationEventPublisher.publishEvent(new AssignmentEvent(MessageType.ASSIGNED, userMail, taskName));
  }

  void emmitAssignationRemoved(String userMail, String taskName) {
    applicationEventPublisher.publishEvent(new AssignmentEvent(MessageType.REMOVED, userMail, taskName));
  }

  void emmitAssignationStatusChanged(String userMail, String taskName) {
    applicationEventPublisher.publishEvent(new AssignmentEvent(MessageType.STATUS_CHANGED, userMail, taskName));
  }
}
