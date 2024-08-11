package erykmarnik.hrm.assignments.domain;

import erykmarnik.hrm.assignments.dto.CreateAssignmentDto;
import erykmarnik.hrm.utils.ContextHolder;
import erykmarnik.hrm.utils.InstantProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AssignmentCreator {
  InstantProvider instantProvider;

  Assignment createAssignment(CreateAssignmentDto createAssignment) {
    return Assignment.builder()
            .userId(createAssignment.getUserId())
            .objectId(createAssignment.getObjectId())
            .assignedAt(instantProvider.now())
            .assignmentCreatedBy(ContextHolder.getUserContext().getUserId())
            .assignmentStatus(AssignmentStatus.NOT_STARTED)
            .build();
  }
}
