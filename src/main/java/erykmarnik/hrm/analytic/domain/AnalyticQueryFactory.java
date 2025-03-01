package erykmarnik.hrm.analytic.domain;

import erykmarnik.hrm.analytic.dto.AssignmentInfoDto;
import erykmarnik.hrm.analytic.dto.QueryConfig;
import erykmarnik.hrm.analytic.dto.RequestParams;
import erykmarnik.hrm.analytic.dto.UserAssignmentDto;
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AnalyticQueryFactory {
  EntityManager entityManager;
  AllUserAssignmentsQueryProvider userAssignmentsQueryProvider;
  StatusAssignmentsQueryProvider statusAssignmentsQueryProvider;

  Page<UserAssignmentDto> getAllUserAssignments(RequestParams requestParams) {
    String query = userAssignmentsQueryProvider.renderQuery(QueryConfig.from(requestParams));
    Query queryResult = entityManager.createNativeQuery(query);
    List<Object[]> objects = queryResult.getResultList();
    List<UserAssignmentDto> result = objects.stream()
            .map(ObjectResultParser::parseToUserAssignment)
            .collect(Collectors.toList());
    return PageGetter.getPage(result, requestParams.getPageNumber(), requestParams.getLimit());
  }

  Page<AssignmentInfoDto> getAssignmentsWithStatus(RequestParams requestParams, AssignmentStatusDto assignmentStatus) {
    String query = statusAssignmentsQueryProvider.renderQuery(QueryConfig.from(requestParams), assignmentStatus);
    Query queryResult = entityManager.createNativeQuery(query);
    List<Object[]> objects = queryResult.getResultList();
    List<AssignmentInfoDto> result = objects.stream()
            .map(ObjectResultParser::parseToAssignmentInfo)
            .collect(Collectors.toList());
    return PageGetter.getPage(result, requestParams.getPageNumber(), requestParams.getLimit());
  }
}
