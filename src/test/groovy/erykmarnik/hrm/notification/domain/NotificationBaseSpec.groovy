package erykmarnik.hrm.notification.domain

import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import spock.lang.Specification

abstract class NotificationBaseSpec extends Specification {
  BundleProvider bundleProvider = new BundleProvider() {
    @Override
    protected String getPathToResourceBundle() {
      return "locales.hrm_messages"
    }
  }
  MailSender mailSender = Mock(MailSender.class)
  NotificationSender notificationSender = new NotificationSender(mailSender, new NotificationMessageGetter(bundleProvider))
  NotificationFacade notificationFacade = new NotificationFacade(notificationSender)
  static final String USER_MAIL = "kowalski@mail.com"
  static final String TASK_NAME = "Onboarding"

  SimpleMailMessage createMessage(String to, String text, String subject) {
    SimpleMailMessage mailMessage = new SimpleMailMessage()
    mailMessage.setFrom(System.getenv("spring.mail.username"))
    mailMessage.setTo(to)
    mailMessage.setText(text)
    mailMessage.setSubject(subject)
    return mailMessage
  }
}
