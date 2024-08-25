package erykmarnik.hrm.assignments.domain;

import erykmarnik.hrm.security.SecurityFacade;
import erykmarnik.hrm.task.domain.TaskFacade;
import erykmarnik.hrm.user.domain.UserFacade;
import erykmarnik.hrm.utils.InstantProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AssignmentConfiguration {
  @Bean
  AssignmentAnalytic assignmentAnalytic(AssignmentRepository assignmentRepository, UserFacade userFacade, TaskFacade taskFacade) {
    return AssignmentAnalytic.builder()
            .assignmentRepository(assignmentRepository)
            .userFacade(userFacade)
            .taskFacade(taskFacade)
            .build();
  }

  @Bean
  AssignmentFacade assignmentFacade(AssignmentRepository assignmentRepository, SecurityFacade securityFacade,
                                    InstantProvider instantProvider, AssignmentAnalytic assignmentAnalytic,
                                    AssignmentNoteRepository assignmentNoteRepository) {
    return AssignmentFacade.builder()
            .assignmentRepository(assignmentRepository)
            .securityFacade(securityFacade)
            .assignmentCreator(new AssignmentCreator(instantProvider))
            .instantProvider(instantProvider)
            .assignmentAnalytic(assignmentAnalytic)
            .assignmentNoteRepository(assignmentNoteRepository)
            .build();
  }

  AssignmentFacade assignmentFacade(InstantProvider instantProvider, SecurityFacade securityFacade, AssignmentAnalytic assignmentAnalytic) {
    return AssignmentFacade.builder()
            .assignmentRepository(new InMemoryAssignmentRepository())
            .assignmentCreator(new AssignmentCreator(instantProvider))
            .instantProvider(instantProvider)
            .securityFacade(securityFacade)
            .assignmentAnalytic(assignmentAnalytic)
            .assignmentNoteRepository(new InMemoryAssignmentNoteRepository())
            .build();
  }
}
