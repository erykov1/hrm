package erykmarnik.hrm.user.domain

import com.fasterxml.jackson.databind.ObjectMapper
import erykmarnik.hrm.integration.HrmApi
import erykmarnik.hrm.integration.UserRequest
import erykmarnik.hrm.user.dto.CreateUserDto
import erykmarnik.hrm.user.dto.ModifyUserDto
import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.user.dto.UserDto
import erykmarnik.hrm.utils.ContextHolder
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

import java.nio.charset.StandardCharsets

class UserApiFacade extends HrmApi {
  private final MockMvc mvc
  private final ObjectMapper mapper

  UserApiFacade(MockMvc mvc, ObjectMapper mapper) {
    this.mvc = mvc
    this.mapper = mapper
  }

  UserDto createEmployee(CreateUserDto createEmployee) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/api/user/register/employee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(createEmployee))
    )
    checkResponse(perform.andReturn().response)
    UserDto value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructType(UserDto.class))
    value
  }

  UserDto createAdmin(CreateUserDto createAdmin) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/api/user/register/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(createAdmin))
    )
    checkResponse(perform.andReturn().response)
    UserDto value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructType(UserDto.class))
    value
  }

  List<UserDto> getUsers(UserRequest userRequest) {
    ContextHolder.setUserContext(new UserContext(userRequest.getUserId(), userRequest.getUserRole()))
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/api/user/all").contentType(MediaType.APPLICATION_JSON))
    checkResponse(perform.andReturn().response)
    List<UserDto> value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8),
            mapper.getTypeFactory().constructCollectionType(List.class, UserDto.class))
    value
  }

  UserDto modifyUser(ModifyUserDto modifyUser, UserRequest userRequest) {
    ContextHolder.setUserContext(new UserContext(userRequest.getUserId(), userRequest.getUserRole()))
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.put("/api/user/modify/{userId}", userRequest.getUserId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(modifyUser))
    )
    checkResponse(perform.andReturn().response)
    UserDto value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructType(UserDto.class))
    value
  }

  void deleteUser(Long userId, UserRequest userRequest) {
    ContextHolder.setUserContext(new UserContext(userRequest.getUserId(), userRequest.getUserRole()))
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.delete("/api/user/delete/{userId}", userId))
    checkResponse(perform.andReturn().response)
  }

  UserDto getByUserId(Long userId, UserRequest userRequest) {
    ContextHolder.setUserContext(new UserContext(userRequest.getUserId(), userRequest.getUserRole()))
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/api/user/{userId}", userId).contentType(MediaType.APPLICATION_JSON))
    checkResponse(perform.andReturn().response)
    UserDto value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructType(UserDto.class))
    value
  }
}
