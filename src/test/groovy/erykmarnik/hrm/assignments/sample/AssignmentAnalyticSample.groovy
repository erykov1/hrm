package erykmarnik.hrm.assignments.sample

import erykmarnik.hrm.analytic.dto.AssignmentInfoDto
import erykmarnik.hrm.analytic.dto.UserAssignmentDto
import erykmarnik.hrm.assignments.dto.AssignmentAnalyticDto
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto
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
          assignedAt: Date.from(Instant.now()),
          endedAt: Date.from(Instant.now()),
          assignmentStatus: AssignmentStatusDto.DONE,
          category: CATEGORY_NAME
  ] as Map<String, Object>

  AssignmentInfoDto createAssignmentAnalytic(Map<String, Object> changes = [:]) {
    def changesWithDefaults = DEFAULT_ASSIGNMENT_ANALYTIC_DATA + changes
    AssignmentInfoDto.builder()
            .username(changesWithDefaults.username as String)
            .name(changesWithDefaults.name as String)
            .surname(changesWithDefaults.surname as String)
            .objectName(changesWithDefaults.objectName as String)
            .assignedAt(changesWithDefaults.assignedAt as Date)
            .endedAt(changesWithDefaults.endedAt as Date)
            .assignmentStatus(changesWithDefaults.assignmentStatus as AssignmentStatusDto)
            .category(changesWithDefaults.category as String)
            .build()
  }

  UserAssignmentDto createUserAssignment(Map<String, Object> changes = [:]) {
    def changesWithDefaults = DEFAULT_ASSIGNMENT_ANALYTIC_DATA + changes
    UserAssignmentDto.builder()
            .objectName(changesWithDefaults.objectName as String)
            .assignedAt(changesWithDefaults.assignedAt as Date)
            .endedAt(changesWithDefaults.endedAt as Date)
            .assignmentStatus(changesWithDefaults.assignmentStatus as AssignmentStatusDto)
            .category(changesWithDefaults.category as String)
            .build()
  }
}