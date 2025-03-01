package erykmarnik.hrm.analytic.domain;

import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AnalyticFacadeConfiguration {
  @Bean
  AnalyticQueryFactory analyticQueryFactory(EntityManager entityManager) {
    return AnalyticQueryFactory.builder()
            .entityManager(entityManager)
            .statusAssignmentsQueryProvider(new StatusAssignmentsQueryProvider())
            .userAssignmentsQueryProvider(new AllUserAssignmentsQueryProvider())
            .build();
  }

  @Bean
  AnalyticFacade analyticFacade(AnalyticQueryFactory analyticQueryFactory) {
    return new AnalyticFacade(analyticQueryFactory);
  }
}
