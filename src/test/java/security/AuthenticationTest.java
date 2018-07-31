package security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import me.hechenberger.employee.ApplicationStart;
import me.hechenberger.employee.model.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ApplicationStart.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class AuthenticationTest {

  private static MediaType CONTENTTYPE = new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype(),
          Charset.forName("utf8"));


  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  // api endpoint
  private static String apiUrl = "/api/v1/employee/";
  private static HttpRequestCaller requester;

  public static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor allowedUser() {
    return user("admin").password("1234");
  }

  // setup before each test
  @Before
  public void setUp() throws Exception {
    this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();
    requester = new HttpRequestCaller(mockMvc,apiUrl);
  }

  @Test
  public void testPermissions() throws Exception {
    // post
    Employee employee = TestUtils.getRandomDummy();
    requester.post(employee).andExpect(status().isUnauthorized());
    ResultActions res = requester.post(employee,allowedUser()).andExpect(status().isCreated());
    Employee stored = TestUtils.readObj(Employee.class, res);

    // get
    requester.get().andExpect(status().isOk());
    requester.get(stored.getId().toString()).andExpect(status().isOk());

    requester.get(allowedUser()).andExpect(status().isOk());
    requester.get(stored.getId().toString(),allowedUser()).andExpect(status().isOk());

    // update
    stored.setFirstName(stored.getFirstName() + Math.random());
    requester.put(stored).andExpect(status().isUnauthorized());
    requester.put(stored,allowedUser()).andExpect(status().isOk());

    // delete
    requester.delete(stored).andExpect(status().isUnauthorized());
    requester.delete(stored,allowedUser()).andExpect(status().isOk());
  }




}
