package erykmarnik.hrm.user.sample

import erykmarnik.hrm.user.dto.CreateUserDto
import erykmarnik.hrm.user.dto.ModifyUserDto
import erykmarnik.hrm.user.dto.UserDto
import erykmarnik.hrm.user.dto.UserRoleDto

trait UserSample {
  private Long userId = 1L
  private String username = "john123"
  private String name = "John"
  private String surname = "Doe"
  private String email = "johndoe@mail.com"
  private String password = "#1@4%%^!"

  private Map<String, Object> DEFAULT_USER_DATA = [
          userId: userId,
          username: username,
          name: name,
          surname: surname,
          email: email,
          password: password,
          userRole: UserRoleDto.EMPLOYEE
  ] as Map<String, Object>

  UserDto createUser(Map<String, Object> changes = [:]) {
    def changesWithDefaults = DEFAULT_USER_DATA + changes
    UserDto.builder()
            .userId(changesWithDefaults.userId as Long)
            .username(changesWithDefaults.username as String)
            .name(changesWithDefaults.name as String)
            .surname(changesWithDefaults.surname as String)
            .email(changesWithDefaults.email as String)
            .password(changesWithDefaults.password as String)
            .userRole(changesWithDefaults.userRole as UserRoleDto)
            .build()
  }

  CreateUserDto createNewUser(Map<String, Object> changes = [:]) {
    def changesWithDefaults = DEFAULT_USER_DATA + changes
    CreateUserDto.builder()
            .username(changesWithDefaults.username as String)
            .name(changesWithDefaults.name as String)
            .surname(changesWithDefaults.surname as String)
            .email(changesWithDefaults.email as String)
            .password(changesWithDefaults.password as String)
            .build()
  }

  ModifyUserDto modifyUser(Map<String, Object> changes = [:]) {
    def changesWithDefaults = DEFAULT_USER_DATA + changes
    ModifyUserDto.builder()
            .username(changesWithDefaults.username as String)
            .name(changesWithDefaults.name as String)
            .surname(changesWithDefaults.surname as String)
            .email(changesWithDefaults.email as String)
            .password(changesWithDefaults.password as String)
            .build()
  }

  void equalsUser(UserDto result, UserDto expected) {
    assert result.userId == expected.userId
    assert result.username == expected.username
    assert result.name == expected.name
    assert result.surname == expected.surname
    assert result.email == expected.email
    assert result.userRole == expected.userRole
  }

  void equalsUsers(List<UserDto> result, List<UserDto> expected) {
    def comparator = Comparator.comparing(UserDto::getUserId)
    result.sort(comparator)
    expected.sort(comparator)
    assert result.userId == expected.userId
    assert result.username == expected.username
    assert result.name == expected.name
    assert result.surname == expected.surname
    assert result.email == expected.email
    assert result.userRole == expected.userRole
  }
}