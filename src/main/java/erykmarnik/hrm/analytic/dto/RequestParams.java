package erykmarnik.hrm.analytic.dto;

import erykmarnik.hrm.assignments.dto.AssignmentStatusDto;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestParams implements Serializable {
  @Parameter(description = "limit")
  Integer limit;
  @Parameter(description = "order")
  Sort.Order order;
  @Parameter(description = "pageNumber")
  Integer pageNumber;

  @Builder
  RequestParams(Integer limit, Sort.Order order, Integer pageNumber) {
    this.limit = limit;
    this.order = order;
    this.pageNumber = pageNumber;
  }
}
