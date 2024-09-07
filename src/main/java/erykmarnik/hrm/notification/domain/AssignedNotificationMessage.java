package erykmarnik.hrm.notification.domain;

import erykmarnik.hrm.notification.dto.NotificationDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AssignedNotificationMessage implements NotificationMessageHandler {
  private static final String KEY = "assignment.assign";
  private static final String SUBJECT_KEY = "assignment.assign.subject";

  @Override
  public NotificationDto getNotification(BundleProvider bundleProvider, String taskName) {
    return new NotificationDto(bundleProvider.getResourceBundle().getString(SUBJECT_KEY),
            String.format(bundleProvider.getResourceBundle().getString(KEY), taskName));
  }

  @Override
  public boolean supports(MessageType messageType) {
    return messageType.equals(MessageType.ASSIGNED);
  }
}
