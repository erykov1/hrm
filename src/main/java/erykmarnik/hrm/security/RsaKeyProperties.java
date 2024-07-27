package erykmarnik.hrm.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@AllArgsConstructor
@Getter
@ConfigurationProperties(prefix = "rsa")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RsaKeyProperties {
  RSAPublicKey publicKey;
  RSAPrivateKey privateKey;
}
