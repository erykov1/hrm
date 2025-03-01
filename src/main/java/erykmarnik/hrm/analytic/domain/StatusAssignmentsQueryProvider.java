package erykmarnik.hrm.analytic.domain;

import erykmarnik.hrm.analytic.dto.QueryConfig;
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class StatusAssignmentsQueryProvider {
  private static final Integer DEFAULT_LIMIT = 25;

  String renderQuery(QueryConfig queryConfig, AssignmentStatusDto status) {
    return select() +
            from() +
            join() +
            where(status) +
            order(queryConfig.getOrder()) +
            limit(queryConfig);
  }

  private String select() {
    return "SELECT " + Tables.User.ALIAS + ".username, " + Tables.User.ALIAS + ".name, " + Tables.User.ALIAS + ".surname, " +
            Tables.Task.ALIAS + ".task_name, " + Tables.Assignment.ALIAS + ".assigned_at, " + Tables.Assignment.ALIAS + ".done_at, " +
            Tables.Assignment.ALIAS + ".assignment_status, " + Tables.Category.ALIAS + ".category_name ";
  }

  private String from() {
    return "FROM " + Tables.User.TABLE_NAME + " " + Tables.User.ALIAS + " ";
  }

  private String join() {
    return "JOIN " + Tables.Assignment.TABLE_NAME + " " + Tables.Assignment.ALIAS + " ON " +
            Tables.Assignment.ALIAS + ".user_id = " + Tables.User.ALIAS + ".user_id " +
            "JOIN " + Tables.Task.TABLE_NAME + " " + Tables.Task.ALIAS + " ON " +
            Tables.Task.ALIAS + ".task_id = " + Tables.Assignment.ALIAS + ".object_id " +
            "JOIN " + Tables.Category.TABLE_NAME + " " + Tables.Category.ALIAS + " ON " +
            Tables.Category.ALIAS + ".category_id = " + Tables.Task.ALIAS + ".category_id ";
  }

  private String where(AssignmentStatusDto status) {
    return "WHERE " + Tables.Assignment.ALIAS + ".assignment_status = '" + status + "' ";
  }

  private String order(Sort.Order order) {
    return "ORDER BY " + OrderPropertyProvider.getSortProperty(order) + " " + order.getDirection() + " ";
  }

  private String limit(QueryConfig queryConfig) {
    return "LIMIT " + Optional.ofNullable(queryConfig.getLimit()).orElse(DEFAULT_LIMIT) + " OFFSET " +
            Optional.ofNullable(queryConfig.getOffset()).orElse(0) + ";";
  }
}
