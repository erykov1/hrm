package erykmarnik.hrm.notification.sample


trait NotificationSample {
  final static String ASSIGNMENT_ASSIGN = "you have been assigned to a new task %s"
  final static String ASSIGNMENT_REMOVE = "you have been removed from task %s"
  final static String ASSIGNMENT_STATUS_CHANGED = "the status of the assigned task %s has changed"
  final static String ASSIGN_SUBJECT = "New assignation"
  final static String REMOVE_SUBJECT = "Removed assignation"
  final static String STATUS_CHANGED_SUBJECT = "Assignation status changed"

  String createAssignText(String taskName) {
    return String.format(ASSIGNMENT_ASSIGN, taskName)
  }

  String createRemoveAssignText(String taskName) {
    return String.format(ASSIGNMENT_REMOVE, taskName)
  }

  String createAssignmentStatusChanged(String taskName) {
    return String.format(ASSIGNMENT_STATUS_CHANGED, taskName)
  }
}