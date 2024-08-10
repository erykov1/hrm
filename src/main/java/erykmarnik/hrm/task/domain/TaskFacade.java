package erykmarnik.hrm.task.domain;

import erykmarnik.hrm.security.SecurityFacade;
import erykmarnik.hrm.task.dto.CreateTaskDto;
import erykmarnik.hrm.task.dto.ModifyTaskDto;
import erykmarnik.hrm.task.dto.TaskDto;
import erykmarnik.hrm.task.exception.ForbiddenTaskOperationException;
import erykmarnik.hrm.task.exception.TaskNotFoundException;
import erykmarnik.hrm.utils.ContextHolder;
import erykmarnik.hrm.utils.InstantProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskFacade {
  TaskRepository taskRepository;
  TaskCreator taskCreator;
  SecurityFacade securityFacade;
  InstantProvider instantProvider;

  public TaskDto createTask(CreateTaskDto createTask) {
    log.info("creating task");
    return taskRepository.save(taskCreator.createTask(createTask)).dto();
  }

  public TaskDto modifyTask(Long taskId, ModifyTaskDto modifyTask) {
    log.info("modifying task: " + taskId);
    validateUserPrivileges(taskId, ContextHolder.getUserContext().getUserId());
    Task task = taskRepository.findTaskByTaskId(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
    return taskRepository.save(task.modifyTask(modifyTask)).dto();
  }

  public TaskDto findByTaskId(Long taskId) {
    log.info("finding task: " + taskId);
    return taskRepository.findTaskByTaskId(taskId).orElseThrow(() -> new TaskNotFoundException(taskId)).dto();
  }

  public void deleteTask(Long taskId) {
    log.info("deleting task: " + taskId);
    taskRepository.deleteById(taskId);
  }

  public List<TaskDto> getAll() {
    return taskRepository.findAll().stream().map(Task::dto).collect(Collectors.toList());
  }

  private void validateUserPrivileges(Long taskId, Long userId) {
    if (!isAbleToModifyTask(taskId, userId)) {
      throw new ForbiddenTaskOperationException(taskId);
    }
  }

  private boolean isAbleToModifyTask(Long taskId, Long userId) {
    TaskDto task = findByTaskId(taskId);
    return task.getCreatedBy().equals(userId) || securityFacade.isAdmin(userId);
  }
}
