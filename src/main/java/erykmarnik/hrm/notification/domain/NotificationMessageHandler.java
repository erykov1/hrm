package erykmarnik.hrm.notification.domain;

import erykmarnik.hrm.notification.dto.NotificationDto;

interface NotificationMessageHandler {
  NotificationDto getNotification(BundleProvider bundleProvider, String taskName);
  boolean supports(MessageType messageType);
}
