package erykmarnik.hrm.task.domain;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class InMemoryTaskRepository implements TaskRepository {
  Map<Long, Task> table = new HashMap<>();

  @Override
  public Optional<Task> findTaskByTaskId(Long taskId) {
    return table.values().stream()
            .filter(task -> task.dto().getTaskId().equals(taskId))
            .findFirst();
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends Task> S saveAndFlush(S entity) {
    return null;
  }

  @Override
  public <S extends Task> List<S> saveAllAndFlush(Iterable<S> entities) {
    return null;
  }

  @Override
  public void deleteAllInBatch(Iterable<Task> entities) {

  }

  @Override
  public void deleteAllByIdInBatch(Iterable<Long> longs) {

  }

  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public Task getOne(Long aLong) {
    return null;
  }

  @Override
  public Task getById(Long aLong) {
    return null;
  }

  @Override
  public Task getReferenceById(Long aLong) {
    return null;
  }

  @Override
  public <S extends Task> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends Task> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends Task> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public <S extends Task> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends Task> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends Task> boolean exists(Example<S> example) {
    return false;
  }

  @Override
  public <S extends Task, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
    return null;
  }

  @Override
  public Task save(Task entity) {
    if (entity.dto().getTaskId() == null) {
      Long taskId = new Random().nextLong();
      entity.setTaskId(taskId);
    }
    table.put(entity.dto().getTaskId(), entity);
    return entity;
  }

  @Override
  public <S extends Task> List<S> saveAll(Iterable<S> entities) {
    return null;
  }

  @Override
  public Optional<Task> findById(Long aLong) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(Long aLong) {
    return false;
  }

  @Override
  public List<Task> findAll() {
    return null;
  }

  @Override
  public List<Task> findAllById(Iterable<Long> longs) {
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
  public void delete(Task entity) {

  }

  @Override
  public void deleteAllById(Iterable<? extends Long> longs) {

  }

  @Override
  public void deleteAll(Iterable<? extends Task> entities) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public List<Task> findAll(Sort sort) {
    return null;
  }

  @Override
  public Page<Task> findAll(Pageable pageable) {
    return null;
  }
}
