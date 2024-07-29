package erykmarnik.hrm.doc.architecture;

import com.structurizr.Workspace;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.PaperSize;
import com.structurizr.view.StaticView;
import com.structurizr.view.ViewSet;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ContextDiagram {
  static External create(Workspace workspace, Model model, SoftwareSystem eLearn) {
    External external = new External(model);
    external.createUsages(eLearn);
    setupContextView(workspace, eLearn);
    return external;
  }

  private static void setupContextView(Workspace workspace, SoftwareSystem softwareSystem) {
    Function<ViewSet, StaticView> contextViewCreator = views ->
            views.createSystemContextView(
                    softwareSystem,
                    "context diagram",
                    "context view"
            );
    ViewCreator.setupView(workspace, contextViewCreator, PaperSize.A5_Landscape);
  }
}
