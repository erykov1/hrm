package erykmarnik.hrm.task.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewCategoryNameDto {
  String newCategoryName;

  @JsonCreator
  public NewCategoryNameDto(@JsonProperty("newCategoryName") String newCategoryName) {
    this.newCategoryName = newCategoryName;
  }
}
