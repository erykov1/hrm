package erykmarnik.hrm.assignments.domain;

import erykmarnik.hrm.security.SecurityFacade;
import erykmarnik.hrm.utils.InstantProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AssignmentConfiguration {
  @Bean
  AssignmentFacade assignmentFacade(AssignmentRepository assignmentRepository, SecurityFacade securityFacade, InstantProvider instantProvider) {
    return AssignmentFacade.builder()
            .assignmentRepository(assignmentRepository)
            .securityFacade(securityFacade)
            .assignmentCreator(new AssignmentCreator(instantProvider))
            .instantProvider(instantProvider)
            .build();
  }

  AssignmentFacade assignmentFacade(InstantProvider instantProvider, SecurityFacade securityFacade) {
    return AssignmentFacade.builder()
            .assignmentRepository(new InMemoryAssignmentRepository())
            .assignmentCreator(new AssignmentCreator(instantProvider))
            .instantProvider(instantProvider)
            .securityFacade(securityFacade)
            .build();
  }
}
