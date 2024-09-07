package erykmarnik.hrm.notification.domain;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class NotificationFacadeConfiguration {
  @Bean
  NotificationMessageGetter notificationMessageGetter() {
    return new NotificationMessageGetter(new BundleProvider());
  }

  @Bean
  NotificationSender notificationSender(MailSender mailSender, NotificationMessageGetter messageGetter) {
    return new NotificationSender(mailSender, messageGetter);
  }

  @Bean
  NotificationFacade notificationFacade(NotificationSender notificationSender) {
    return new NotificationFacade(notificationSender);
  }
}
