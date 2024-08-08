package erykmarnik.hrm.task.sample

import erykmarnik.hrm.task.dto.CreateTaskDto
import erykmarnik.hrm.task.dto.TaskDto
import erykmarnik.hrm.task.dto.TaskStatusDto
import java.time.Instant

trait TaskSample {
  static final long TASK_ID = 1112L
  static final long USER_ID = 111L
  static final long TASK_USER_CREATOR = 11L
  static final String TASK_NAME = "Get to know with your boss"
  static final String ONBOARDING_TASK = "Onboarding"
  static final String TASK_DESC = "The boss is looking forward to meet you"

  private Map<String, Object> DEFAULT_TASK_DATA = [
          taskId: TASK_ID,
          createdAt: Instant.now(),
          assignedTo: USER_ID,
          createdBy: TASK_USER_CREATOR,
          taskStatus: TaskStatusDto.DONE,
          doneAt: Instant.now(),
          taskName: TASK_NAME,
          description: TASK_DESC
  ] as Map<String, Object>

  TaskDto createTask(Map<String, Object> changes = [:]) {
    def changesWithDefaults = DEFAULT_TASK_DATA + changes
    TaskDto.builder()
            .taskId(changesWithDefaults.taskId as Long)
            .createdAt(changesWithDefaults.createdAt as Instant)
            .assignedTo(changesWithDefaults.assignedTo as Long)
            .createdBy(changesWithDefaults.createdBy as Long)
            .taskStatus(changesWithDefaults.taskStatus as TaskStatusDto)
            .doneAt(changesWithDefaults.doneAt as Instant)
            .taskName(changesWithDefaults.taskName as String)
            .description(changesWithDefaults.description as String)
            .build()
  }

  CreateTaskDto createNewTask(Map<String, Object> changes = [:]) {
    def changesWithDefaults = DEFAULT_TASK_DATA + changes
    CreateTaskDto.builder()
            .createdAt(changesWithDefaults.createdAt as Instant)
            .assignedTo(changesWithDefaults.assignedTo as Long)
            .taskName(changesWithDefaults.taskName as String)
            .description(changesWithDefaults.description as String)
            .build()
  }

  void equalsTask(TaskDto result, TaskDto expected) {
    assert result.getTaskId() == expected.getTaskId()
    assert result.getCreatedAt() == expected.getCreatedAt()
    assert result.getAssignedTo() == expected.getAssignedTo()
    assert result.getCreatedBy() == expected.getCreatedBy()
    assert result.getTaskStatus() == expected.getTaskStatus()
    assert result.getDoneAt() == expected.getDoneAt()
    assert result.getTaskName() == expected.getTaskName()
    assert result.getDescription() == expected.getDescription()
  }
}