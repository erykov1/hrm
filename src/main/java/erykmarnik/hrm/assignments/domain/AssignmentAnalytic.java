package erykmarnik.hrm.assignments.domain;

import erykmarnik.hrm.assignments.dto.AssignmentAnalyticDto;
import erykmarnik.hrm.task.domain.TaskFacade;
import erykmarnik.hrm.user.domain.UserFacade;
import erykmarnik.hrm.user.dto.UserDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AssignmentAnalytic {
  AssignmentRepository assignmentRepository;
  UserFacade userFacade;
  TaskFacade taskFacade;
  private static final Long EMPTY_MINUTES = 0L;

  List<AssignmentAnalyticDto> getAllNotStartedAssignments() {
    List<Assignment> assignments = assignmentRepository.findAllNotStarted();
    return assignments.stream().map(assignment -> {
      UserDto user = userFacade.getByUserId(assignment.dto().getUserId());
      String taskName = taskFacade.findByTaskId(assignment.dto().getObjectId()).getTaskName();
      return AssignmentAnalyticDto.builder()
              .userId(user.getUserId())
              .username(user.getUsername())
              .name(user.getName())
              .surname(user.getSurname())
              .objectName(taskName)
              .minutesTakenToDone(EMPTY_MINUTES)
              .startedAt(Date.from(assignment.dto().getAssignedAt()))
              .endedAt(dateFrom(assignment.dto().getDoneAt()))
              .assignmentStatus(assignment.dto().getAssignmentStatus())
              .build();
    }).toList();
  }

  List<AssignmentAnalyticDto> getAllDoneAssignments() {
    List<Assignment> assignments = assignmentRepository.findAllDone();
    return assignments.stream().map(assignment -> {
      UserDto user = userFacade.getByUserId(assignment.dto().getUserId());
      String taskName = taskFacade.findByTaskId(assignment.dto().getObjectId()).getTaskName();
      return AssignmentAnalyticDto.builder()
              .userId(user.getUserId())
              .username(user.getUsername())
              .name(user.getName())
              .surname(user.getSurname())
              .objectName(taskName)
              .minutesTakenToDone(getMinutesTakenToDoneTask(assignment))
              .startedAt(Date.from(assignment.dto().getAssignedAt()))
              .endedAt(Date.from(assignment.dto().getDoneAt()))
              .assignmentStatus(assignment.dto().getAssignmentStatus())
              .build();
    }).toList();
  }

  List<AssignmentAnalyticDto> getAllUserAssignments(Long userId) {
    List<Assignment> assignments = assignmentRepository.findUserAssignments(userId);
    UserDto user = userFacade.getByUserId(userId);
    return assignments.stream().map(assignment -> {
      String taskName = taskFacade.findByTaskId(assignment.dto().getObjectId()).getTaskName();
      return AssignmentAnalyticDto.builder()
              .userId(user.getUserId())
              .username(user.getUsername())
              .name(user.getName())
              .surname(user.getSurname())
              .objectName(taskName)
              .minutesTakenToDone(getMinutesTakenToDoneTask(assignment))
              .startedAt(Date.from(assignment.dto().getAssignedAt()))
              .endedAt(dateFrom(assignment.dto().getDoneAt()))
              .assignmentStatus(assignment.dto().getAssignmentStatus())
              .build();
    }).toList();
  }

  String getUserMail(Long userId) {
    return userFacade.getByUserId(userId).getEmail();
  }

  String getTaskName(UUID taskId) {
    return taskFacade.findByTaskId(taskId).getTaskName();
  }

  private Long getMinutesTakenToDoneTask(Assignment assignment) {
    if (assignment.dto().getDoneAt() != null) {
      return Duration.between(assignment.dto().getAssignedAt(), assignment.dto().getDoneAt()).toMinutes();
    } else {
      return EMPTY_MINUTES;
    }
  }

  private Date dateFrom(Instant instant) {
    if (instant == null) {
      return null;
    } else {
      return Date.from(instant);
    }
  }
}
