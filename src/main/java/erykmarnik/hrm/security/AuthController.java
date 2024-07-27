package erykmarnik.hrm.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class AuthController {
  SecurityFacade securityFacade;

  @Autowired
  AuthController(SecurityFacade securityFacade) {
    this.securityFacade = securityFacade;
  }

  @PostMapping("/login")
  ResponseEntity<String> registerUser(@RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(securityFacade.createToken(loginRequest));
  }
}
