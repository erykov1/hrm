package erykmarnik.hrm.user.domain;

import erykmarnik.hrm.user.dto.UserDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsServiceImplementation implements UserDetailsService {
  UserRepository userRepository;

  @Autowired
  public UserDetailsServiceImplementation(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDto user = userRepository.findByUsername(username).map(User::dto)
            .orElseThrow(() -> new UsernameNotFoundException("Can not find user with such username: " + username));
    return CustomUserDetailsService.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .password("{noop}" + user.getPassword())
            .userRole(UserRole.valueOf(user.getUserRole().name()))
            .build();
  }
}
