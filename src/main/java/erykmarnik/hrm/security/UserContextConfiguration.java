package erykmarnik.hrm.security;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserContextConfiguration {
  @Bean
  UserContextInterceptor userContextInterceptor(JwtDecodeGetter jwtDecodeGetter) {
    return new UserContextInterceptor(jwtDecodeGetter);
  }
}
