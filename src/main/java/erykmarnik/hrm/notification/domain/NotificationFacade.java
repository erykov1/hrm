package erykmarnik.hrm.notification.domain;

import erykmarnik.hrm.assignments.dto.AssignmentEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationFacade {
  NotificationSender notificationSender;

  @Async
  @EventListener
  public void onAssignmentEvent(AssignmentEvent event) {
    log.info("Sending mail");
    notificationSender.sendMessage(event.getUserMail(), event.getMessageType(), event.getTaskName());
  }
}
