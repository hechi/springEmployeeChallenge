package model;

// for simple testing
import static org.assertj.core.api.Assertions.*;

import me.hechenberger.employee.model.Employee;
import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import utils.TestUtils;

import javax.validation.ConstraintViolation;
import java.util.Date;
import java.util.Set;

/**
 * Test the model
 */
public class EmployeeTest {
  private Employee dummyEmployee;
  private LocalValidatorFactoryBean validator;


  @Before
  public void setUp(){
    dummyEmployee = TestUtils.getRandomDummy();
    validator = new LocalValidatorFactoryBean();
    validator.setProviderClass(HibernateValidator.class);
    validator.afterPropertiesSet();
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

  @Test
  public void checkValidation(){
    Employee dummy = new Employee("","","",new Date());
    Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(dummy);
    assertThat(constraintViolations.size()).isEqualTo(3); // violate 3 constrains
  }
}
