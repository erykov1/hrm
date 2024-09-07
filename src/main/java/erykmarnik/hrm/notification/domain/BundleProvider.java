package erykmarnik.hrm.notification.domain;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ResourceBundle;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class BundleProvider {
  ResourceBundle resourceBundle;
  private static final String PATH_TO_RESOURCE_BUNDLE = "locales/hrm_messages";

  BundleProvider() {
    this.resourceBundle = ResourceBundle.getBundle(PATH_TO_RESOURCE_BUNDLE);
  }

  protected String getPathToResourceBundle() {
    return PATH_TO_RESOURCE_BUNDLE;
  }

  public ResourceBundle getResourceBundle() {
    return this.resourceBundle;
  }
}
