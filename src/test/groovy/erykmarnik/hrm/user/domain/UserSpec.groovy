package erykmarnik.hrm.user.domain

import erykmarnik.hrm.user.dto.UserDto
import erykmarnik.hrm.user.dto.UserRoleDto
import erykmarnik.hrm.user.exception.AlreadyTakenException
import erykmarnik.hrm.user.exception.InvalidEmailException
import erykmarnik.hrm.user.exception.UserNotFoundException
import erykmarnik.hrm.user.sample.UserSample
import spock.lang.Specification
import spock.lang.Unroll


class UserSpec extends Specification implements UserSample {
  UserCreator userCreator = new UserCreator()
  UserFacade userFacade = new UserConfiguration().userFacade(userCreator)
  private static final NOT_EXISTING_USER_ID = 11L

  def "Should create employee if email or username is not taken"() {
    when: "creates user"
      UserDto john = userFacade.createEmployee(createNewUser())
    then: "user is created"
      equalsUser(john, createUser(userId: john.userId))
  }

  @Unroll
  def "Should not create user if email or username is taken"() {
    given: "there is user john"
      userFacade.createEmployee(createNewUser())
    when: "creates user with the same $email or $username"
      userFacade.createEmployee(createNewUser(username: username, email: email))
    then: "user is not created"
      thrown(AlreadyTakenException)
    where:
      username | email
      "john123"| "johndoe@mail.com"
      "john213"| "johndoe@mail.com"
      "john123"| "john123@mail.com"
  }

  def "Should delete user"() {
    given: "there is user john"
      long johnId = userFacade.createEmployee(createNewUser()).userId
    when: "deletes user john"
      userFacade.deleteUserById(johnId)
    then: "john is deleted"
      userFacade.getAllUsers() == []
  }

  def "Should not create user if email is invalid"() {
    when: "creates user with invalid email"
      userFacade.createEmployee(createNewUser(email: "email"))
    then: "user is not created"
      thrown(InvalidEmailException)
  }

  def "Should create admin"() {
    when: "creates admin john"
      UserDto john = userFacade.createAdmin(createNewUser(userRole: UserRoleDto.ADMIN))
    then: "admin john is created"
      equalsUser(john, createUser(userId: john.userId, userRole: UserRoleDto.ADMIN))
  }

  def "Should not modify user data that does not exist"() {
    when: "modifies user data if user does not exist"
      userFacade.changeUserData(modifyUser(username: "new username"), NOT_EXISTING_USER_ID)
    then: "user is not modified"
      thrown(UserNotFoundException)
  }

  def "Should modify user data if user exists"() {
    given: "there is user john"
      UserDto john = userFacade.createEmployee(createNewUser())
    when: "$john changes username and email"
      userFacade.changeUserData(modifyUser(username: "new username", email: "john1234@gmail.com"), john.userId)
    then: "$john username and email are changed"
      List<UserDto> result = userFacade.getAllUsers()
      equalsUser(result[0], createUser(userId: john.userId, username: "new username", email: "john1234@gmail.com"))
  }

  def "Should not modify user data if new email is invalid"() {
    given: "there is user john"
      UserDto john = userFacade.createEmployee(createNewUser())
    when: "$john changes email with invalid data"
      userFacade.changeUserData(modifyUser(email: "john1234"), john.userId)
    then: "$john data is not modified"
      thrown(InvalidEmailException)
  }
}
