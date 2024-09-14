package erykmarnik.hrm.task.domain;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class InMemoryCategoryRepository implements CategoryRepository {
  Map<Long, Category> table = new ConcurrentHashMap<>();

  @Override
  public Optional<Category> findByCategoryId(Long categoryId) {
    return table.values().stream()
            .filter(category -> category.categoryDto().getCategoryId().equals(categoryId))
            .findFirst();
  }

  @Override
  public Optional<Task> findTaskById(UUID taskId) {
    return table.values().stream()
            .filter(category -> category.getTasks().stream().anyMatch(note -> note.dto().getTaskId().equals(taskId)))
            .map(category -> category.getTask(taskId))
            .findFirst();
  }

  @Override
  public List<Task> findAllTasks() {
    List<Task> tasks = new ArrayList<>();
    for (Category category: table.values()) {
      tasks.addAll(category.getTasks());
    }
    return tasks;
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends Category> S saveAndFlush(S entity) {
    return null;
  }

  @Override
  public <S extends Category> List<S> saveAllAndFlush(Iterable<S> entities) {
    return null;
  }

  @Override
  public void deleteAllInBatch(Iterable<Category> entities) {

  }

  @Override
  public void deleteAllByIdInBatch(Iterable<Long> longs) {

  }

  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public Category getOne(Long aLong) {
    return null;
  }

  @Override
  public Category getById(Long aLong) {
    return null;
  }

  @Override
  public Category getReferenceById(Long aLong) {
    return null;
  }

  @Override
  public <S extends Category> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends Category> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends Category> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public <S extends Category> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends Category> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends Category> boolean exists(Example<S> example) {
    return false;
  }

  @Override
  public <S extends Category, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
    return null;
  }

  @Override
  public Category save(Category entity) {
    if (entity.categoryDto().getCategoryId() == null) {
      entity.setCategoryId(new Random().nextLong());
    }
    table.put(entity.categoryDto().getCategoryId(), entity);
    return entity;
  }

  @Override
  public <S extends Category> List<S> saveAll(Iterable<S> entities) {
    return null;
  }

  @Override
  public Optional<Category> findById(Long aLong) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(Long aLong) {
    return false;
  }

  @Override
  public List<Category> findAll() {
    return new ArrayList<>(table.values());
  }

  @Override
  public List<Category> findAllById(Iterable<Long> longs) {
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
  public void delete(Category entity) {

  }

  @Override
  public void deleteAllById(Iterable<? extends Long> longs) {

  }

  @Override
  public void deleteAll(Iterable<? extends Category> entities) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public List<Category> findAll(Sort sort) {
    return null;
  }

  @Override
  public Page<Category> findAll(Pageable pageable) {
    return null;
  }
}
