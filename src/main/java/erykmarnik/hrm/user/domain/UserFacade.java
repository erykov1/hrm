package erykmarnik.hrm.user.domain;

import erykmarnik.hrm.user.dto.CreateUserDto;
import erykmarnik.hrm.user.dto.ModifyUserDto;
import erykmarnik.hrm.user.dto.UserDto;
import erykmarnik.hrm.user.exception.AlreadyTakenException;
import erykmarnik.hrm.user.exception.UserNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserFacade {
  UserRepository userRepository;
  UserCreator userCreator;

  public UserDto createEmployee(CreateUserDto employee) {
    log.info("creating employee");
    validateUsername(employee.getUsername());
    validateUserEmail(employee.getEmail());
    return userRepository.save(userCreator.createEmployee(employee)).dto();
  }

  public UserDto createAdmin(CreateUserDto admin) {
    log.info("creating admin");
    validateUsername(admin.getUsername());
    validateUserEmail(admin.getEmail());
    return userRepository.save(userCreator.createAdmin(admin)).dto();
  }

  public void deleteUserById(Long userId) {
    log.info("deleting user: " + userId);
    userRepository.deleteById(userId);
  }

  public List<UserDto> getAllUsers() {
    log.info("getting all users");
    return userRepository.findAll().stream()
            .map(User::dto)
            .collect(Collectors.toList());
  }

  public UserDto changeUserData(ModifyUserDto change, Long userId) {
    log.info("modifying user: " + userId);
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
    return userRepository.save(user.modifyData(change)).dto();
  }

  private void validateUserEmail(String email) {
    EmailValidator.validateUserEmailData(email);
    userRepository.findUserByEmail(email).ifPresent(user -> {
      throw new AlreadyTakenException(email);
    });
  }

  private void validateUsername(String username) {
    userRepository.findUserByUsername(username).ifPresent(user -> {
      throw new AlreadyTakenException(username);
    });
  }
}
