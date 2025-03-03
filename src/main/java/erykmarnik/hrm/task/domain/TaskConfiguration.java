package erykmarnik.hrm.task.domain;

import erykmarnik.hrm.security.SecurityFacade;
import erykmarnik.hrm.utils.InstantProvider;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TaskConfiguration {
  @Bean
  TaskFacade taskFacade(InstantProvider instantProvider, CategoryRepository categoryRepository) {
    return TaskFacade.builder()
            .taskCreator(new TaskCreator(instantProvider))
            .instantProvider(instantProvider)
            .categoryRepository(categoryRepository)
            .build();
  }

  TaskFacade taskFacade(InstantProvider instantProvider) {
    return TaskFacade.builder()
            .taskCreator(new TaskCreator(instantProvider))
            .instantProvider(instantProvider)
            .categoryRepository(new InMemoryCategoryRepository())
            .build();
  }
}
