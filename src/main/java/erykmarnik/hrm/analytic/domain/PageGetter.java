package erykmarnik.hrm.analytic.domain;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class PageGetter {
  static <T> Page<T> getPage(List<T> result, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), result.size());
    List<T> pageContent = result.subList(start, end);
    return new PageImpl<>(pageContent, pageable, result.size());
  }
}
