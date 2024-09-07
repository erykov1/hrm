package erykmarnik.hrm.notification.domain

import erykmarnik.hrm.assignments.dto.AssignmentEvent
import erykmarnik.hrm.notification.sample.NotificationSample
import spock.lang.Unroll

class NotificationSpec extends NotificationBaseSpec implements NotificationSample {
  @Unroll
  def "Should send notification for #type"() {
    when: "sends message with message for assignation type $type"
      notificationFacade.onAssignmentEvent(new AssignmentEvent(type, USER_MAIL, TASK_NAME))
    then: "notification is send"
      1 * mailSender.send(createMessage(USER_MAIL, text as String, subject))
    where:
      type                       | text                                     | subject
      MessageType.ASSIGNED       | createAssignText(TASK_NAME)              | ASSIGN_SUBJECT
      MessageType.STATUS_CHANGED | createAssignmentStatusChanged(TASK_NAME) | STATUS_CHANGED_SUBJECT
      MessageType.REMOVED        | createRemoveAssignText(TASK_NAME)        | REMOVE_SUBJECT
  }
}
