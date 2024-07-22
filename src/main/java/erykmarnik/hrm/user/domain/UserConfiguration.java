package erykmarnik.hrm.user.domain;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserConfiguration {
  @Bean
  UserFacade userFacade(UserRepository userRepository, UserCreator userCreator) {
    return new UserFacade(userRepository, userCreator);
  }

  UserFacade userFacade(UserCreator userCreator) {
    return new UserFacade(new InMemoryUserRepository(), userCreator);
  }
}
