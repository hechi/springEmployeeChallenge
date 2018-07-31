package controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.TestUtils.readObj;

import me.hechenberger.employee.ApplicationStart;
import me.hechenberger.employee.model.Employee;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import utils.HttpRequestCaller;
import utils.TestUtils;

import java.util.LinkedList;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ApplicationStart.class)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  // api endpoint
  private static String apiUrl = "/api/v1/employee/";
  private static HttpRequestCaller requester;


  // list to keep track of saved and deleted entries
  private static LinkedList<Employee> EMPLOYEELIST;

  // setup before each test
  @Before
  public void setUp() throws Exception {
    this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .build();
    requester = new HttpRequestCaller(mockMvc,apiUrl);
  }

  // setup before suite (class) starts
  @BeforeClass
  public static void setupSuite() throws Exception {
    EMPLOYEELIST = new LinkedList<>();
  }

  @Test
  public void createEmployee() throws Exception {
    Employee dummy = TestUtils.getRandomDummy();
    Employee stored = createStoreEmployee(dummy);
    assertThat(stored).isNotNull();
    assertThat(stored).isNotEqualTo(dummy);
    assertThat(stored.getId()).isGreaterThan(0);
    // check for second try
    requester.post(dummy).andExpect(status().isNotAcceptable());
  }

  @Test
  public void updateEmployee() throws Exception {
    Employee stored = createStoreEmployee(TestUtils.getRandomDummy());
    stored.setFirstName(stored.getFirstName() + " " + Math.random());
    requester.put(stored).andExpect(status().isOk());
    stored = (Employee) stored.clone(); // id is set to 0 by cloning it
    requester.put(stored).andExpect(status().isNotAcceptable());
  }

  @Test
  public void deleteEmployee() throws Exception {
    Employee stored = createStoreEmployee(TestUtils.getRandomDummy());
    requester.delete(stored).andExpect(status().isOk());
    EMPLOYEELIST.remove(stored);
  }

  @Test
  public void getAllEmployee() throws Exception {
    createStoreEmployee(TestUtils.getRandomDummy());
    ResultActions res = requester.get().andExpect(status().isOk());
    Employee[] list = readObj(Employee[].class,res);
    assertThat(list.length).isEqualTo(EMPLOYEELIST.size());
  }

  @Test
  public void getEmployee() throws Exception {
    Employee stored = createStoreEmployee(TestUtils.getRandomDummy());
    ResultActions res = requester.get(stored.getId().toString()).andExpect(status().isOk());
    Employee returnedEmployee = TestUtils.readObj(Employee.class,res);
    assertThat(stored).isEqualTo(returnedEmployee);
  }

  // ##############################################################################################
  // ############# HELPER FUNCTIONS
  // ##############################################################################################

  /**
   * store an employee via the api and save it into a list to check adding updating and remove
   * @param employee
   * @return corresponding employee, if the id is not null, the employee was already stored
   * @throws Exception
   */
  private Employee createStoreEmployee(Employee employee) throws Exception {
    if (employee.getId() == null) {
      ResultActions res = requester.post(employee).andExpect(status().isCreated());
      Employee stored = TestUtils.readObj(Employee.class,res);
      EMPLOYEELIST.add(stored);
      return stored;
    }
    return employee;
  }
}
