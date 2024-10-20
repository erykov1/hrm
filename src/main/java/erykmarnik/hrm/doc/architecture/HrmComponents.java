package erykmarnik.hrm.doc.architecture;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.view.ComponentView;
import com.structurizr.view.PaperSize;
import com.structurizr.view.ViewSet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import java.util.function.Function;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class HrmComponents {
  ModulesInteractor modulesInteractor = new ModulesInteractor();
  Component userController;
  Component authController;
  Component userFacade;
  Component securityFacade;
  Component taskFacade;
  Component taskController;
  Component assignmentController;
  Component assignmentFacade;
  Component categoryController;
  Component notificationFacade;

  HrmComponents(Container hrm) {
    userController = hrm.addComponent("user controller");
    authController = hrm.addComponent("auth controller");
    userFacade = hrm.addComponent("user facade");
    securityFacade = hrm.addComponent("security facade");
    assignmentController = hrm.addComponent("assignment controller");
    assignmentFacade = hrm.addComponent("assignment facade");
    taskController = hrm.addComponent("task controller");
    taskFacade = hrm.addComponent("task facade");
    categoryController = hrm.addComponent("category controller");
    notificationFacade = hrm.addComponent("notification facade");
  }

  void createUsages(External external) {
    modulesInteractor.createInteractors(external);
  }

  private class ModulesInteractor {
    void createInteractors(External external) {
      external.getEmployee().uses(authController, "makes api call to login");
      authController.uses(securityFacade, "asks for generating jwt token");
      securityFacade.uses(userFacade, "check if user exists");
      userFacade.uses(external.getDatabase(), "asks for user with given username and login");
      authController.uses(securityFacade, "generating token if credentials are valid");
      external.getAdmin().uses(userController, "makes api call to register employee/admin");
      userController.uses(userFacade, "creates new employee/admin");
      userFacade.uses(external.getDatabase(), "inserts new user data");
      external.getAdmin().uses(userController, "makes api call to get all users");
      userController.uses(userFacade, "asks for all users");
      userFacade.uses(external.getDatabase(), "get data from db and pass to controller");
      external.getEmployee().uses(userController, "makes api call to modify his data");
      userController.uses(userFacade, "asks to modify existing user data");
      userFacade.uses(external.getDatabase(), "modifies user data");
      external.getAdmin().uses(userController, "makes api call to delete user");
      userController.uses(userFacade, "asks to delete user");
      userFacade.uses(external.getDatabase(), "deletes user");
      external.getAdmin().uses(taskController, "makes api call to create/delete/modify task");
      external.getEmployee().uses(taskController, "makes api call to get task");
      taskController.uses(taskFacade, "asks to create/modify/get tasks");
      taskFacade.uses(external.getDatabase(), "inserts new tasks/category, modify, get tasks/category");
      external.getAdmin().uses(assignmentController, "makes api call to create/delete assignments, get analytic data for assignments");
      external.getEmployee().uses(assignmentController, "makes api call to set to done assignments");
      assignmentController.uses(assignmentFacade, "asks to delete/create/set to done assignment, gets info about analytic data for assignments");
      assignmentFacade.uses(external.getDatabase(), "inserts/deletes assignments, get data for assignments");
      assignmentFacade.uses(userFacade, "gets user info for analytic data");
      assignmentFacade.uses(taskFacade, "gets task info for analytic data");
      external.getEmployee().uses(assignmentController, "makes api call to set to done assignments, gets assignments info");
      external.getEmployee().uses(assignmentController, "makes api call to add/get/delete/modifies note to assigned object");
      assignmentController.uses(assignmentFacade, "asks to add/get/delete/modifies note to user assigned object");
      assignmentFacade.uses(securityFacade, "asks to validate user operations on notes");
      external.getAdmin().uses(categoryController, "makes api call to create/get/modify/delete category");
      categoryController.uses(taskFacade, "creates/modifies/gets/deletes category");
      notificationFacade.delivers(external.getEmployee(), "sends notification when user is assigned/removed or assignation status is changed");
    }
  }

  static void create(Workspace workspace, HrmContainers hrmContainers, External external, HrmComponents hrmComponents) {
    hrmComponents.createUsages(external);
    Function<ViewSet, ComponentView> componentViewCreator = view ->
            view.createComponentView(hrmContainers.getHrm(), "components", "hrm components");
    ViewSet viewSet = workspace.getViews();
    ComponentView contextView = componentViewCreator.apply(viewSet);
    contextView.setPaperSize(PaperSize.A2_Landscape);
    contextView.add(hrmComponents.getAuthController());
    contextView.add(hrmComponents.getUserController());
    contextView.add(hrmComponents.getSecurityFacade());
    contextView.add(hrmComponents.getUserFacade());
    contextView.add(external.getDatabase());
    contextView.add(external.getEmployee());
    contextView.add(external.getAdmin());
    contextView.add(hrmComponents.getTaskController());
    contextView.add(hrmComponents.getTaskFacade());
    contextView.add(hrmComponents.getAssignmentController());
    contextView.add(hrmComponents.getAssignmentFacade());
    contextView.add(hrmComponents.getCategoryController());
    contextView.add(hrmComponents.getNotificationFacade());
  }
}
