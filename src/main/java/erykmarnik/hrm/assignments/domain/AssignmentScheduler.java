package erykmarnik.hrm.assignments.domain;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AssignmentScheduler {
  AssignmentFacade assignmentFacade;
  Environment environment;

  @Autowired
  AssignmentScheduler(AssignmentFacade assignmentFacade, Environment environment) {
    this.assignmentFacade = assignmentFacade;
    this.environment = environment;
    if (!isEnabled()) {
      log.warn("Assignment scheduler is disabled");
    } else {
      log.warn("Assignment scheduler is enabled");
    }
  }

  @Scheduled(timeUnit = TimeUnit.SECONDS, fixedDelay = 120, initialDelay = 30)
  public void execute() {
    if (isEnabled()) {
      assignmentFacade.outDateNotStartedAssignments();
    }
  }

  private boolean isEnabled() {
    return environment.getProperty("assignment.scheduler.enabled", Boolean.class, true);
  }
}
