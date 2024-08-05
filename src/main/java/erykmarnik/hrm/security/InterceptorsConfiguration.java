package erykmarnik.hrm.security;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class InterceptorsConfiguration implements WebMvcConfigurer {
  UserContextInterceptor userContextInterceptor;

  @Autowired
  InterceptorsConfiguration(UserContextInterceptor userContextInterceptor) {
    this.userContextInterceptor = userContextInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(userContextInterceptor);
  }
}
