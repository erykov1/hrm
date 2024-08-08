package erykmarnik.hrm.security;

import erykmarnik.hrm.user.domain.CustomUserDetailsService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class JwtTokenGenerator {
  JwtEncoder jwtEncoder;

  String generateToken(Authentication authentication) {
    Instant now = Instant.now();
    CustomUserDetailsService userDetails = (CustomUserDetailsService) authentication.getPrincipal();
    String role = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));
    JwtClaimsSet claimsSet = JwtClaimsSet.builder()
            .issuer(userDetails.getUserId().toString())
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.HOURS))
            .subject(authentication.getName())
            .claim("role", role)
            .build();
    return this.jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
  }
}
