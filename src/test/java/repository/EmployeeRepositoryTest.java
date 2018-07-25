package repository;

// for simple testing
import static org.assertj.core.api.Assertions.*;

import me.hechenberger.employee.ApplicationStart;
import me.hechenberger.employee.model.Employee;
import me.hechenberger.employee.respository.EmployeeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * check only the own defined functions in the employee repository
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationStart.class)
@DataJpaTest
public class EmployeeRepositoryTest {

  @Autowired
  private EmployeeRepository employeeRepository;

  private Employee dummyEmployee;
  private Employee dummyEmployeeWithHobbies;

  @Before
  public void setUp(){
    // create dummies
    dummyEmployee = new Employee("john.doe@example.com","John","Doe",new Date());
    dummyEmployeeWithHobbies = new Employee("jane.doe@example.com","Jane","Doe",new Date(),new String[]{"soccer","music","dance"});
  }

  @After
  public void tearDown(){
    employeeRepository.deleteAll();
  }

  // find an employee
  @Test
  public void findEmployeeByEmail(){
    storeEmployee(dummyEmployee);
    storeEmployee(dummyEmployeeWithHobbies);

    // find dummy by mail
    Employee found = employeeRepository.findByEmail(dummyEmployee.getEmail());

    // check if those are the same
    assertThat(found.getId()).isNotNull();
    assertThat(found).isEqualTo(dummyEmployee).isNotEqualTo(dummyEmployeeWithHobbies);
  }

  // check if employee exists by mail
  @Test
  public void employeeExistsByMail(){
    storeEmployee(dummyEmployee);
    // check if dummy email address exists
    assertThat(employeeRepository.existsByEmail(dummyEmployee.getEmail())).isTrue();
    // search for non existing email address
    assertThat(employeeRepository.existsByEmail(dummyEmployeeWithHobbies.getEmail())).isFalse();

  }

  // ##############################################################################################
  // ############# HELPER FUNCTIONS
  // ##############################################################################################

  /**
   * store employee and set the saved id
   * @param employee
   */
  private void storeEmployee(Employee employee){
    // persist dummy
    employee.setId(employeeRepository.save(employee).getId());
    assertThat(employee.getId()).isGreaterThan(0);//0 means not saved in the repository
  }

}
