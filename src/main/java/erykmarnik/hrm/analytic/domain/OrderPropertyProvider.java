package erykmarnik.hrm.analytic.domain;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class OrderPropertyProvider {
  static String getSortProperty(Sort.Order order) {
    String property = order.getProperty();
    return switch (property) {
      case "username", "name", "surname", "email", "userRole" -> Tables.User.ALIAS + "." + renderSortProperty(property);
      case "taskCreatedAt", "createdBy", "taskName", "description" ->
              Tables.Task.ALIAS + "." + renderSortProperty(property);
      case "assignedAt", "doneAt", "assignmentStatus" -> Tables.Assignment.ALIAS + "." + renderSortProperty(property);
      case "note_content" -> Tables.AssignmentNote.ALIAS + "." + renderSortProperty(property);
      case "categoryName", "categoryCreatedAt" -> Tables.Category.ALIAS + "." + renderSortProperty(property);
      default -> Tables.Assignment.ALIAS + ".assignment_status";
    };
  }

  private static String renderSortProperty(String sortProperty) {
    return sortProperty.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
  }
}
