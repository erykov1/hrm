package erykmarnik.hrm.task.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@EqualsAndHashCode
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryDto {
  Long categoryId;
  String categoryName;
  Long createdBy;
  Instant createdAt;
}
