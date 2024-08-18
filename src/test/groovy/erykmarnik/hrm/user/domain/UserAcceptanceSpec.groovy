package erykmarnik.hrm.user.domain

import erykmarnik.hrm.user.dto.UserDto
import erykmarnik.hrm.user.dto.UserRoleDto
import erykmarnik.hrm.integration.IntegrationSpec
import erykmarnik.hrm.user.sample.UserSample

class UserAcceptanceSpec extends IntegrationSpec implements UserSample {
  private UserApiFacade userApiFacade
  private UserDto user
  private UserDto mike

  def setup() {
    userApiFacade = new UserApiFacade(mockMvc, objectMapper)
  }

  def cleanup() {
    userApiFacade.deleteUser(user.userId)
    if (mike != null) {
      userApiFacade.deleteUser(mike.userId)
    }
  }

  def "Should create employee if email or username is not taken"() {
    when: "creates user as employee"
      user = userApiFacade.createEmployee(createNewUser())
    then: "employee is created"
      equalsUser(user, createUser(userId: user.userId))
  }

  def "Should create admin if email or username is not taken"(){
    when: "creates user as employee"
      user = userApiFacade.createAdmin(createNewUser())
    then: "employee is created"
      equalsUser(user, createUser(userId: user.userId, userRole: UserRoleDto.ADMIN))
  }

  def "Should modify user data"() {
    given: "there is employee"
      user = userApiFacade.createEmployee(createNewUser())
    when: "changes his username to 'john1234'"
      user = userApiFacade.modifyUser(modifyUser(username: "john1234"), user.userId)
    then: "username is changed"
      equalsUser(user, createUser(userId: user.userId, username: "john1234"))
  }

  def "Should get all users"() {
    given: "there is employee"
      user = userApiFacade.createEmployee(createNewUser())
    and: "there is another employee"
      mike = userApiFacade.createEmployee(createNewUser(username: "mike1", name: "Mike", surname:  "Smith", email: "mike@mail.com"))
    when: "asks for all users"
      List<UserDto> users = userApiFacade.getUsers()
    then: "return all users"
      equalsUsers(users, [createUser(userId: user.userId), createUser(userId: mike.userId, username: "mike1", name: "Mike",
              surname:  "Smith", email: "mike@mail.com")])
  }

  def "Should delete user"() {
    given: "there is employee"
      user = userApiFacade.createEmployee(createNewUser())
    and: "there is another employee"
      mike = userApiFacade.createEmployee(createNewUser(username: "mike1", name: "Mike", surname:  "Smith", email: "mike@mail.com"))
    when: "deletes 'mike'"
      userApiFacade.deleteUser(mike.userId)
    then: "'mike' is deleted"
      List<UserDto> users = userApiFacade.getUsers()
      equalsUsers(users, [createUser(userId: user.userId)])
  }

  def "Should get user by user id"() {
    given: "there is employee"
      user = userApiFacade.createEmployee(createNewUser())
    when: "asks for user $user"
      UserDto result = userApiFacade.getByUserId(user.userId)
    then: "gets user $user"
      result == user
  }
}
