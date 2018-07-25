package controller;

// for simple testing

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hechenberger.employee.ApplicationStart;
import me.hechenberger.employee.model.Employee;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ApplicationStart.class)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

  private static MediaType CONTENTTYPE = new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype(),
          Charset.forName("utf8"));


  @Autowired
  private MockMvc mockMvc;
  private HttpMessageConverter mappingJackson2HttpMessageConverter;

  @Autowired
  private WebApplicationContext webApplicationContext;

  // api endpoint
  private static String apiUrl = "/api/v1/employee/";

  // list to keep track of saved and deleted entries
  private static LinkedList<Employee> EMPLOYEELIST;

  @Autowired
  void setConverters(HttpMessageConverter<?>[] converters) {

    this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

    assertThat(this.mappingJackson2HttpMessageConverter).isNotNull();
  }

  // setup before each test
  @Before
  public void setUp() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  // setup before suite (class) starts
  @BeforeClass
  public static void setupSuite() throws Exception {
    EMPLOYEELIST = new LinkedList<>();
  }

  @Test
  public void createEmployee() throws Exception {
    Employee dummy = getRandomDummy();
    Employee stored = createStoreEmployee(dummy);
    assertThat(stored).isNotNull();
    assertThat(stored).isNotEqualTo(dummy);
    assertThat(stored.getId()).isGreaterThan(0);
    // check for second try
    mockMvc.perform(post(apiUrl).content(this.json(dummy)).contentType(CONTENTTYPE)).andExpect(status().isNotAcceptable());
  }

  @Test
  public void updateEmployee() throws Exception {
    Employee stored = createStoreEmployee(getRandomDummy());
    stored.setFirstName(stored.getFirstName() + " " + Math.random());
    mockMvc.perform(put(apiUrl).content(this.json(stored)).contentType(CONTENTTYPE)).andExpect(status().isOk());
    stored = (Employee) stored.clone(); // id is set to 0 by cloning it
    mockMvc.perform(put(apiUrl).content(this.json(stored)).contentType(CONTENTTYPE)).andExpect(status().isNotAcceptable());
  }

  @Test
  public void deleteEmployee() throws Exception {
    Employee stored = createStoreEmployee(getRandomDummy());
    mockMvc.perform(delete(apiUrl).content(this.json(stored)).contentType(CONTENTTYPE)).andExpect(status().isOk());
    EMPLOYEELIST.remove(stored);
  }

  @Test
  public void getAllEmployee() throws Exception {
    createStoreEmployee(getRandomDummy());
    ResultActions res = mockMvc.perform(get(apiUrl).contentType(CONTENTTYPE)).andExpect(status().isOk());
    Employee[] list = readObj(Employee[].class,res);
    assertThat(list.length).isEqualTo(EMPLOYEELIST.size());
  }

  @Test
  public void getEmployee() throws Exception {
    Employee stored = createStoreEmployee(getRandomDummy());
    ResultActions res = mockMvc.perform(get(apiUrl + stored.getId()).contentType(CONTENTTYPE)).andExpect(status().isOk());
    Employee returnedEmployee = readObj(Employee.class,res);
    assertThat(stored).isEqualTo(returnedEmployee);
  }

  // ##############################################################################################
  // ############# HELPER FUNCTIONS
  // ##############################################################################################

  private <T> T readObj(Class clazz, ResultActions res) throws Exception{
    return new ObjectMapper().readerFor(clazz).readValue(res.andReturn().getResponse().getContentAsString());
  }

  /**
   * converts an object to an json string
   * @param o
   * @return corresponding json string of object
   */
  private String json(Object o) {
    try {
      MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
      this.mappingJackson2HttpMessageConverter.write(
              o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
      return mockHttpOutputMessage.getBodyAsString();
    } catch (IOException io) {
      io.printStackTrace();
    }
    return null;
  }

  /**
   * create a dummy with a random name and email
   * @return employee object
   */
  private Employee getRandomDummy() {
    return new Employee("jane.doe" + Math.random() + "@example.com", "Jane" + Math.random(), "Doe", new Date(), new String[]{"soccer", "music", "dance"});
  }

  /**
   * store an employee via the api and save it into a list to check adding updating and remove
   * @param employee
   * @return corresponding employee, if the id is not null, the employee was already stored
   * @throws Exception
   */
  private Employee createStoreEmployee(Employee employee) throws Exception {
    if (employee.getId() == null) {
      ResultActions res = mockMvc.perform(post(apiUrl).content(this.json(employee)).contentType(CONTENTTYPE)).andExpect(status().isCreated());
      Employee stored = readObj(Employee.class,res);
      EMPLOYEELIST.add(stored);
      return stored;
    }
    return employee;
  }
}
