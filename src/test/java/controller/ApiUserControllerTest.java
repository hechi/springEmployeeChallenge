package controller;

import me.hechenberger.employee.ApplicationStart;
import me.hechenberger.employee.model.ApiUser;
import me.hechenberger.employee.model.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import utils.HttpRequestCaller;
import utils.TestUtils;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ApplicationStart.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class ApiUserControllerTest {

  private static MediaType CONTENTTYPE = new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype(),
          Charset.forName("utf8"));


  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  // api endpoint
  private static String apiUrl = "/api/v1/user/";
  private static String apiUrlEmployee = "/api/v1/employee/";

  private static HttpRequestCaller requestUser;
  private static HttpRequestCaller requestEmployee;

  // setup before each test
  @Before
  public void setUp() throws Exception {
    this.mockMvc = MockMvcBuilders.
            webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();
    requestUser = new HttpRequestCaller(mockMvc,apiUrl);
    requestEmployee = new HttpRequestCaller(mockMvc,apiUrlEmployee);
  }

  @Test
  public void testRegistration() throws Exception {
    ApiUser john = new ApiUser("john@doe.com","johny");
    Employee employee = TestUtils.getRandomDummy();
    requestEmployee.post(employee).andExpect(status().isUnauthorized());
    ResultActions resUser = requestUser.post(john).andExpect(status().isCreated());
    requestEmployee.post(employee,user(john.getUsername()).password(john.getPassword())).andExpect(status().isCreated());
    ApiUser storedJohn = TestUtils.readObj(ApiUser.class,resUser);
    assertThat(storedJohn).isNotNull();
    assertThat(storedJohn.getUsername()).isEqualTo(john.getUsername());
    assertThat(storedJohn.getPassword()).isNotEqualTo(john.getPassword()); // password should be hashed
  }

}
