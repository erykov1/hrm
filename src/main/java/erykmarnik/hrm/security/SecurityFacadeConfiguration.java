package erykmarnik.hrm.security;


import erykmarnik.hrm.user.domain.UserFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.jwt.JwtEncoder;

@Configuration
class SecurityFacadeConfiguration {
  @Bean
  JwtTokenGenerator jwtTokenGenerator(JwtEncoder jwtEncoder) {
    return new JwtTokenGenerator(jwtEncoder);
  }

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
