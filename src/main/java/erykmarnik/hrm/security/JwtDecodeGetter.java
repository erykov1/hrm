package erykmarnik.hrm.security;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class JwtDecodeGetter {
  JwtDecoder jwtDecoder;

  public JwtDecodeGetter(Environment env) throws NoSuchAlgorithmException, InvalidKeySpecException {
    String publicKeyString = Objects.requireNonNull(env.getProperty("token.decoder"));
    byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(spec);
    this.jwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
  }

  public Jwt decodeToken(String token) {
    return jwtDecoder.decode(token);
  }
}
