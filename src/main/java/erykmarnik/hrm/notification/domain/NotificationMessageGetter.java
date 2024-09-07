package erykmarnik.hrm.notification.domain;

import erykmarnik.hrm.notification.dto.NotificationDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class NotificationMessageGetter {
  BundleProvider bundleProvider;
  private static final List<NotificationMessageHandler> HANDLERS = List.of(
          new RemovedNotificationMessage(), new AssignedNotificationMessage(), new StatusChangedNotificationMessage()
  );

  NotificationMessageGetter(BundleProvider bundleProvider) {
    this.bundleProvider = bundleProvider;
  }

  NotificationDto getMessage(MessageType messageType, String taskName) {
    return HANDLERS.stream()
            .filter(handler -> handler.supports(messageType))
            .findFirst().orElseThrow(RuntimeException::new).getNotification(bundleProvider, taskName);
  }
}
