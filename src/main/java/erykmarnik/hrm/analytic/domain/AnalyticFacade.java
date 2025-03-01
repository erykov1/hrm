package erykmarnik.hrm.analytic.domain;

import erykmarnik.hrm.analytic.dto.AssignmentInfoDto;
import erykmarnik.hrm.analytic.dto.RequestParams;
import erykmarnik.hrm.analytic.dto.UserAssignmentDto;
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto;
import erykmarnik.hrm.utils.ContextHolder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnalyticFacade {
  AnalyticQueryFactory queryFactory;

  public Page<AssignmentInfoDto> getAssignmentsWithStatus(RequestParams requestParams, AssignmentStatusDto assignmentStatus) {
    log.info("getting all assignments with status");
    return queryFactory.getAssignmentsWithStatus(requestParams, assignmentStatus);
  }

  public Page<UserAssignmentDto> getUserAssignments(RequestParams requestParams) {
    log.info("getting all user : " + ContextHolder.getUserContext().getUserId() + " assignments");
    return queryFactory.getAllUserAssignments(requestParams);
  }
}
