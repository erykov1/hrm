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
class InMemoryAssignmentRepository implements AssignmentRepository {
  Map<Long, Assignment> table = new HashMap<>();

  @Override
  public void flush() {

  }

  @Override
  public <S extends Assignment> S saveAndFlush(S entity) {
    return null;
  }

  @Override
  public <S extends Assignment> List<S> saveAllAndFlush(Iterable<S> entities) {
    return null;
  }

  @Override
  public void deleteAllInBatch(Iterable<Assignment> entities) {

  }

  @Override
  public void deleteAllByIdInBatch(Iterable<Long> longs) {

  }

  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public Assignment getOne(Long aLong) {
    return null;
  }

  @Override
  public Assignment getById(Long aLong) {
    return null;
  }

  @Override
  public Assignment getReferenceById(Long aLong) {
    return null;
  }

  @Override
  public <S extends Assignment> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends Assignment> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends Assignment> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public <S extends Assignment> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends Assignment> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends Assignment> boolean exists(Example<S> example) {
    return false;
  }

  @Override
  public <S extends Assignment, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
    return null;
  }

  @Override
  public Assignment save(Assignment entity) {
    if (entity.dto().getAssignmentId() == null) {
      entity.setAssignmentId(new Random().nextLong());
    }
    table.put(entity.dto().getAssignmentId(), entity);
    return entity;
  }

  @Override
  public <S extends Assignment> List<S> saveAll(Iterable<S> entities) {
    return null;
  }

  @Override
  public Optional<Assignment> findById(Long aLong) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(Long aLong) {
    return false;
  }

  @Override
  public List<Assignment> findAll() {
    return new ArrayList<>(table.values());
  }

  @Override
  public List<Assignment> findAllById(Iterable<Long> longs) {
    return null;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(Long aLong) {
    table.remove(aLong);
  }

  @Override
  public void delete(Assignment entity) {

  }

  @Override
  public void deleteAllById(Iterable<? extends Long> longs) {

  }

  @Override
  public void deleteAll(Iterable<? extends Assignment> entities) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public List<Assignment> findAll(Sort sort) {
    return null;
  }

  @Override
  public Page<Assignment> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public Optional<Assignment> findByAssignmentId(Long assignmentId) {
    return table.values().stream()
            .filter(value -> value.dto().getAssignmentId().equals(assignmentId))
            .findFirst();
  }

  @Override
  public Optional<Assignment> findByObjectIdAndUserId(Long objectId, Long userId) {
    return table.values().stream()
            .filter(value -> value.dto().getObjectId().equals(objectId) && value.dto().getUserId().equals(userId))
            .findFirst();
  }

  @Override
  public List<Assignment> findUserAssignments(Long userId) {
    return table.values().stream()
            .filter(assignment -> assignment.dto().getUserId().equals(userId))
            .collect(Collectors.toList());
  }
}
