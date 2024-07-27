package erykmarnik.hrm.security;

import erykmarnik.hrm.user.domain.UserFacade;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class SecurityFacade {
  JwtTokenGenerator tokenGenerator;
  PasswordEncoder passwordEncoder;
  AuthenticationManager authenticationManager;
  UserFacade userFacade;

  public String createToken(LoginRequest loginRequest) {
    String currentPswd = userFacade.getByUsername(loginRequest.getUsername()).getPassword();
    boolean matchPwd = passwordEncoder.bCryptPasswordEncoder().matches(loginRequest.getPassword(), currentPswd);
    if (matchPwd) {
      log.info("generating token");
      Authentication auth = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), currentPswd)
      );
      return tokenGenerator.generateToken(auth);
    } else {
      log.error("bad credentials");
      return "";
    }
  }
}
