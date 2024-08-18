package erykmarnik.hrm.assignments.domain;

import erykmarnik.hrm.assignments.dto.AssignmentAnalyticDto;
import erykmarnik.hrm.assignments.dto.AssignmentDto;
import erykmarnik.hrm.assignments.dto.CreateAssignmentDto;
import erykmarnik.hrm.assignments.exception.AlreadyAssignedException;
import erykmarnik.hrm.assignments.exception.AssignmentNotFoundException;
import erykmarnik.hrm.assignments.exception.ForbiddenAssignmentOperationException;
import erykmarnik.hrm.security.SecurityFacade;
import erykmarnik.hrm.utils.ContextHolder;
import erykmarnik.hrm.utils.InstantProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentFacade {
  AssignmentRepository assignmentRepository;
  AssignmentCreator assignmentCreator;
  SecurityFacade securityFacade;
  InstantProvider instantProvider;
  AssignmentAnalytic assignmentAnalytic;

  public AssignmentDto createAssignment(CreateAssignmentDto createAssignment) {
    log.info("creating assignment");
    validateAssignmentCreation(createAssignment.getUserId(), createAssignment.getObjectId());
    return assignmentRepository.save(assignmentCreator.createAssignment(createAssignment)).dto();
  }

  public void deleteAssignment(Long assignmentId) {
    log.info("deleting assignment: " + assignmentId);
    validateDeleteAssignment(ContextHolder.getUserContext().getUserId(), assignmentId);
    assignmentRepository.deleteById(assignmentId);
  }

  public void setAssignmentToDone(Long assignmentId) {
    log.info("changing status assignment" + assignmentId + "to done");
    validateAssignmentOperation(ContextHolder.getUserContext().getUserId(), assignmentId);
    Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId).orElseThrow(
            () -> new AssignmentNotFoundException(assignmentId));
    assignmentRepository.save(assignment.setToDone(instantProvider.now()));
  }

  public AssignmentDto getAssignmentById(Long assignmentId) {
    log.info("finding assignment with id " + assignmentId);
    validateAssignmentOperation(ContextHolder.getUserContext().getUserId(), assignmentId);
    return assignmentRepository.findByAssignmentId(assignmentId).orElseThrow(
            () -> new AssignmentNotFoundException(assignmentId)).dto();
  }

  public List<AssignmentDto> getUserAssignments() {
    Long userId = ContextHolder.getUserContext().getUserId();
    log.info("getting assignments for user: " + userId);
    return assignmentRepository.findUserAssignments(userId).stream()
            .map(Assignment::dto)
            .collect(Collectors.toList());
  }

  public List<AssignmentDto> getAllAssignments() {
    log.info("finding all assignments");
    return assignmentRepository.findAll().stream().map(Assignment::dto).collect(Collectors.toList());
  }

  public List<AssignmentAnalyticDto> getAllUserAssignmentsFor(Long userId) {
    log.info("getting all user assignments: " + userId);
    return assignmentAnalytic.getAllUserAssignments(userId);
  }

  public List<AssignmentAnalyticDto> getAllNotStarted() {
    log.info("getting all not started assignments");
    return assignmentAnalytic.getAllNotStartedAssignments();
  }

  public List<AssignmentAnalyticDto> getAllDone() {
    log.info("getting all done assignments");
    return assignmentAnalytic.getAllDoneAssignments();
  }

  private void validateAssignmentOperation(Long userId, Long assignmentId) {
    Assignment assignment = getAssignment(assignmentId);
    if (!securityFacade.isAdmin(userId) && !assignment.dto().getAssignmentCreatedBy().equals(userId)
            && !assignment.dto().getUserId().equals(userId)) {
      throw new ForbiddenAssignmentOperationException(assignmentId);
    }
  }

  private void validateDeleteAssignment(Long userId, Long assignmentId) {
    Optional<Assignment> assignment = assignmentRepository.findByAssignmentId(assignmentId);
    if (assignment.isPresent()) {
      if (!securityFacade.isAdmin(userId) && !assignment.get().dto().getAssignmentCreatedBy().equals(userId)) {
        throw new ForbiddenAssignmentOperationException(assignmentId);
      }
    }
  }

  private Assignment getAssignment(Long assignmentId) {
    return assignmentRepository.findByAssignmentId(assignmentId).orElseThrow(() -> new AssignmentNotFoundException(assignmentId));
  }

  private void validateAssignmentCreation(Long userId, Long objectId) {
    if (assignmentRepository.findByObjectIdAndUserId(objectId, userId).isPresent()) {
      throw new AlreadyAssignedException(userId, objectId);
    }
  }
}
