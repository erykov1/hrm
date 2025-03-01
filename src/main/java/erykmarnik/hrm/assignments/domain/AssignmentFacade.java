package erykmarnik.hrm.assignments.domain;

import erykmarnik.hrm.assignments.dto.*;
import erykmarnik.hrm.assignments.exception.*;
import erykmarnik.hrm.security.SecurityFacade;
import erykmarnik.hrm.utils.ContextHolder;
import erykmarnik.hrm.utils.InstantProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
  EventPublisher eventPublisher;

  public AssignmentDto createAssignment(CreateAssignmentDto createAssignment) {
    log.info("creating assignment");
    validateAssignmentCreation(createAssignment.getUserId(), createAssignment.getObjectId());
    eventPublisher.emmitAssignationAdded(assignmentAnalytic.getUserMail(createAssignment.getUserId()),
            assignmentAnalytic.getTaskName(createAssignment.getObjectId()));
    return assignmentRepository.save(assignmentCreator.createAssignment(createAssignment)).dto();
  }

  public void deleteAssignment(Long assignmentId) {
    log.info("deleting assignment: " + assignmentId);
    validateDeleteAssignment(ContextHolder.getUserContext().getUserId(), assignmentId);
    AssignmentDto assignment = getAssignmentById(assignmentId);
    eventPublisher.emmitAssignationRemoved(assignmentAnalytic.getUserMail(assignment.getUserId()),
            assignmentAnalytic.getTaskName(assignment.getObjectId()));
    assignmentRepository.deleteById(assignmentId);
  }

  public void setAssignmentToDone(Long assignmentId) {
    log.info("changing status assignment" + assignmentId + "to done");
    validateAssignmentOperation(ContextHolder.getUserContext().getUserId(), assignmentId);
    validateOverDueAssignment(assignmentId);
    Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId).orElseThrow(
            () -> new AssignmentNotFoundException(assignmentId));
    eventPublisher.emmitAssignationStatusChanged(assignmentAnalytic.getUserMail(assignment.dto().getUserId()),
            assignmentAnalytic.getTaskName(assignment.dto().getObjectId()));
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

  public AssignmentNoteDto addAssignmentNote(CreateAssignmentNoteDto note) {
    log.info("creating note assignment");
    validateAssignmentOperation(ContextHolder.getUserContext().getUserId(), note.getAssignmentId());
    Assignment assignment = getAssignment(note.getAssignmentId());
    AssignmentNoteDto assignmentNote = assignment.addAssignmentNote(note).dto();
    assignmentRepository.save(assignment);
    return assignmentNote;
  }

  public void deleteAssignmentNote(UUID noteId) {
    log.info("deleting assignment note: " + noteId);
    AssignmentNote assignmentNote = getAssignmentNote(noteId);
    validateNoteOperation(ContextHolder.getUserContext().getUserId(), noteId);
    assignmentRepository.save(getAssignment(assignmentNote.dto().getAssignmentId()).removeAssignmentNote(assignmentNote));
  }

  public List<AssignmentNoteDto> getNotesForAssignment(Long assignmentId) {
    log.info("getting notes for assignment: " + assignmentId);
    validateAssignmentOperation(ContextHolder.getUserContext().getUserId(), assignmentId);
    return getAssignment(assignmentId).getAssignmentNotes().stream()
            .map(AssignmentNote::dto)
            .collect(Collectors.toList());
  }

  public AssignmentNoteDto modifyAssignmentNote(UUID noteId, AssignmentNoteModifyDto noteModify) {
    log.info("modifying note " + noteId + " content");
    validateNoteOperation(ContextHolder.getUserContext().getUserId(), noteId);
    AssignmentNote assignmentNote = getAssignmentNote(noteId);
    Assignment assignment = getAssignment(assignmentNote.dto().getAssignmentId());
    assignmentRepository.save(assignment.modifyAssignmentNote(noteId, noteModify));
    return assignmentNote.modifyAssignmentNote(noteModify).dto();
  }

  private void validateAssignmentOperation(Long userId, Long assignmentId) {
    Assignment assignment = getAssignment(assignmentId);
    if (!securityFacade.isAdmin(userId) && !assignment.dto().getAssignmentCreatedBy().equals(userId)
            && !assignment.dto().getUserId().equals(userId)) {
      throw new ForbiddenAssignmentOperationException(assignmentId);
    }
  }

  private void validateOverDueAssignment(Long assignmentId) {
    Assignment assignment = getAssignment(assignmentId);
    if (isOverDue(assignment.dto().getDueTo())) {
      throw new OverDueAssignmentException(assignmentId);
    }
  }

  private boolean isOverDue(Instant dueTo) {
    return dueTo != null && dueTo.isBefore(instantProvider.now());
  }

  private void validateNoteOperation(Long userId, UUID noteId) {
    Long assignmentId = getAssignmentNote(noteId).dto().getAssignmentId();
    Assignment assignment = getAssignment(assignmentId);
    if (!securityFacade.isAdmin(userId) && !assignment.dto().getAssignmentCreatedBy().equals(userId)
            && !assignment.dto().getUserId().equals(userId)) {
      throw new ForbiddenAssignmentOperationException(assignmentId);
    }
  }

  private AssignmentNote getAssignmentNote(UUID noteId) {
    return assignmentRepository.findAssignmentNoteById(noteId).orElseThrow(() -> new AssignmentNoteNotFoundException(noteId));
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

  private void validateAssignmentCreation(Long userId, UUID objectId) {
    if (assignmentRepository.findByObjectIdAndUserId(objectId, userId).isPresent()) {
      throw new AlreadyAssignedException(userId, objectId);
    }
  }

  public void outDateNotStartedAssignments() {
    List<Assignment> terminatedAssignments = assignmentRepository.findAllNotStarted().stream()
            .filter(assignment -> isOverDue(assignment.dto().getDueTo()))
            .toList();
    log.info("terminating " + terminatedAssignments.size() + " assignments");
    terminatedAssignments.forEach(Assignment::outDate);
    assignmentRepository.saveAll(terminatedAssignments);
  }
}
