package erykmarnik.hrm.user.domain;

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
class InMemoryUserRepository implements UserRepository {
  private Map<Long, User> table = new HashMap<>();

  @Override
  public Optional<User> findByEmail(String email) {
    return table.values().stream()
            .filter(user -> user.dto().getEmail().equals(email))
            .findFirst();
  }

  @Override
  public Optional<User> findByUserId(Long userId) {
    return table.values().stream()
            .filter(user -> user.dto().getUserId().equals(userId))
            .findFirst();
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return table.values().stream()
            .filter(user -> user.dto().getUsername().equals(username))
            .findFirst();
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends User> S saveAndFlush(S entity) {
    return null;
  }

  @Override
  public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
    return null;
  }

  @Override
  public void deleteAllInBatch(Iterable<User> entities) {

  }

  @Override
  public void deleteAllByIdInBatch(Iterable<Long> longs) {

  }

  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public User getOne(Long aLong) {
    return null;
  }

  @Override
  public User getById(Long aLong) {
    return null;
  }

  @Override
  public User getReferenceById(Long aLong) {
    return null;
  }

  @Override
  public <S extends User> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends User> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends User> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends User> boolean exists(Example<S> example) {
    return false;
  }

  @Override
  public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
    return null;
  }

  @Override
  public <S extends User> S save(S entity) {
    if (entity.dto().getUserId() == null) {
      Long userId = new Random().nextLong();
      entity.setUserId(userId);
    }
    table.put(entity.dto().getUserId(), entity);
    return entity;
  }

  @Override
  public <S extends User> List<S> saveAll(Iterable<S> entities) {
    return null;
  }

  @Override
  public Optional<User> findById(Long aLong) {
    return table.values().stream()
            .filter(user -> user.dto().getUserId().equals(aLong))
            .findFirst();
  }

  @Override
  public boolean existsById(Long aLong) {
    return false;
  }

  @Override
  public List<User> findAll() {
    return new ArrayList<>(table.values());
  }

  @Override
  public List<User> findAllById(Iterable<Long> longs) {
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
  public void delete(User entity) {

  }

  @Override
  public void deleteAllById(Iterable<? extends Long> longs) {

  }

  @Override
  public void deleteAll(Iterable<? extends User> entities) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public List<User> findAll(Sort sort) {
    return null;
  }

  @Override
  public Page<User> findAll(Pageable pageable) {
    return null;
  }
}
