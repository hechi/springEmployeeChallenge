package security;

// for simple testing

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hechenberger.employee.ApplicationStart;
import me.hechenberger.employee.model.Employee;
import org.junit.Before;
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
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;

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
  private HttpMessageConverter mappingJackson2HttpMessageConverter;

  @Autowired
  private WebApplicationContext webApplicationContext;

  // api endpoint
  private static String apiUrl = "/api/v1/employee/";

  public static RequestPostProcessor allowedUser() {
    return user("admin").password("1234");
  }

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
    this.mockMvc = MockMvcBuilders.
            webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();
  }

  @Test
  public void testPermissions() throws Exception {
    // post
    Employee employee = new Employee("jane.doe" + Math.random() + "@example.com", "Jane" + Math.random(), "Doe", new Date(), new String[]{"soccer", "music", "dance"});
    mockMvc.perform(post(apiUrl).content(this.json(employee)).contentType(CONTENTTYPE)).andExpect(status().isUnauthorized());
    ResultActions res = mockMvc.perform(post(apiUrl).content(this.json(employee)).contentType(CONTENTTYPE).with(allowedUser())).andExpect(status().isCreated());
    Employee stored = readObj(Employee.class, res);

    // get
    mockMvc.perform(get(apiUrl).contentType(CONTENTTYPE)).andExpect(status().isOk());
    mockMvc.perform(get(apiUrl + stored.getId()).contentType(CONTENTTYPE)).andExpect(status().isOk());

    mockMvc.perform(get(apiUrl).contentType(CONTENTTYPE).with(allowedUser())).andExpect(status().isOk());
    mockMvc.perform(get(apiUrl + stored.getId()).contentType(CONTENTTYPE).with(allowedUser())).andExpect(status().isOk());

    // update
    stored.setFirstName(stored.getFirstName() + Math.random());
    mockMvc.perform(put(apiUrl).content(this.json(stored)).contentType(CONTENTTYPE)).andExpect(status().isUnauthorized());
    mockMvc.perform(put(apiUrl).content(this.json(stored)).contentType(CONTENTTYPE).with(allowedUser())).andExpect(status().isOk());

    // delete
    mockMvc.perform(delete(apiUrl).content(this.json(stored)).contentType(CONTENTTYPE)).andExpect(status().isUnauthorized());
    mockMvc.perform(delete(apiUrl).content(this.json(stored)).contentType(CONTENTTYPE).with(allowedUser())).andExpect(status().isOk());
  }


  // ##############################################################################################
  // ############# HELPER FUNCTIONS
  // ##############################################################################################

  private <T> T readObj(Class clazz, ResultActions res) throws Exception {
    return new ObjectMapper().readerFor(clazz).readValue(res.andReturn().getResponse().getContentAsString());
  }

  /**
   * converts an object to an json string
   *
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

}
