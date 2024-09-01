package erykmarnik.hrm.assignments.domain

import com.fasterxml.jackson.databind.ObjectMapper
import erykmarnik.hrm.assignments.dto.AssignmentNoteDto
import erykmarnik.hrm.assignments.dto.AssignmentNoteModifyDto
import erykmarnik.hrm.assignments.dto.CreateAssignmentNoteDto
import erykmarnik.hrm.integration.HrmApi
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

import java.nio.charset.StandardCharsets

class AssignmentNoteApiFacade extends HrmApi {
  private final MockMvc mvc
  private final ObjectMapper mapper

  AssignmentNoteApiFacade(MockMvc mvc, ObjectMapper mapper) {
    this.mvc = mvc
    this.mapper = mapper
  }

  AssignmentNoteDto addAssignmentNote(CreateAssignmentNoteDto note) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/api/assignment/note/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(note))
    )
    checkResponse(perform.andReturn().response)
    AssignmentNoteDto value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8),
            mapper.getTypeFactory().constructType(AssignmentNoteDto.class))
    value
  }

  void deleteAssignmentNote(UUID noteId) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.delete("/api/assignment/note/delete/{noteId}", noteId))
    checkResponse(perform.andReturn().response)
  }

  List<AssignmentNoteDto> getAssignmentNotesFor(Long assignmentId) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/api/assignment/note/{assignmentId}", assignmentId)
            .contentType(MediaType.APPLICATION_JSON))
    checkResponse(perform.andReturn().response)
    List<AssignmentNoteDto> value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8),
            mapper.getTypeFactory().constructCollectionType(List.class, AssignmentNoteDto.class))
    value
  }

  AssignmentNoteDto modifyAssignmentNote(UUID noteId, AssignmentNoteModifyDto noteModify) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.put("/api/assignment/note/modify/{noteId}", noteId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(noteModify))
    )
    checkResponse(perform.andReturn().response)
    AssignmentNoteDto value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8),
            mapper.getTypeFactory().constructType(AssignmentNoteDto.class))
    value
  }
}
