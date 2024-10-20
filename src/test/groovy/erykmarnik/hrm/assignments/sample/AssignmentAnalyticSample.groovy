package erykmarnik.hrm.assignments.sample

import erykmarnik.hrm.assignments.dto.AssignmentAnalyticDto
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto
import erykmarnik.hrm.user.dto.UserDto

import java.time.Instant

trait AssignmentAnalyticSample {
  private static final USER_ID = 9999L
  private static final String USERNAME = "janedoe123"
  private static final String NAME = "Jane"
  private static final String SURNAME = "Doe"
  private static final String OBJECT_NAME = "onboarding"
  private static final Long MINUTES_TAKEN_TO_DONE = 1L
  static final Long EMPTY_MINUTES = 0L
  static final Long MINUTES_FOR_WEEK_LATER = 10080L
  static final String CATEGORY_NAME = "new employee"

  private Map<String, Object> DEFAULT_ASSIGNMENT_ANALYTIC_DATA = [
          userId: USER_ID,
          username: USERNAME,
          name: NAME,
          surname: SURNAME,
          objectName: OBJECT_NAME,
          minutesTakenToDoneTask: MINUTES_TAKEN_TO_DONE,
          startedAt: Date.from(Instant.now()),
          endedAt: Date.from(Instant.now()),
          assignmentStatus: AssignmentStatusDto.DONE,
          category: CATEGORY_NAME
  ] as Map<String, Object>

  AssignmentAnalyticDto createAssignmentAnalytic(Map<String, Object> changes = [:]) {
    def changesWithDefaults = DEFAULT_ASSIGNMENT_ANALYTIC_DATA + changes
    AssignmentAnalyticDto.builder()
            .userId(changesWithDefaults.userId as Long)
            .username(changesWithDefaults.username as String)
            .name(changesWithDefaults.name as String)
            .surname(changesWithDefaults.surname as String)
            .objectName(changesWithDefaults.objectName as String)
            .minutesTakenToDone(changesWithDefaults.minutesTakenToDone as Long)
            .startedAt(changesWithDefaults.startedAt as Date)
            .endedAt(changesWithDefaults.endedAt as Date)
            .assignmentStatus(changesWithDefaults.assignmentStatus as AssignmentStatusDto)
            .category(changesWithDefaults.category as String)
            .build()
  }

  void equalsAssignments(List<AssignmentAnalyticDto> result, List<AssignmentAnalyticDto> expected) {
    def comparator = Comparator.comparing(AssignmentAnalyticDto::getStartedAt)
    result.sort(comparator)
    expected.sort(comparator)
    assert result.userId == expected.userId
    assert result.username == expected.username
    assert result.name == expected.name
    assert result.surname == expected.surname
    assert result.objectName == expected.objectName
    assert result.minutesTakenToDone == expected.minutesTakenToDone
    assert result.startedAt == expected.startedAt
    assert result.endedAt == expected.endedAt
    assert result.assignmentStatus == expected.assignmentStatus
    assert result.category == expected.category
  }
}