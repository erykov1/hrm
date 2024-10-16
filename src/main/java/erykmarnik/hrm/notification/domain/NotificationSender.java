package erykmarnik.hrm.notification.domain;

import erykmarnik.hrm.notification.dto.NotificationDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class NotificationSender {
  MailSender mailSender;
  NotificationMessageGetter messageGetter;

  void sendMessage(String userMail, MessageType messageType, String taskName) {
    NotificationDto notification = messageGetter.getMessage(messageType, taskName);
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(System.getenv("spring.mail.username"));
    mailMessage.setTo(userMail);
    mailMessage.setText(notification.getMessage());
    mailMessage.setSubject(notification.getSubject());
    mailSender.send(mailMessage);
  }
}
