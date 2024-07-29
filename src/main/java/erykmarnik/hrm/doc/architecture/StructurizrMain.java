package erykmarnik.hrm.doc.architecture;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClientException;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;

class StructurizrMain {
  public static void main(String[] args) throws StructurizrClientException {
    Workspace workspace = createWorkspace();
    ViewCreator.setupStyles(workspace);
    ViewUploader.upload(workspace);
  }

  private static Workspace createWorkspace() {
    Workspace workspace = new Workspace("hrm", "human resources management platform");
    Model model = workspace.getModel();
    SoftwareSystem eLearn = model.addSoftwareSystem("hrm system");
    External external = ContextDiagram.create(workspace, model, eLearn);
    HrmContainers hrmContainers = HrmContainers.create(eLearn);
    HrmComponents hrmComponents = new HrmComponents(hrmContainers.getHrm());
    HrmComponents.create(workspace, hrmContainers, external, hrmComponents);
    return workspace;
  }
}
