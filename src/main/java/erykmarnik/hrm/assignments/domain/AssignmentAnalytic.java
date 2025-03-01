package erykmarnik.hrm.assignments.domain;

import erykmarnik.hrm.task.domain.TaskFacade;
import erykmarnik.hrm.user.domain.UserFacade;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import java.util.UUID;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AssignmentAnalytic {
  AssignmentRepository assignmentRepository;
  UserFacade userFacade;
  TaskFacade taskFacade;
  private static final Long EMPTY_MINUTES = 0L;

  String getUserMail(Long userId) {
    return userFacade.getByUserId(userId).getEmail();
  }

  String getTaskName(UUID taskId) {
    return taskFacade.findByTaskId(taskId).getTaskName();
  }
}
