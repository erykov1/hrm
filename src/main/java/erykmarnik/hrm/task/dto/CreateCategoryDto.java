package erykmarnik.hrm.task.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateCategoryDto {
  String categoryName;

  @JsonCreator
  public CreateCategoryDto(@JsonProperty("categoryName") String categoryName) {
    this.categoryName = categoryName;
  }
}
