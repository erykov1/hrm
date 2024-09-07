package erykmarnik.hrm.notification.domain;

import erykmarnik.hrm.notification.dto.NotificationDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

class RemovedNotificationMessage implements NotificationMessageHandler {
  private static final String KEY = "assignment.remove";
  private static final String SUBJECT_KEY = "assignment.remove.subject";

  @Override
  public NotificationDto getNotification(BundleProvider bundleProvider, String taskName) {
    return new NotificationDto(bundleProvider.getResourceBundle().getString(SUBJECT_KEY),
            String.format(bundleProvider.getResourceBundle().getString(KEY), taskName));
  }

  @Override
  public boolean supports(MessageType messageType) {
    return messageType.equals(MessageType.REMOVED);
  }
}
