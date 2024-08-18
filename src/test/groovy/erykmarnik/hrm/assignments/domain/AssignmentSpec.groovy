package erykmarnik.hrm.assignments.domain

import erykmarnik.hrm.assignments.dto.AssignmentDto
import erykmarnik.hrm.assignments.dto.AssignmentStatusDto
import erykmarnik.hrm.assignments.dto.CreateAssignmentDto
import erykmarnik.hrm.assignments.sample.AssignmentSample
import erykmarnik.hrm.task.sample.TaskSample
import erykmarnik.hrm.utils.ContextSpec
import erykmarnik.hrm.utils.InstantProvider
import erykmarnik.hrm.utils.sample.TimeSample
import erykmarnik.hrm.assignments.exception.AlreadyAssignedException
import erykmarnik.hrm.assignments.exception.ForbiddenAssignmentOperationException
import erykmarnik.hrm.assignments.exception.AssignmentNotFoundException

class AssignmentSpec extends ContextSpec implements TimeSample, AssignmentSample, TaskSample {
  InstantProvider instantProvider = new InstantProvider()
  AssignmentFacade assignmentFacade = new AssignmentConfiguration().assignmentFacade(instantProvider, securityFacade, Stub(AssignmentAnalytic.class))

  def setup() {
    instantProvider.useFixedClock(NOW)
    given: "admin $ADMIN_JANE is logged in"
      loginUser(ADMIN_JANE)
  }

  def "Should create new assignment"() {
    when: "admin $ADMIN_JANE creates assignment for user $EMPLOYEE_MIKE to object $OBJECT_ID"
      AssignmentDto assignment = assignmentFacade.createAssignment(new CreateAssignmentDto(EMPLOYEE_MIKE, OBJECT_ID))
    then: "assignment is created"
      assignment == createAssignment(assignmentId: assignment.assignmentId, userId: EMPLOYEE_MIKE, objectId: OBJECT_ID, assignedAt: NOW,
        doneAt: null, assignmentCreatedBy: ADMIN_JANE, assignmentStatus: AssignmentStatusDto.NOT_STARTED
      )
  }

  def "Should not create new assignment if user is already assigned to object"() {
    given: "admin $ADMIN_JANE creates assignment for user $EMPLOYEE_MIKE to object $OBJECT_ID"
      assignmentFacade.createAssignment(new CreateAssignmentDto(EMPLOYEE_MIKE, OBJECT_ID))
    when: "admin $ADMIN_JANE creates assignment for user $EMPLOYEE_MIKE to object $OBJECT_ID again"
      assignmentFacade.createAssignment(new CreateAssignmentDto(EMPLOYEE_MIKE, OBJECT_ID))
    then: "assignment is not created"
      thrown(AlreadyAssignedException)
  }

  def "Should set to assignment to done if assigned user done object"() {
    given: "admin $ADMIN_JANE creates assignment for user $EMPLOYEE_MIKE to object $OBJECT_ID"
      Long assignmentId = assignmentFacade.createAssignment(new CreateAssignmentDto(EMPLOYEE_MIKE, OBJECT_ID)).assignmentId
    and: "user $EMPLOYEE_MIKE logs in $WEEK_LATER"
      loginUser(EMPLOYEE_MIKE)
      instantProvider.useFixedClock(WEEK_LATER)
    when: "user $EMPLOYEE_MIKE set assignment to done"
      assignmentFacade.setAssignmentToDone(assignmentId)
    then: "assignment status is set to done"
      AssignmentDto result = assignmentFacade.getAssignmentById(assignmentId)
      result == createAssignment(assignmentId: assignmentId, userId: EMPLOYEE_MIKE, objectId: OBJECT_ID, assignedAt: NOW,
              doneAt: WEEK_LATER, assignmentCreatedBy: ADMIN_JANE, assignmentStatus: AssignmentStatusDto.DONE
      )
  }

  def "Should not set to assignment to done if assigned user done object"() {
    given: "admin $ADMIN_JANE creates assignment for user $EMPLOYEE_MIKE to object $OBJECT_ID"
      Long assignmentId = assignmentFacade.createAssignment(new CreateAssignmentDto(EMPLOYEE_MIKE, OBJECT_ID)).assignmentId
    and: "user $EMPLOYEE_JOHN logs in"
      loginUser(EMPLOYEE_JOHN)
    when: "user $EMPLOYEE_JOHN tries to set to done assignment $assignmentId"
      assignmentFacade.setAssignmentToDone(assignmentId)
    then: "assignment is not set to done"
      thrown(ForbiddenAssignmentOperationException)
  }

  def "Should delete assignment"() {
    given: "admin $ADMIN_JANE creates assignment for user $EMPLOYEE_MIKE to object $OBJECT_ID"
      Long assignmentId = assignmentFacade.createAssignment(new CreateAssignmentDto(EMPLOYEE_MIKE, OBJECT_ID)).assignmentId
    when: "admin $ADMIN_JANE deletes assignment for user $EMPLOYEE_MIKE to object $OBJECT_ID"
      assignmentFacade.deleteAssignment(assignmentId)
    then: "assignment is deleted"
      assignmentFacade.getAllAssignments() == []
  }

  def "Employee should not be able to delete assignment"() {
    given: "admin $ADMIN_JANE creates assignment for user $EMPLOYEE_MIKE to object $OBJECT_ID"
      Long assignmentId = assignmentFacade.createAssignment(new CreateAssignmentDto(EMPLOYEE_MIKE, OBJECT_ID)).assignmentId
    and: "$EMPLOYEE_MIKE logs in"
      loginUser(EMPLOYEE_MIKE)
    when: "$EMPLOYEE_MIKE tries to delete assignment"
      assignmentFacade.deleteAssignment(assignmentId)
    then: "assignment with id $assignmentId is not deleted"
      thrown(ForbiddenAssignmentOperationException)
  }

  def "Should not found assignment if assignment does not exist"() {
    when: "asks for assignment that does not exist"
      assignmentFacade.getAssignmentById(ASSIGNMENT_ID)
    then: "assignment is not found"
      thrown(AssignmentNotFoundException)
  }

  def "Should get user assignments"() {
    given: "admin $ADMIN_JANE creates assignment for user $EMPLOYEE_MIKE to object $OBJECT_ID"
      assignmentFacade.createAssignment(new CreateAssignmentDto(EMPLOYEE_MIKE, OBJECT_ID))
    and: "$EMPLOYEE_MIKE logs in"
      loginUser(EMPLOYEE_MIKE)
    when: "user $EMPLOYEE_MIKE asks for his assignments"
      List<AssignmentDto> assignments = assignmentFacade.getUserAssignments()
    then: "gets his assignments"
      assignments == [createAssignment(assignmentId: assignments[0].assignmentId, userId: EMPLOYEE_MIKE, objectId: OBJECT_ID, assignedAt: NOW,
              doneAt: null, assignmentCreatedBy: ADMIN_JANE, assignmentStatus: AssignmentStatusDto.NOT_STARTED
      )]
  }
}
