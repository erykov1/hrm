package erykmarnik.hrm.analytic.domain

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import erykmarnik.hrm.analytic.dto.AssignmentInfoDto
import erykmarnik.hrm.analytic.dto.RequestParams
import erykmarnik.hrm.analytic.dto.UserAssignmentDto
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto
import erykmarnik.hrm.integration.HrmApi
import erykmarnik.hrm.integration.UserRequest
import erykmarnik.hrm.user.dto.UserContext
import erykmarnik.hrm.utils.ContextHolder
import org.apache.tomcat.util.json.JSONParser
import org.json.JSONObject
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

class AnalyticApiFacade extends HrmApi {
  private final MockMvc mvc
  private final ObjectMapper mapper

  AnalyticApiFacade(MockMvc mvc, ObjectMapper mapper) {
    this.mvc = mvc
    this.mapper = mapper
  }

  Collection<UserAssignmentDto> getUserAssignments(RequestParams params, UserRequest userRequest) {
    ContextHolder.setUserContext(new UserContext(userRequest.getUserId(), userRequest.getUserRole()))
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/api/analytic/user/assignments")
            .param("direction", params.order.direction.name())
            .param("parameter", params.order.property)
            .param("limit", params.limit.toString())
            .param("pageNumber", params.pageNumber.toString())
            .contentType(MediaType.APPLICATION_JSON)
    )
    checkResponse(perform.andReturn().response)
    parsePage(perform, new TypeReference<Collection<UserAssignmentDto>>() {})
  }

  Collection<AssignmentInfoDto> getAssignmentsWithStatus(RequestParams params, AssignmentStatusDto status, UserRequest userRequest) {
    ContextHolder.setUserContext(new UserContext(userRequest.getUserId(), userRequest.getUserRole()))
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/api/analytic/assignments/{status}", status)
            .param("direction", params.order.direction.name())
            .param("parameter", params.order.property)
            .param("limit", params.limit.toString())
            .param("pageNumber", params.pageNumber.toString())
            .contentType(MediaType.APPLICATION_JSON)
    )
    checkResponse(perform.andReturn().response)
    parsePage(perform, new TypeReference<Collection<AssignmentInfoDto>>() {})
  }

  private Collection parsePage(ResultActions perform, TypeReference typeReference) {
    JSONParser parser = new JSONParser(perform.andReturn().response.getContentAsString())
    JSONObject object = parser.parse() as JSONObject
    return (Collection) mapper.readValue(object.get("content").toString(), typeReference)
  }
}
