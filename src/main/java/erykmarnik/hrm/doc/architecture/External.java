package erykmarnik.hrm.doc.architecture;

import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class External {
  Person admin;
  Person employee;
  SoftwareSystem database;

  External(Model model) {
    admin = model.addPerson("admin", "hrm admin");
    employee = model.addPerson("employee", "user with basic role");
    database = model.addSoftwareSystem("database", "PostgreSQL database");
  }

  SoftwareSystem createUsages(SoftwareSystem eLearn) {
    admin.uses(eLearn, "creates admin/employee account, modifies accounts, deletes accounts, views employee list");
    employee.uses(eLearn, "views his user data");
    database.uses(eLearn, "data operation");
    return eLearn;
  }
}
