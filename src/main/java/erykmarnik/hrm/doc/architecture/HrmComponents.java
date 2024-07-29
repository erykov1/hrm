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

  HrmComponents(Container hrm) {
    userController = hrm.addComponent("user controller");
    authController = hrm.addComponent("auth controller");
    userFacade = hrm.addComponent("user facade");
    securityFacade = hrm.addComponent("security facade");
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
  }
}
