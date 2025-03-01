package erykmarnik.hrm.analytic.domain;

import erykmarnik.hrm.analytic.dto.QueryConfig;
import erykmarnik.hrm.utils.ContextHolder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AllUserAssignmentsQueryProvider {
  private static final int DEFAULT_LIMIT = 50;

  String renderQuery(QueryConfig queryConfig) {
    return select() +
            from() +
            join() +
            where() +
            orderBy(queryConfig.getOrder()) +
            limit(queryConfig);
  }

  private String select() {
    return "SELECT " + Tables.Task.ALIAS + ".task_name, " + Tables.Assignment.ALIAS + ".assigned_at, " +
            Tables.Assignment.ALIAS + ".done_at, " + Tables.Assignment.ALIAS + ".assignment_status, " +
            Tables.Category.ALIAS + ".category_name ";
  }

  private String from() {
    return "FROM " + Tables.Task.TABLE_NAME + " " + Tables.Task.ALIAS + " ";
  }

  private String join() {
    return "JOIN " + Tables.Assignment.TABLE_NAME + " " + Tables.Assignment.ALIAS + " " +
            "ON " + Tables.Task.ALIAS + ".task_id = " + Tables.Assignment.ALIAS + ".object_id " +
            "JOIN " + Tables.Category.TABLE_NAME + " " + Tables.Category.ALIAS + " " +
            "ON " + Tables.Task.ALIAS + ".category_id = " + Tables.Category.ALIAS + ".category_id ";
  }

  private String where() {
    Long userId = ContextHolder.getUserContext().getUserId();
    return "WHERE " + Tables.Assignment.ALIAS + ".user_id = " + userId + " ";
  }

  private String orderBy(Sort.Order order) {
    return "ORDER BY " + OrderPropertyProvider.getSortProperty(order) + " " + order.getDirection() + " ";
  }

  private String limit(QueryConfig queryConfig) {
    return "LIMIT " + Optional.ofNullable(queryConfig.getLimit()).orElse(DEFAULT_LIMIT) + " OFFSET " +
            Optional.ofNullable(queryConfig.getOffset()).orElse(0) + ";";
  }
}
