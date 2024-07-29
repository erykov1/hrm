package erykmarnik.hrm.doc.architecture;

import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class HrmContainers {
  Container hrm;

  public HrmContainers(SoftwareSystem hrm) {
    this.hrm = hrm.addContainer("Hrm");
  }

  static HrmContainers create(SoftwareSystem hrmSys) {
    return new HrmContainers(hrmSys);
  }
}
