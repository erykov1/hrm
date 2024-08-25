package erykmarnik.hrm.assignments.domain;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class InMemoryAssignmentNoteRepository implements AssignmentNoteRepository {
  Map<UUID, AssignmentNote> table = new HashMap<>();

  @Override
  public Optional<AssignmentNote> findByNoteId(UUID noteId) {
    return table.values().stream()
            .filter(note -> note.dto().getNoteId().equals(noteId))
            .findFirst();
  }

  @Override
  public List<AssignmentNote> findNotesForAssignment(Long assignmentId) {
    return table.values().stream()
            .filter(note -> note.dto().getAssignmentId().equals(assignmentId))
            .collect(Collectors.toList());
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends AssignmentNote> S saveAndFlush(S entity) {
    return null;
  }

  @Override
  public <S extends AssignmentNote> List<S> saveAllAndFlush(Iterable<S> entities) {
    return null;
  }

  @Override
  public void deleteAllInBatch(Iterable<AssignmentNote> entities) {

  }

  @Override
  public void deleteAllByIdInBatch(Iterable<UUID> uuids) {

  }

  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public AssignmentNote getOne(UUID uuid) {
    return null;
  }

  @Override
  public AssignmentNote getById(UUID uuid) {
    return null;
  }

  @Override
  public AssignmentNote getReferenceById(UUID uuid) {
    return null;
  }

  @Override
  public <S extends AssignmentNote> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends AssignmentNote> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends AssignmentNote> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public <S extends AssignmentNote> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends AssignmentNote> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends AssignmentNote> boolean exists(Example<S> example) {
    return false;
  }

  @Override
  public <S extends AssignmentNote, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
    return null;
  }

  @Override
  public AssignmentNote save(AssignmentNote entity) {
    table.put(entity.dto().getNoteId(), entity);
    return entity;
  }

  @Override
  public <S extends AssignmentNote> List<S> saveAll(Iterable<S> entities) {
    return null;
  }

  @Override
  public Optional<AssignmentNote> findById(UUID uuid) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(UUID uuid) {
    return false;
  }

  @Override
  public List<AssignmentNote> findAll() {
    return null;
  }

  @Override
  public List<AssignmentNote> findAllById(Iterable<UUID> uuids) {
    return null;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(UUID uuid) {
    table.remove(uuid);
  }

  @Override
  public void delete(AssignmentNote entity) {

  }

  @Override
  public void deleteAllById(Iterable<? extends UUID> uuids) {

  }

  @Override
  public void deleteAll(Iterable<? extends AssignmentNote> entities) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public List<AssignmentNote> findAll(Sort sort) {
    return null;
  }

  @Override
  public Page<AssignmentNote> findAll(Pageable pageable) {
    return null;
  }
}
