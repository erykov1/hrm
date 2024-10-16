package erykmarnik.hrm.task.domain

import com.fasterxml.jackson.databind.ObjectMapper
import erykmarnik.hrm.integration.HrmApi
import erykmarnik.hrm.task.dto.CreateTaskDto
import erykmarnik.hrm.task.dto.ModifyTaskDto
import erykmarnik.hrm.task.dto.TaskDto
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

import java.nio.charset.StandardCharsets

class TaskApiFacade extends HrmApi {
  private final MockMvc mvc
  private final ObjectMapper mapper

  TaskApiFacade(MockMvc mvc, ObjectMapper mapper) {
    this.mvc = mvc
    this.mapper = mapper
  }

  TaskDto createTask(CreateTaskDto createTask) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/api/task/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(createTask))
    )
    checkResponse(perform.andReturn().response)
    TaskDto value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructType(TaskDto.class))
    value
  }

  TaskDto modifyTask(UUID taskId, ModifyTaskDto modifyTask) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.put("/api/task/{taskId}/modify", taskId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(modifyTask))
    )
    checkResponse(perform.andReturn().response)
    TaskDto value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructType(TaskDto.class))
    value
  }

  void deleteTask(UUID taskId) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.delete("/api/task/{taskId}/delete", taskId))
    checkResponse(perform.andReturn().response)
  }

  List<TaskDto> getAll() {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/api/task/all").contentType(MediaType.APPLICATION_JSON))
    List<TaskDto> value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8),
            mapper.getTypeFactory().constructCollectionType(List.class, TaskDto.class))
    value
  }

  TaskDto getTaskById(UUID taskId) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/api/task/{taskId}", taskId)
            .contentType(MediaType.APPLICATION_JSON)
    )
    checkResponse(perform.andReturn().response)
    TaskDto value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructType(TaskDto.class))
    value
  }
}
