package erykmarnik.hrm.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class InstantProviderConfig {
  @Bean
  InstantProvider instantProvider() {
    return new InstantProvider();
  }
}
