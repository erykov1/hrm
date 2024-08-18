package erykmarnik.hrm.assignments.domain

import com.fasterxml.jackson.databind.ObjectMapper
import erykmarnik.hrm.assignments.dto.AssignmentAnalyticDto
import erykmarnik.hrm.assignments.dto.AssignmentDto
import erykmarnik.hrm.assignments.dto.CreateAssignmentDto
import erykmarnik.hrm.integration.HrmApi
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

import java.nio.charset.StandardCharsets

class AssignmentApiFacade extends HrmApi {
  private final MockMvc mvc
  private final ObjectMapper mapper

  AssignmentApiFacade(MockMvc mvc, ObjectMapper mapper) {
    this.mvc = mvc
    this.mapper = mapper
  }

  AssignmentDto createAssignment(CreateAssignmentDto createAssignment) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/api/assignment/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(createAssignment))
    )
    checkResponse(perform.andReturn().response)
    AssignmentDto value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructType(AssignmentDto.class))
    value
  }

  void deleteAssignment(Long assignmentId) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.delete("/api/assignment/delete/{assignmentId}", assignmentId))
    checkResponse(perform.andReturn().response)
  }

  void setToDone(Long assignmentId) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.put("/api/assignment/done/{assignmentId}", assignmentId))
    checkResponse(perform.andReturn().response)
  }

  AssignmentDto getAssignmentById(Long assignmentId) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/api/assignment/{assignmentId}", assignmentId)
            .contentType(MediaType.APPLICATION_JSON))
    checkResponse(perform.andReturn().response)
    AssignmentDto value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructType(AssignmentDto.class))
    value
  }

  List<AssignmentDto> getAllAssignments() {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/api/assignment/all").contentType(MediaType.APPLICATION_JSON))
    checkResponse(perform.andReturn().response)
    List<AssignmentDto> value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8),
            mapper.getTypeFactory().constructCollectionType(List.class, AssignmentDto.class))
    value
  }

  List<AssignmentDto> getUserAssignments() {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/api/assignment/user").contentType(MediaType.APPLICATION_JSON))
    checkResponse(perform.andReturn().response)
    List<AssignmentDto> value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8),
            mapper.getTypeFactory().constructCollectionType(List.class, AssignmentDto.class))
    value
  }

  List<AssignmentAnalyticDto> getAllDoneAssignments() {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/api/assignment/all/done").contentType(MediaType.APPLICATION_JSON))
    checkResponse(perform.andReturn().response)
    List<AssignmentAnalyticDto> value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8),
            mapper.getTypeFactory().constructCollectionType(List.class, AssignmentAnalyticDto.class))
    value
  }

  List<AssignmentAnalyticDto> getAllNotStartedAssignments() {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/api/assignment/all/notStarted").contentType(MediaType.APPLICATION_JSON))
    checkResponse(perform.andReturn().response)
    List<AssignmentAnalyticDto> value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8),
            mapper.getTypeFactory().constructCollectionType(List.class, AssignmentAnalyticDto.class))
    value
  }

  List<AssignmentAnalyticDto> getAllForUser(Long userId) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/api/assignment/all/{userId}", userId).contentType(MediaType.APPLICATION_JSON))
    checkResponse(perform.andReturn().response)
    List<AssignmentAnalyticDto> value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8),
            mapper.getTypeFactory().constructCollectionType(List.class, AssignmentAnalyticDto.class))
    value
  }
}
