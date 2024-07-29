package erykmarnik.hrm.user;

import erykmarnik.hrm.user.domain.UserFacade;
import erykmarnik.hrm.user.dto.CreateUserDto;
import erykmarnik.hrm.user.dto.ModifyUserDto;
import erykmarnik.hrm.user.dto.UserDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserController {
  UserFacade userFacade;

  @Autowired
  UserController(UserFacade userFacade) {
    this.userFacade = userFacade;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/register/employee")
  ResponseEntity<UserDto> registerUser(@RequestBody CreateUserDto createUser) {
    return ResponseEntity.ok(userFacade.createEmployee(createUser));
  }

  @PostMapping("/register/admin")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<UserDto> registerAdmin(@RequestBody CreateUserDto createUser) {
    return ResponseEntity.ok(userFacade.createAdmin(createUser));
  }

  @GetMapping("/all")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<List<UserDto>> getUsers() {
    return ResponseEntity.ok(userFacade.getAllUsers());
  }

  @PutMapping("/modify/{userId}")
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  ResponseEntity<UserDto> modifyUser(@RequestBody ModifyUserDto modifyUserDto, @PathVariable Long userId) {
    return ResponseEntity.ok(userFacade.changeUserData(modifyUserDto, userId));
  }

  @DeleteMapping("/delete/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
    userFacade.deleteUserById(userId);
    return ResponseEntity.ok().build();
  }
}
