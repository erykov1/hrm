package erykmarnik.hrm.analytic.domain;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class Tables {
  static final String DESC = "DESC";
  static final String ASC = "ASC";

  static class User {
    static final String TABLE_NAME = "user_info";
    static final String ALIAS = "ui";
  }

  static class Task {
    static final String TABLE_NAME = "task";
    static final String ALIAS = "t";
  }

  static class Assignment {
    static final String TABLE_NAME = "assignment";
    static final String ALIAS = "a";
  }

  static class AssignmentNote {
    static final String TABLE_NAME = "assignment_note";
    static final String ALIAS = "an";
  }

  static class Category {
    static final String TABLE_NAME = "category";
    static final String ALIAS = "c";
  }
}
