package model;

// for simple testing
import static org.assertj.core.api.Assertions.*;

import me.hechenberger.employee.model.Employee;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Test the model
 */
public class EmployeeTest {
  private Employee dummyEmployee;
  private Employee dummyEmployeeWithHobbies;

  @Before
  public void setUp(){
    dummyEmployee = new Employee("john.doe@example.com","John","Doe",new Date());
    dummyEmployeeWithHobbies = new Employee("jane.doe@example.com","Jane","Doe",new Date(),new String[]{"soccer","music","dance"});
  }

  @Test
  public void testEquals(){
    Employee dummy2 = (Employee)dummyEmployee.clone();
    assertThat(dummy2).isEqualTo(dummyEmployee);
    dummy2.setFirstName("John Hans");
    assertThat(dummy2).isNotEqualTo(dummyEmployee);
  }

  @Test
  public void testHashCode(){
    Employee dummy2 = (Employee)dummyEmployee.clone();
    assertThat(dummy2.hashCode()).isEqualTo(dummyEmployee.hashCode());
    dummy2.setFirstName("John Hans");
    assertThat(dummy2.hashCode()).isNotEqualTo(dummyEmployee.hashCode());
  }
}
