package erykmarnik.hrm.security;


import erykmarnik.hrm.user.dto.UserContext;
import erykmarnik.hrm.utils.ContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.security.oauth2.jwt.Jwt;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserContextInterceptor implements HandlerInterceptor {
  JwtDecodeGetter jwtDecodeGetter;

  @Autowired
  UserContextInterceptor(JwtDecodeGetter jwtDecodeGetter) {
    this.jwtDecodeGetter = jwtDecodeGetter;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      Jwt jwt = jwtDecodeGetter.decodeToken(authHeader.substring(7));
      String userId = jwt.getClaimAsString("iss");
      if (userId != null) {
        ContextHolder.setUserContext(new UserContext(Long.valueOf(userId)));
      }
    }
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    ContextHolder.clear();
  }
}
