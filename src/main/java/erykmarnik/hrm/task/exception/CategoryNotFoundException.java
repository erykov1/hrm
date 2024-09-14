package erykmarnik.hrm.task.exception;

public class CategoryNotFoundException extends RuntimeException {
  public CategoryNotFoundException(Long categoryId) {
    super("category with id: " + categoryId + " not found");
  }
}
