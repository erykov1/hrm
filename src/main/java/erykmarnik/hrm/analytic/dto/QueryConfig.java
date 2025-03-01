package erykmarnik.hrm.analytic.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QueryConfig {
  Integer limit;
  Sort.Order order;
  Integer offset;

  public static QueryConfig from(RequestParams params) {
    return new QueryConfig(params.getLimit(), params.getOrder(), params.getPageNumber() * params.getLimit());
  }
}
