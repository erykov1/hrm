package erykmarnik.hrm.user.domain;

import erykmarnik.hrm.user.dto.ModifyUserDto;
import erykmarnik.hrm.user.dto.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class User {
  Long userId;
  String username;
  String name;
  String surname;
  String email;
  String password;
  UserRole userRole;

  UserDto dto() {
    return UserDto.builder()
            .userId(userId)
            .username(username)
            .name(name)
            .surname(surname)
            .email(email)
            .password(password)
            .userRole(userRole.dto())
            .build();
  }

  User modifyData(ModifyUserDto modifyData) {
    if (modifyData.getEmail() != null) {
      EmailValidator.validateUserEmailData(modifyData.getEmail());
    }
    return User.builder()
            .userId(userId)
            .username(modifyData.getUsername() == null ? username : modifyData.getUsername())
            .name(modifyData.getName() == null ? name : modifyData.getName())
            .surname(modifyData.getSurname() == null ? surname : modifyData.getSurname())
            .email(modifyData.getEmail() == null ? email : modifyData.getEmail())
            .password(modifyData.getPassword() == null ? password : modifyData.getPassword())
            .userRole(userRole)
            .build();
  }

  void setUserId(Long userId) {
    this.userId = userId;
  }
}
