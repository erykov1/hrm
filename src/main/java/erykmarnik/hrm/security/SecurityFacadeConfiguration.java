package erykmarnik.hrm.security;


import erykmarnik.hrm.user.domain.UserFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
class SecurityFacadeConfiguration {
  @Bean
  SecurityFacade securityFacade(JwtTokenGenerator tokenGenerator, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                                UserFacade userFacade) {
    return SecurityFacade.builder()
            .tokenGenerator(tokenGenerator)
            .passwordEncoder(passwordEncoder)
            .authenticationManager(authenticationManager)
            .userFacade(userFacade)
            .build();
  }
}
