package erykmarnik.hrm.user.domain;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserConfiguration {
  @Bean
  UserCreator userCreator(BCryptPasswordEncoder bCryptPasswordEncoder) {
    return new UserCreator(bCryptPasswordEncoder);
  }

  @Bean
  UserFacade userFacade(UserRepository userRepository, UserCreator userCreator) {
    return new UserFacade(userRepository, userCreator);
  }

  UserFacade userFacade(UserCreator userCreator) {
    return new UserFacade(new InMemoryUserRepository(), userCreator);
  }
}
