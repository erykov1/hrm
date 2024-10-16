package erykmarnik.hrm.task.domain

import com.fasterxml.jackson.databind.ObjectMapper
import erykmarnik.hrm.integration.HrmApi
import erykmarnik.hrm.task.dto.CategoryDto
import erykmarnik.hrm.task.dto.CreateCategoryDto
import erykmarnik.hrm.task.dto.NewCategoryNameDto
import erykmarnik.hrm.task.dto.TaskDto
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.nio.charset.StandardCharsets

class CategoryApiFacade extends HrmApi {
  private final MockMvc mvc
  private final ObjectMapper mapper
  private final static String CATEGORY_API_PATH = "/api/category"

  CategoryApiFacade(MockMvc mvc, ObjectMapper mapper) {
    this.mvc = mvc
    this.mapper = mapper
  }

  CategoryDto createCategory(CreateCategoryDto createCategory) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.post(CATEGORY_API_PATH + "/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(createCategory))
    )
    checkResponse(perform.andReturn().response)
    CategoryDto value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructType(CategoryDto.class))
    value
  }

  CategoryDto modifyCategory(Long categoryId, NewCategoryNameDto newCategoryName) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.put(CATEGORY_API_PATH + "/{categoryId}/modify", categoryId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(newCategoryName))
    )
    checkResponse(perform.andReturn().response)
    CategoryDto value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructType(CategoryDto.class))
    value
  }

  void deleteCategory(Long categoryId) {
    if (categoryId != 0) {
      ResultActions perform = mvc.perform(MockMvcRequestBuilders.delete(CATEGORY_API_PATH + "/{categoryId}/delete", categoryId))
      checkResponse(perform.andReturn().response)
    }
  }

  CategoryDto getCategory(Long categoryId) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(CATEGORY_API_PATH + "/{categoryId}", categoryId)
            .contentType(MediaType.APPLICATION_JSON)
    )
    checkResponse(perform.andReturn().response)
    CategoryDto value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructType(CategoryDto.class))
    value
  }

  List<TaskDto> getTasksForCategory(Long categoryId) {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(CATEGORY_API_PATH + "/{categoryId}/tasks", categoryId).contentType(MediaType.APPLICATION_JSON))
    List<TaskDto> value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8),
            mapper.getTypeFactory().constructCollectionType(List.class, TaskDto.class))
    value
  }

  List<CategoryDto> getAllCategories() {
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(CATEGORY_API_PATH + "/all").contentType(MediaType.APPLICATION_JSON))
    List<CategoryDto> value = mapper.readValue(perform.andReturn().response.getContentAsString(StandardCharsets.UTF_8),
            mapper.getTypeFactory().constructCollectionType(List.class, CategoryDto.class))
    value
  }
}
