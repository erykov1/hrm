package erykmarnik.hrm.task.domain

import erykmarnik.hrm.task.dto.CategoryDto
import java.time.Instant

trait CategorySample {
  static final long CATEGORY_ID = 11L
  static final String CATEGORY_NAME = "new employee"
  static final String ONBOARDING = "Onboarding"
  static final String NEWS = "news"
  static final long CATEGORY_CREATOR = 1L

  private Map<String, Object> DEFAULT_CATEGORY_DATA = [
          categoryId: CATEGORY_ID,
          categoryName: CATEGORY_NAME,
          createdBy: CATEGORY_CREATOR,
          createdAt: Instant.now(),
  ] as Map<String, Object>

  CategoryDto createCategory(Map<String, Object> changes = [:]) {
    def changesWithDefaults = DEFAULT_CATEGORY_DATA + changes
    CategoryDto.builder()
            .categoryId(changesWithDefaults.categoryId as Long)
            .categoryName(changesWithDefaults.categoryName as String)
            .createdBy(changesWithDefaults.createdBy as Long)
            .createdAt(changesWithDefaults.createdAt as Instant)
            .build()
  }

  void equalsCategories(List<CategoryDto> expected, List<CategoryDto> result) {
    def comparator = Comparator.comparing(CategoryDto::getCreatedAt)
    result.sort(comparator)
    expected.sort(comparator)
    assert result.categoryId == expected.categoryId
    assert result.categoryName == expected.categoryName
    assert result.createdBy == expected.createdBy
    assert result.createdAt == expected.createdAt
  }
}