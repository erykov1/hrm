package erykmarnik.hrm.analytic;

import erykmarnik.hrm.analytic.domain.AnalyticFacade;
import erykmarnik.hrm.analytic.dto.AssignmentInfoDto;
import erykmarnik.hrm.analytic.dto.RequestParams;
import erykmarnik.hrm.analytic.dto.UserAssignmentDto;
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytic")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class AnalyticController {
  AnalyticFacade analyticFacade;

  @Autowired
  AnalyticController(AnalyticFacade analyticFacade) {
    this.analyticFacade = analyticFacade;
  }

  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  @GetMapping("/user/assignments")
  ResponseEntity<Page<UserAssignmentDto>> getUserAssignments(
          @RequestParam Sort.Direction direction,
          @RequestParam String parameter,
          @RequestParam(required = false) Integer limit,
          @RequestParam(required = false) Integer pageNumber
          ) {
    RequestParams param = RequestParams.builder()
            .limit(limit)
            .order(new Sort.Order(direction, parameter))
            .pageNumber(pageNumber)
            .build();
    return ResponseEntity.ok(analyticFacade.getUserAssignments(param));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/assignments/{status}")
  ResponseEntity<Page<AssignmentInfoDto>> getAssignmentsWithStatus(
          @RequestParam Sort.Direction direction,
          @RequestParam String parameter,
          @RequestParam(required = false) Integer limit,
          @RequestParam(required = false) Integer pageNumber,
          @PathVariable AssignmentStatusDto status
  ) {
    RequestParams param = RequestParams.builder()
            .limit(limit)
            .order(new Sort.Order(direction, parameter))
            .pageNumber(pageNumber)
            .build();
    return ResponseEntity.ok(analyticFacade.getAssignmentsWithStatus(param, status));
  }
}
