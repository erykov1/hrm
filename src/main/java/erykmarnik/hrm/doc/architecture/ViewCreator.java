package erykmarnik.hrm.doc.architecture;

import com.structurizr.Workspace;
import com.structurizr.model.Tags;
import com.structurizr.view.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ViewCreator {
  static Workspace setupView(Workspace workspace, Function<ViewSet, StaticView> viewGenerator, PaperSize paperSize) {
    ViewSet viewSet = workspace.getViews();
    StaticView contextView = viewGenerator.apply(viewSet);
    contextView.setPaperSize(paperSize);
    contextView.addAllElements();
    return workspace;
  }

  static void setupStyles(Workspace workspace) {
    ViewSet viewSet = workspace.getViews();
    Styles styles = viewSet.getConfiguration().getStyles();
    styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd").color("#ffffff");
    styles.addElementStyle(Tags.PERSON).background("#08427b").color("#ffffff").shape(Shape.Person);
    styles.addRelationshipStyle(Tags.RELATIONSHIP).width(800).setDashed(false);
  }
}
