package service;

// for simple testing
import static org.assertj.core.api.Assertions.*;

import me.hechenberger.employee.ApplicationStart;
import me.hechenberger.employee.model.Employee;
import me.hechenberger.employee.service.IEmployeeService;
import me.hechenberger.employee.service.impl.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Validator;

import javax.validation.Validation;
import java.util.Date;
import java.util.List;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationStart.class)
public class EmployeeServiceTest {

  @TestConfiguration
  static class EmployeeServiceTestContextConfiguration {

    @Bean
    public EmployeeService employeeService() {
      return new EmployeeService();
    }
  }

  @Autowired
  private IEmployeeService employeeService;

  private Employee dummyEmployee;
  private Employee dummyEmployeeWithHobbies;

  @Before
  public void setUp(){
    dummyEmployee = new Employee("john.doe@example.com","John","Doe",new Date());
    dummyEmployeeWithHobbies = new Employee("jane.doe@example.com","Jane","Doe",new Date(),new String[]{"soccer","music","dance"});
  }

  // check empty list
  @Test
  public void checkEmpty(){
    List<Employee> employeeList = employeeService.getAll();
    assertThat(employeeList.size()).isEqualTo(0);
  }

  // create employee
  // create employee twice should throw error
  @Test
  public void createEmployee(){
    Employee stored = employeeService.save(dummyEmployee);
    assertThat(stored).isNotNull();
    assertThat(stored.getId()).isGreaterThan(0);
    stored = employeeService.save(dummyEmployee);
    assertThat(stored).isNull();
  }

  // update employee
  @Test
  public void updateEmployee(){
    Employee stored = employeeService.save((Employee)dummyEmployeeWithHobbies.clone());
    assertThat(stored).isNotNull();
    stored.setFirstName("Jane Anna");
    stored = employeeService.update(stored);
    assertThat(stored).isNotNull().isNotEqualTo(dummyEmployeeWithHobbies);
    assertThat(stored.getFirstName()).isEqualTo("Jane Anna");
  }

  // delete employee
  @Test
  public void deleteEmployee(){
    // be sure that at least one is there
    employeeService.save(dummyEmployeeWithHobbies);
    List<Employee> employeeList = employeeService.getAll();
    assertThat(employeeList).isNotEmpty();
    employeeList.forEach(employee -> {
      employeeService.remove(employee);
    });
    assertThat(employeeService.getAll()).isEmpty();
  }


}
